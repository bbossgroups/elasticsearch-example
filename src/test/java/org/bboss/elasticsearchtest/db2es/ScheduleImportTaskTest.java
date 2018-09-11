package org.bboss.elasticsearchtest.db2es;
/**
 * Copyright 2008 biaoping.yin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.DataStream;
import org.frameworkset.elasticsearch.client.ImportBuilder;
import org.junit.Test;

import com.frameworkset.common.poolman.SQLExecutor;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/9/10 9:29
 * @author biaoping.yin
 * @version 1.0
 */
public class ScheduleImportTaskTest {
//	public static void main(String[] args){
//		testSimpleLogImportBuilderFromExternalDBConfig();
//	}
	/**
	 * 从外部application.properties文件中加载数据源配置和es配置
	 */
	@Test
	public void testSimpleLogImportBuilderFromExternalDBConfig(){
		ImportBuilder importBuilder = ImportBuilder.newInstance();
		try {
			//清除测试表,导入的时候回重建表，测试的时候加上为了看测试效果，实际线上环境不要删表
			ElasticSearchHelper.getRestClientUtil().dropIndice("dbdemo");
		}
		catch (Exception e){

		}


		//指定导入数据的sql语句，必填项，可以设置自己的提取逻辑
		importBuilder.setSql("select * from td_sm_log");
		/**
		 * es相关配置
		 */
		importBuilder
				.setIndex("dbdemo") //必填项
				.setIndexType("dbdemo") //必填项
				.setRefreshOption("refresh")//可选项，null表示不实时刷新，importBuilder.setRefreshOption("refresh");表示实时刷新
				.setUseJavaName(true) //可选项,将数据库字段名称转换为java驼峰规范的名称，例如:doc_id -> docId
				.setBatchSize(50);  //可选项,批量导入es的记录数，默认为-1，逐条处理，> 0时批量处理

		importBuilder.setFixedRate(false)
					 .setScheduleBatchSize(2)
					 .setDeyLay(1000L)
					 .setPeriod(10000L);
		importBuilder.setNumberLastValueColumn("log_id");
		importBuilder.setFromFirst(true);
		importBuilder.setLastValueStorePath("testdb");
		importBuilder.setLastValueStoreTableName("logs");

//		importBuilder.

		/**
		 * 一次、作业创建一个内置的线程池，实现多线程并行数据导入elasticsearch功能，作业完毕后关闭线程池
		 */
		importBuilder.setParallel(true);//设置为多线程并行批量导入
		importBuilder.setQueue(10);//设置批量导入线程池等待队列长度
		importBuilder.setThreadCount(50);//设置批量导入线程池工作线程数量
		importBuilder.setContinueOnError(true);//任务出现异常，是否继续执行作业：true（默认值）继续执行 false 中断作业执行
		importBuilder.setAsyn(false);//true 异步方式执行，不等待所有导入作业任务结束，方法快速返回；false（默认值） 同步方式执行，等待所有导入作业任务结束，所有作业结束后方法才返回
		importBuilder.setEsIdField("log_id");
		importBuilder.setDebugResponse(false);//设置是否将每次处理的reponse打印到日志文件中，默认false，不打印响应报文将大大提升性能，只有在需要的时候才，log日志级别同时要设置为INFO
//		importBuilder.setDiscardBulkResponse(true);//设置是否需要批量处理的响应报文，不需要设置为false，true为需要，默认true，如果不需要响应报文将大大提升处理速度
		/**
		 * 执行数据库表数据导入es操作
		 */
		DataStream dataStream = importBuilder.builder();
		dataStream.db2es();

		long count = ElasticSearchHelper.getRestClientUtil().countAll("dbdemo");
		try {
			Long dbcount = SQLExecutor.queryObject(Long.class, "select count(1) from (" + importBuilder.getSql()+ ") b");
			System.out.println("数据库记录数dbcount:" + dbcount);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("数据导入完毕后索引表dbdemo中的文档数量:"+count);
	}

}
