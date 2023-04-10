package org.bboss.elasticsearchtest.bulkprocessor;

import com.frameworkset.common.poolman.BatchHandler;
import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.util.SQLUtil;
import com.frameworkset.util.SimpleStringUtil;
import org.frameworkset.bulk.*;
import org.frameworkset.util.concurrent.Count;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.Thread.sleep;

/**
 * 持久化触点地址，url地址
 */
public class PersistentBulkProcessor {
    private static Logger logger = LoggerFactory.getLogger(PersistentBulkProcessor.class);
    public static void main(String[] args){
        int bulkSize = 150;
        int workThreads = 5;
        int workThreadQueue = 100;
		final ConfigSQLExecutor executor = new ConfigSQLExecutor("dbbulktest.xml");//加载sql配置文件，初始化一个db dao组件
//		DBInit.startDatasource(""); //初始化bboss数据源方法，参考文档：https://doc.bbossgroups.com/#/persistent/PersistenceLayer1
        SQLUtil.startPool("default",//数据源名称
                "com.mysql.jdbc.Driver",//oracle驱动
                "jdbc:mysql://localhost:3306/bboss",//mysql链接串
                "root","123456",//数据库账号和口令
                "select 1 " //数据库连接校验sql
        );
        //定义BulkProcessor批处理组件构建器
		CommonBulkProcessorBuilder bulkProcessorBuilder = new CommonBulkProcessorBuilder();
        bulkProcessorBuilder.setBlockedWaitTimeout(-1)//指定bulk工作线程缓冲队列已满时后续添加的bulk处理排队等待时间，如果超过指定的时候bulk将被拒绝处理，单位：毫秒，默认为0，不拒绝并一直等待成功为止

                .setBulkSizes(bulkSize)//按批处理数据记录数
                .setFlushInterval(5000)//强制bulk操作时间，单位毫秒，如果自上次bulk操作flushInterval毫秒后，数据量没有满足BulkSizes对应的记录数，但是有记录，那么强制进行bulk处理

                .setWarnMultsRejects(1000)//由于没有空闲批量处理工作线程，导致bulk处理操作出于阻塞等待排队中，BulkProcessor会对阻塞等待排队次数进行计数统计，bulk处理操作被每被阻塞排队WarnMultsRejects次（1000次），在日志文件中输出拒绝告警信息
                .setWorkThreads(workThreads)//bulk处理工作线程数
                .setWorkThreadQueue(workThreadQueue)//bulk处理工作线程池缓冲队列大小
                .setBulkProcessorName("db_bulkprocessor")//工作线程名称，实际名称为BulkProcessorName-+线程编号
                .setBulkRejectMessage("db bulkprocessor ")//bulk处理操作被每被拒绝WarnMultsRejects次（1000次），在日志文件中输出拒绝告警信息提示前缀
                .addBulkInterceptor(new CommonBulkInterceptor() {// 添加异步处理结果回调函数
					/**
					 * 执行前回调方法
					 * @param bulkCommand
					 */
					public void beforeBulk(CommonBulkCommand bulkCommand) {
                        //查看队列中追加的总记录数
                        logger.info("appendSize:"+bulkCommand.getAppendRecords());
                        //查看已经被处理成功的总记录数
                        logger.info("totalSize:"+bulkCommand.getTotalSize());
                        //查看处理失败的记录数
                        logger.info("totalFailedSize:"+bulkCommand.getTotalFailedSize());
                    }

					/**
					 * 执行成功回调方法
					 * @param bulkCommand
					 * @param result
					 */
					public void afterBulk(CommonBulkCommand bulkCommand, BulkResult result) {
                       if(logger.isDebugEnabled()){
//                           logger.debug(result.getResult());
                       }
                        //查看队列中追加的总记录数
                        logger.info("appendSize:"+bulkCommand.getAppendRecords());
                        //查看已经被处理成功的总记录数
                        logger.info("totalSize:"+bulkCommand.getTotalSize());
                        //查看处理失败的记录数
                        logger.info("totalFailedSize:"+bulkCommand.getTotalFailedSize());
                    }

					/**
					 * 执行异常回调方法
					 * @param bulkCommand
					 * @param exception
					 */
					public void exceptionBulk(CommonBulkCommand bulkCommand, Throwable exception) {
                        if(logger.isErrorEnabled()){
                            logger.error("exceptionBulk",exception);
                        }
                        //查看队列中追加的总记录数
                        logger.info("appendSize:"+bulkCommand.getAppendRecords());
                        //查看已经被处理成功的总记录数
                        logger.info("totalSize:"+bulkCommand.getTotalSize());
                        //查看处理失败的记录数
                        logger.info("totalFailedSize:"+bulkCommand.getTotalFailedSize());
                    }

					/**
					 * 执行过程中部分数据有问题回调方法
					 * @param bulkCommand
					 * @param result
					 */
					public void errorBulk(CommonBulkCommand bulkCommand, BulkResult result) {
                        if(logger.isWarnEnabled()){
//                            logger.warn(result);
                        }
                        //查看队列中追加的总记录数
                        logger.info("appendSize:"+bulkCommand.getAppendRecords());
                        //查看已经被处理成功的总记录数
                        logger.info("totalSize:"+bulkCommand.getTotalSize());
                        //查看处理失败的记录数
                        logger.info("totalFailedSize:"+bulkCommand.getTotalFailedSize());
                    }
                })//添加批量处理执行拦截器，可以通过addBulkInterceptor方法添加多个拦截器
				/**
				 * 设置执行数据批处理接口，实现对数据的异步批处理功能逻辑
				 */
				.setBulkAction(new BulkAction() {
					public BulkResult execute(CommonBulkCommand command) {
						List<CommonBulkData> bulkDataList = command.getBatchBulkDatas();//拿出要进行批处理操作的数据
						List<PositionUrl> positionUrls = new ArrayList<PositionUrl>();
						for(int i = 0; i < bulkDataList.size(); i ++){
							CommonBulkData commonBulkData = bulkDataList.get(i);
							/**
							 * 可以根据操作类型，对数据进行相应处理
							 */
//							if(commonBulkData.getType() == CommonBulkData.INSERT) 新增记录
//							if(commonBulkData.getType() == CommonBulkData.UPDATE) 修改记录
//							if(commonBulkData.getType() == CommonBulkData.DELETE) 删除记录
								positionUrls.add((PositionUrl)commonBulkData.getData());

						}
						BulkResult bulkResult = new BulkResult();//构建批处理操作结果对象
						try {
							//调用数据库dao executor，将数据批量写入数据库，对应的sql语句addPositionUrl在xml配置文件dbbulktest.xml中定义
							executor.executeBatch("addPositionUrl", positionUrls, 150, new BatchHandler<PositionUrl>() {
								public void handler(PreparedStatement stmt, PositionUrl record, int i) throws SQLException {
									//id,positionUrl,positionName,createTime
									stmt.setString(1, record.getId());
									stmt.setString(2, record.getPositionUrl());
									stmt.setString(3, record.getPositionName());
									stmt.setTimestamp(4, record.getCreatetime());
								}
							});

						}
						catch (Exception e){
							//如果执行出错，则将错误信息设置到结果中
							logger.error("",e);
							bulkResult.setError(true);
							bulkResult.setErrorInfo(e.getMessage());
						}
						return bulkResult;
					}
				})
        ;
        /**
         * 构建BulkProcessor批处理组件，一般作为单实例使用，单实例多线程安全，可放心使用
         */
		final CommonBulkProcessor bulkProcessor = bulkProcessorBuilder.build();//构建批处理作业组件

		//构建一个线程，向bulkProcessor中添加要处理的记录
		final Count count = new Count();
		Thread dataproducer = new Thread(new Runnable() {
			public void run() {
					do{
						if(bulkProcessor.isShutdown())//如果已经关闭异步批处理器，中断插入数据
							break;
						int i = count.increament();
						PositionUrl entity = new PositionUrl();
						entity.setId(UUID.randomUUID().toString());
						entity.setPositionUrl("positionUrl"+ i);
						entity.setPositionName("position.getPostionName()"+i);
						Timestamp time = new Timestamp(System.currentTimeMillis());
						entity.setCreatetime(time);
						logger.info(SimpleStringUtil.object2json(entity));
						bulkProcessor.insertData(entity);//添加一条记录
						try {
							sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}while(true);
			}
		});
		dataproducer.start();//启动添加数据线程

    }


}
