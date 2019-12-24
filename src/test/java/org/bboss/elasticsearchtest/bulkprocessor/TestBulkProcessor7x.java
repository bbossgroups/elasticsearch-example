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

import org.frameworkset.elasticsearch.bulk.BulkCommand;
import org.frameworkset.elasticsearch.bulk.BulkInterceptor;
import org.frameworkset.elasticsearch.bulk.BulkProcessor;
import org.frameworkset.elasticsearch.bulk.BulkProcessorBuilder;
import org.frameworkset.elasticsearch.client.ClientOptions;

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
	/**
	 * BulkProcessor批处理组件，一般作为单实例使用，单实例多线程安全，可放心使用
	 */
	private BulkProcessor bulkProcessor;
	public static void main(String[] args){
		TestBulkProcessor7x testBulkProcessor = new TestBulkProcessor7x();
		testBulkProcessor.buildBulkProcessor();
		
		testBulkProcessor.testBulkDatas();
		
		testBulkProcessor.shutdown(false);//调用shutDown停止方法后，BulkProcessor不会接收新的请求，但是会处理完所有已经进入bulk队列的数据


	}
	public void buildBulkProcessor(){
		//定义BulkProcessor批处理组件构建器
		BulkProcessorBuilder bulkProcessorBuilder = new BulkProcessorBuilder();
		bulkProcessorBuilder.setBlockedWaitTimeout(0)//指定bulk数据缓冲队列已满时后续添加的bulk数据排队等待时间，如果超过指定的时候数据将被拒绝处理，单位：毫秒，默认为0，不拒绝并一直等待成功为止
				.setBulkFailRetry(1)//如果处理失败，重试次数，暂时不起作用
				.setBulkSizes(10)//按批处理数据记录数
				.setFlushInterval(5000)//强制bulk操作时间，单位毫秒，如果自上次bulk操作flushInterval毫秒后，数据量没有满足BulkSizes对应的记录数，但是有记录，那么强制进行bulk处理
				
				.setWarnMultsRejects(1000)//bulk处理操作被每被拒绝WarnMultsRejects次（1000次），在日志文件中输出拒绝告警信息
				.setWorkThreads(2)//bulk处理工作线程数
				.setWorkThreadQueue(2)//bulk处理工作线程池缓冲队列大小
				.setBulkProcessorName("test_bulkprocessor")//工作线程名称，实际名称为BulkProcessorName-+线程编号
				.setBulkRejectMessage("Reject test bulkprocessor")//bulk处理操作被每被拒绝WarnMultsRejects次（1000次），在日志文件中输出拒绝告警信息提示前缀
				.setElasticsearch("default")//指定Elasticsearch集群数据源名称，bboss可以支持多数据源
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
				// https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-bulk.html
				//下面的参数都是bulk url请求的参数：RefreshOption和其他参数只能二选一，配置了RefreshOption（类似于refresh=true&&aaaa=bb&cc=dd&zz=ee这种形式，将相关参数拼接成合法的url参数格式）就不能配置其他参数，
				// 其中的refresh参数控制bulk操作结果强制refresh入elasticsearch，便于实时查看数据，测试环境可以打开，生产不要设置
//				.setRefreshOption("refresh")
//				.setTimeout("100s")
				.setMasterTimeout("50s")
				.setRefresh("true")
//				.setWaitForActiveShards(2)
//				.setRouting("1") //(Optional, string) Target the specified primary shard.
//				.setPipeline("1") // (Optional, string) ID of the pipeline to use to preprocess incoming documents.
		;
		/**
		 * 构建BulkProcessor批处理组件，一般作为单实例使用，单实例多线程安全，可放心使用
		 */
		bulkProcessor = bulkProcessorBuilder.build();//构建批处理作业组件
	}
	public void testBulkDatas(){
		System.out.println("testBulkDatas");
		ClientOptions clientOptions = new ClientOptions();
		clientOptions.setIdField("id")//通过clientOptions指定map中的key为id的字段值作为文档_id，
		          .setEsRetryOnConflict(1)
//							.setPipeline("1")

				.setOpType("index")
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
		ClientOptions deleteclientOptions = new ClientOptions();
		 

		deleteclientOptions.setEsRetryOnConflict(1);
		//.setPipeline("1")
		bulkProcessor.deleteData("bulkdemo","1",deleteclientOptions);
		List<Object> datas = new ArrayList<Object>();
		for(int i = 6; i < 106; i ++) {
			data = new HashMap<String,Object>();
			data.put("name","duoduo"+i);
			data.put("id",i);
			datas.add(data);
		}
		bulkProcessor.insertDatas("bulkdemo",datas,clientOptions);
		data = new HashMap<String,Object>();
		data.put("name","updateduoduo5");
		data.put("id",5);
		ClientOptions updateOptions = new ClientOptions();
//		List<String> sourceUpdateExcludes = new ArrayList<String>();
//		sourceUpdateExcludes.add("name");
		//					updateOptions.setSourceUpdateExcludes(sourceUpdateExcludes); //es 7不起作用
		List<String> sourceUpdateIncludes = new ArrayList<String>();
		sourceUpdateIncludes.add("name");

		/**
		 * ersion typesedit
		 * In addition to the external version type, Elasticsearch also supports other types for specific use cases:
		 *
		 * internal
		 * Only index the document if the given version is identical to the version of the stored document.
		 * external or external_gt
		 * Only index the document if the given version is strictly higher than the version of the stored document or if there is no existing document. The given version will be used as the new version and will be stored with the new document. The supplied version must be a non-negative long number.
		 * external_gte
		 * Only index the document if the given version is equal or higher than the version of the stored document. If there is no existing document the operation will succeed as well. The given version will be used as the new version and will be stored with the new document. The supplied version must be a non-negative long number.
		 * The external_gte version type is meant for special use cases and should be used with care. If used incorrectly, it can result in loss of data. There is another option, force, which is deprecated because it can cause primary and replica shards to diverge.
		 */
		updateOptions.setSourceUpdateIncludes(sourceUpdateIncludes);//es 7不起作用
		updateOptions.setDetectNoop(false)
				.setDocasupsert(false)
				.setReturnSource(true)
//				.setEsRetryOnConflict(1)
				.setIdField("id") //elasticsearch7不能同时指定EsRetryOnConflict和IfPrimaryTerm/IfSeqNo
				//.setVersion(10).setVersionType("internal") elasticsearch 7x必须使用IfPrimaryTerm和IfSeqNo代替version
//						.setIfPrimaryTerm(2l)
//				.setIfSeqNo(3l).setPipeline("1")
		;
		bulkProcessor.updateData("bulkdemo",data,updateOptions);

	}
	
	public void shutdown(boolean asyn) {
		if(asyn) {
			Thread t = new Thread() {
				public void run() {
						bulkProcessor.shutDown();
				}
			};
			t.start();
		}
		else {
			bulkProcessor.shutDown();
		}


//		System.out.println("bulkProcessor.getTotalSize():"+bulkProcessor.getTotalSize());
	}

}
