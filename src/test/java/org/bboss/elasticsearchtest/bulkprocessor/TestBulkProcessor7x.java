package org.bboss.elasticsearchtest.bulkprocessor;
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

import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.frameworkset.elasticsearch.bulk.*;
import org.frameworkset.elasticsearch.client.ClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: for elasticsearch 7x</p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/12/8 9:57
 * @author biaoping.yin
 * @version 1.0
 */
public class TestBulkProcessor7x {
	private static Logger logger = LoggerFactory.getLogger(TestBulkProcessor7x.class);
	/**
	 * BulkProcessor批处理组件，一般作为单实例使用，单实例多线程安全，可放心使用
	 */
	private BulkProcessor bulkProcessor;
	private BulkProcessor logBulkProcessor;
	public static void main(String[] args){
		TestBulkProcessor7x testBulkProcessor = new TestBulkProcessor7x();
        //设置批量记录占用内存最大值，以字节为单位，达到最大值时，执行一次bulk操作
        // 可以根据实际情况调整maxMemSize参数，如果不设置maxMemSize，则按照按批处理数据记录数BulkSizes来判别是否执行执行一次bulk操作
        //maxMemSize参数默认值为0，不起作用，只有>0才起作用
		testBulkProcessor.buildBulkProcessor(1*1024*1024,100);//1M

//        testBulkProcessor.buildBulkProcessor(0);//20M
//		testBulkProcessor.buildLogBulkProcessor();
		testBulkProcessor.testBulkDatas();
		
		testBulkProcessor.shutdown(false);//调用shutDown停止方法后，BulkProcessor不会接收新的请求，但是会处理完所有已经进入bulk队列的数据


	}
	public void buildBulkProcessor(int maxMemSize,int bulkSize){
		//定义BulkProcessor批处理组件构建器
		BulkProcessorBuilder bulkProcessorBuilder = new BulkProcessorBuilder();
		bulkProcessorBuilder.setBlockedWaitTimeout(0)//指定bulk数据缓冲队列已满时后续添加的bulk数据排队等待时间，如果超过指定的时候数据将被拒绝处理，单位：毫秒，默认为0，不拒绝并一直等待成功为止
				.setBulkSizes(bulkSize)//按批处理数据记录数，达到BulkSizes对应的值时，执行一次bulk操作
                //设置批量记录占用内存最大值，以字节为单位，达到最大值时，执行一次bulk操作
                // 可以根据实际情况调整maxMemSize参数，如果不设置maxMemSize，则按照按批处理数据记录数BulkSizes来判别是否执行执行一次bulk操作
                //maxMemSize参数默认值为0，不起作用，只有>0才起作用
                .setMaxMemSize(maxMemSize)
				.setFlushInterval(5000)//强制bulk操作时间，单位毫秒，如果自上次bulk操作flushInterval毫秒后，数据量没有满足BulkSizes对应的记录数，或者没有满足maxMemSize，但是有记录，那么强制进行bulk处理
				
				.setWarnMultsRejects(1000)//bulk处理操作被每被拒绝WarnMultsRejects次（1000次），在日志文件中输出拒绝告警信息
				.setWorkThreads(2)//bulk处理工作线程数
				.setWorkThreadQueue(2)//bulk处理工作线程池缓冲队列大小
				.setBulkProcessorName("test_bulkprocessor")//工作线程名称，实际名称为BulkProcessorName-+线程编号
				.setBulkRejectMessage("Reject test bulkprocessor")//bulk处理操作被每被拒绝WarnMultsRejects次（1000次），在日志文件中输出拒绝告警信息提示前缀
				.setElasticsearch("default")//指定Elasticsearch集群数据源名称，bboss可以支持多数据源
				.addBulkInterceptor(new BulkInterceptor() {
					public void beforeBulk(BulkCommand bulkCommand) {
						logger.debug("beforeBulk");
					}

					public void afterBulk(BulkCommand bulkCommand, String result) {
                        List<BulkData> bulkDatas = bulkCommand.getBatchBulkDatas();
						logger.debug("afterBulk："+result);
						logger.debug("totalSize:"+bulkCommand.getTotalSize());
						logger.debug("totalFailedSize:"+bulkCommand.getTotalFailedSize());
					}

					public void exceptionBulk(BulkCommand bulkCommand, Throwable exception) {
						logger.error("exceptionBulk：",exception);
                        List<BulkData> bulkDatas = bulkCommand.getBatchBulkDatas();
					}
					public void errorBulk(BulkCommand bulkCommand, String result) {
						logger.warn("errorBulk："+result);
                        List<BulkData> bulkDatas = bulkCommand.getBatchBulkDatas();
					}
				})
				//为了提升性能，并没有把所有响应数据都返回，过滤掉了部分数据，可以自行设置FilterPath进行控制
				.setFilterPath("took,errors,items.*.error")

				// 重试配置
				.setBulkRetryHandler(new BulkRetryHandler() { //设置重试判断策略，哪些异常需要重试
					public boolean neadRetry(Exception exception, BulkCommand bulkCommand) { //判断哪些异常需要进行重试
						if (exception instanceof HttpHostConnectException     //NoHttpResponseException 重试
								|| exception instanceof ConnectTimeoutException //连接超时重试
								|| exception instanceof UnknownHostException
								|| exception instanceof NoHttpResponseException
//              				|| exception instanceof SocketTimeoutException    //响应超时不重试，避免造成业务数据不一致
						) {

							return true;//需要重试
						}

						if(exception instanceof SocketException){
							String message = exception.getMessage();
							if(message != null && message.trim().equals("Connection reset")) {
								return true;//需要重试
							}
						}

						return false;//不需要重试
					}
				})
				.setRetryTimes(3) // 设置重试次数，默认为0，设置 > 0的数值，会重试给定的次数，否则不会重试
				.setRetryInterval(1000l) // 可选，默认为0，不等待直接进行重试，否则等待给定的时间再重试

				// https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-bulk.html
				//下面的参数都是bulk url请求的参数：RefreshOption和其他参数只能二选一，配置了RefreshOption（类似于refresh=true&&aaaa=bb&cc=dd&zz=ee这种形式，将相关参数拼接成合法的url参数格式）就不能配置其他参数，
				// 其中的refresh参数控制bulk操作结果强制refresh入elasticsearch，便于实时查看数据，测试环境可以打开，生产不要设置
//				.setRefreshOption("refresh")
//				.setTimeout("100s")
//				.setMasterTimeout("50s")
//				.setRefresh("true")
//				.setWaitForActiveShards(2)
//				.setRouting("1") //(Optional, string) Target the specified primary shard.
//				.setPipeline("1") // (Optional, string) ID of the pipeline to use to preprocess incoming documents.
		;
		/**
		 * 构建BulkProcessor批处理组件，一般作为单实例使用，单实例多线程安全，可放心使用
		 */
		bulkProcessor = bulkProcessorBuilder.build();//构建批处理作业组件
	}
	public void buildLogBulkProcessor(){
		//定义BulkProcessor批处理组件构建器
		BulkProcessorBuilder bulkProcessorBuilder = new BulkProcessorBuilder();
		bulkProcessorBuilder.setBlockedWaitTimeout(0)//指定bulk数据缓冲队列已满时后续添加的bulk数据排队等待时间，如果超过指定的时候数据将被拒绝处理，单位：毫秒，默认为0，不拒绝并一直等待成功为止
				.setBulkSizes(10)//按批处理数据记录数
                //设置批量记录占用内存最大值20M，以字节为单位，达到最大值时，执行一次bulk操作，
                // 可以根据实际情况调整maxMemSize参数，如果不设置maxMemSize，则按照按批处理数据记录数BulkSizes来判别是否执行执行一次bulk操作
                .setMaxMemSize(20*1024*1024)
				.setFlushInterval(5000)//强制bulk操作时间，单位毫秒，如果自上次bulk操作flushInterval毫秒后，数据量没有满足BulkSizes对应的记录数，但是有记录，那么强制进行bulk处理

				.setWarnMultsRejects(1000)//bulk处理操作被每被拒绝WarnMultsRejects次（1000次），在日志文件中输出拒绝告警信息
				.setWorkThreads(2)//bulk处理工作线程数
				.setWorkThreadQueue(2)//bulk处理工作线程池缓冲队列大小
				.setBulkProcessorName("test_bulkprocessor")//工作线程名称，实际名称为BulkProcessorName-+线程编号
				.setBulkRejectMessage("Reject test bulkprocessor")//bulk处理操作被每被拒绝WarnMultsRejects次（1000次），在日志文件中输出拒绝告警信息提示前缀
				.setElasticsearch("logs")//指定Elasticsearch集群数据源名称，bboss可以支持多数据源
				.addBulkInterceptor(new BulkInterceptor() {
					public void beforeBulk(BulkCommand bulkCommand) {
						System.out.println("beforeBulk");
					}

					public void afterBulk(BulkCommand bulkCommand, String result) {
						System.out.println("afterBulk："+result);
						System.out.println("totalSize:"+bulkCommand.getTotalSize());
						System.out.println("totalFailedSize:"+bulkCommand.getTotalFailedSize());
					}

					public void exceptionBulk(BulkCommand bulkCommand, Throwable exception) {
						System.out.println("exceptionBulk：");
						exception.printStackTrace();
					}
					public void errorBulk(BulkCommand bulkCommand, String result) {
						System.out.println("errorBulk："+result);
					}
				})

				// 重试配置
				.setBulkRetryHandler(new BulkRetryHandler() { //设置重试判断策略，哪些异常需要重试
					public boolean neadRetry(Exception exception, BulkCommand bulkCommand) { //判断哪些异常需要进行重试
						if (exception instanceof HttpHostConnectException     //NoHttpResponseException 重试
								|| exception instanceof ConnectTimeoutException //连接超时重试
								|| exception instanceof UnknownHostException
								|| exception instanceof NoHttpResponseException
//              				|| exception instanceof SocketTimeoutException    //响应超时不重试，避免造成业务数据不一致
						) {

							return true;//需要重试
						}

						if(exception instanceof SocketException){
							String message = exception.getMessage();
							if(message != null && message.trim().equals("Connection reset")) {
								return true;//需要重试
							}
						}

						return false;//不需要重试
					}
				})
				.setRetryTimes(3) // 设置重试次数，默认为0，设置 > 0的数值，会重试给定的次数，否则不会重试
				.setRetryInterval(1000l) // 可选，默认为0，不等待直接进行重试，否则等待给定的时间再重试

		// https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-bulk.html
		//下面的参数都是bulk url请求的参数：RefreshOption和其他参数只能二选一，配置了RefreshOption（类似于refresh=true&&aaaa=bb&cc=dd&zz=ee这种形式，将相关参数拼接成合法的url参数格式）就不能配置其他参数，
		// 其中的refresh参数控制bulk操作结果强制refresh入elasticsearch，便于实时查看数据，测试环境可以打开，生产不要设置
//				.setRefreshOption("refresh")
//				.setTimeout("100s")
//				.setMasterTimeout("50s")
//				.setRefresh("true")
//				.setWaitForActiveShards(2)
//				.setRouting("1") //(Optional, string) Target the specified primary shard.
//				.setPipeline("1") // (Optional, string) ID of the pipeline to use to preprocess incoming documents.
		;
		/**
		 * 构建BulkProcessor批处理组件，一般作为单实例使用，单实例多线程安全，可放心使用
		 */
		logBulkProcessor = bulkProcessorBuilder.build();//构建批处理作业组件
	}
	public void testBulkDatas(){
		System.out.println("testBulkDatas");
		ClientOptions clientOptions = new ClientOptions();
		clientOptions.setIdField("id")//通过clientOptions指定map中的key为id的字段值作为文档_id，
//		          .setEsRetryOnConflict(1)
//							.setPipeline("1")

//				.setOpType("index")
//				.setIfPrimaryTerm(2l)
//				.setIfSeqNo(3l)
		;
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("name","duoduo1");
		data.put("id",1);
		bulkProcessor.insertData("bulkdemo",data,clientOptions);
		data = new HashMap<String,Object>();
		data.put("name","duoduo2");
		data.put("id",2);
		bulkProcessor.insertData("bulkdemo",data,clientOptions);
		data = new HashMap<String,Object>();
		data.put("name","duoduo3");
		data.put("id",3);
		bulkProcessor.insertData("bulkdemo",data,clientOptions);
		data = new HashMap<String,Object>();
		data.put("name","duoduo4");
		data.put("id",4);
		bulkProcessor.insertData("bulkdemo",data,clientOptions);
		data = new HashMap<String,Object>();
		data.put("name","duoduo5");
		data.put("id",5);

		bulkProcessor.insertData("bulkdemo",data,clientOptions);
//		ClientOptions deleteclientOptions = new ClientOptions();
		TestObject testObject = new TestObject();
		testObject.setId("1000");
		testObject.setName("duoduo1000");
		clientOptions.setIdField("id");//通过clientOptions指定map中的key为id的字段值作为文档_id，
		bulkProcessor.insertData("bulkdemo",testObject,clientOptions);

//		deleteclientOptions.setEsRetryOnConflict(1);
		//.setPipeline("1")
//		bulkProcessor.deleteDataWithClientOptions("bulkdemo","1",deleteclientOptions);
		bulkProcessor.deleteData("bulkdemo","1");//验证error回调方法
		List<Object> datas = new ArrayList<Object>();
		for(int i = 6; i < 106; i ++) {
			data = new HashMap<String,Object>();
			data.put("name","duoduo"+i);
			data.put("id",i);
			datas.add(data);
		}
		bulkProcessor.insertDatas("bulkdemo",datas);
		data = new HashMap<String,Object>();
		data.put("name",5);//error data
		data.put("id",5);
		ClientOptions updateOptions = new ClientOptions();
//		List<String> sourceUpdateExcludes = new ArrayList<String>();
//		sourceUpdateExcludes.add("name");
		//					updateOptions.setSourceUpdateExcludes(sourceUpdateExcludes); //es 7不起作用
//		List<String> sourceUpdateIncludes = new ArrayList<String>();
//		sourceUpdateIncludes.add("name");
//
//		/**
//		 * ersion typesedit
//		 * In addition to the external version type, Elasticsearch also supports other types for specific use cases:
//		 *
//		 * internal
//		 * Only index the document if the given version is identical to the version of the stored document.
//		 * external or external_gt
//		 * Only index the document if the given version is strictly higher than the version of the stored document or if there is no existing document. The given version will be used as the new version and will be stored with the new document. The supplied version must be a non-negative long number.
//		 * external_gte
//		 * Only index the document if the given version is equal or higher than the version of the stored document. If there is no existing document the operation will succeed as well. The given version will be used as the new version and will be stored with the new document. The supplied version must be a non-negative long number.
//		 * The external_gte version type is meant for special use cases and should be used with care. If used incorrectly, it can result in loss of data. There is another option, force, which is deprecated because it can cause primary and replica shards to diverge.
//		 */
//		updateOptions.setSourceUpdateIncludes(sourceUpdateIncludes);//es 7不起作用
		updateOptions
//				.setDetectNoop(false)
//				.setDocasupsert(false)
//				.setReturnSource(true)
//				.setEsRetryOnConflict(1)
				.setIdField("id") //验证exception回调方法
		//elasticsearch7不能同时指定EsRetryOnConflict和IfPrimaryTerm/IfSeqNo
				//.setVersion(10).setVersionType("internal") elasticsearch 7x必须使用IfPrimaryTerm和IfSeqNo代替version
//						.setIfPrimaryTerm(2l)
//				.setIfSeqNo(3l).setPipeline("1")
		;
		bulkProcessor.updateData("bulkdemo",data,updateOptions);


		data = new HashMap<String,Object>();
		data.put("id",1000);
		data.put("script","{\"name\":\"duoduo104\",\"goodsid\":104}");
		clientOptions = new ClientOptions();
		clientOptions.setIdField("id");
		clientOptions.setScriptField("script");
		bulkProcessor.insertData("bulkdemo",data,clientOptions);

		data = new HashMap<String,Object>();
		data.put("id",1000);
		data.put("script","{\"name\":\"updateduoduo104\",\"goodsid\":1104}");
		clientOptions = new ClientOptions();
		clientOptions.setIdField("id");
		clientOptions.setScriptField("script");
        clientOptions.setHaveScriptDoc(false);//默认值false
		bulkProcessor.updateData("bulkdemo",data,clientOptions);

	}
	
	public void shutdown(boolean asyn) {
		if(asyn) {
			Thread t = new Thread() {
				public void run() {
						bulkProcessor.shutDown();
						logBulkProcessor.shutDown();
				}
			};
			t.start();
		}
		else {
			bulkProcessor.shutDown();
//			logBulkProcessor.shutDown();
		}


//		System.out.println("bulkProcessor.getTotalSize():"+bulkProcessor.getTotalSize());
	}

}
