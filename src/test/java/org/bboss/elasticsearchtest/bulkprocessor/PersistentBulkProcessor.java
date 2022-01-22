package org.bboss.elasticsearchtest.bulkprocessor;

import com.frameworkset.common.poolman.BatchHandler;
import com.frameworkset.common.poolman.ConfigSQLExecutor;
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
		final ConfigSQLExecutor executor = new ConfigSQLExecutor("dbbulktest.xml");
//		DBInit.startDatasource("");
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
                .addBulkInterceptor(new CommonBulkInterceptor() {
                    public void beforeBulk(CommonBulkCommand bulkCommand) {

                    }

                    public void afterBulk(CommonBulkCommand bulkCommand, BulkResult result) {
                       if(logger.isDebugEnabled()){
//                           logger.debug(result.getResult());
                       }
                    }

                    public void exceptionBulk(CommonBulkCommand bulkCommand, Throwable exception) {
                        if(logger.isErrorEnabled()){
                            logger.error("exceptionBulk",exception);
                        }
                    }
                    public void errorBulk(CommonBulkCommand bulkCommand, BulkResult result) {
                        if(logger.isWarnEnabled()){
//                            logger.warn(result);
                        }
                    }
                })//添加批量处理执行拦截器，可以通过addBulkInterceptor方法添加多个拦截器
				.setBulkAction(new BulkAction() {
					public BulkResult execute(CommonBulkCommand command) {
						List<CommonBulkData> bulkDataList = command.getBatchBulkDatas();
						List<PositionUrl> positionUrls = new ArrayList<PositionUrl>();
						for(int i = 0; i < bulkDataList.size(); i ++){
							CommonBulkData commonBulkData = bulkDataList.get(i);
//							if(commonBulkData.getType() == CommonBulkData.INSERT)
//							if(commonBulkData.getType() == CommonBulkData.UPDATE)
//							if(commonBulkData.getType() == CommonBulkData.DELETE)
								positionUrls.add((PositionUrl)commonBulkData.getData());

						}
						BulkResult bulkResult = new BulkResult();
						try {
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
		final Count count = new Count();
		Thread dataproducer = new Thread(new Runnable() {
			public void run() {
					do{
						if(bulkProcessor.isShutdown())
							break;
						int i = count.increament();
						PositionUrl entity = new PositionUrl();
						entity.setId(UUID.randomUUID().toString());
						entity.setPositionUrl("positionUrl"+ i);
						entity.setPositionName("position.getPostionName()"+i);
						Timestamp time = new Timestamp(System.currentTimeMillis());
						entity.setCreatetime(time);
						logger.info(SimpleStringUtil.object2json(entity));
						bulkProcessor.insertData(entity);
						try {
							sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}while(true);
			}
		});
		dataproducer.start();

    }


}
