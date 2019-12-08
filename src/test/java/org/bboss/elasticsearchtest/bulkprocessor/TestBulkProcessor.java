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
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/12/8 9:57
 * @author biaoping.yin
 * @version 1.0
 */
public class TestBulkProcessor {
	/**
	 * BulkProcessor批处理组件，一般作为单实例使用，单实例多线程安全，可放心使用
	 */
	private BulkProcessor bulkProcessor;
	public static void main(String[] args){
		TestBulkProcessor testBulkProcessor = new TestBulkProcessor();
		testBulkProcessor.buildBulkProcessor();
		testBulkProcessor.testBulkDatas();

	}
	public void buildBulkProcessor(){
		//定义BulkProcessor批处理组件构建器
		BulkProcessorBuilder bulkProcessorBuilder = new BulkProcessorBuilder();
		bulkProcessorBuilder.setBlockedWaitTimeout(10000)//如果数据阻塞排队超过指定的时间，将被拒绝处理，单位：毫秒，默认为0，不拒绝
				.setBulkFailRetry(1)//如果处理失败，重试次数，暂时不起作用
				.setBulkQueue(1000)//bulk数据缓冲队列大小，越大处理速度越快，根据实际服务器内存资源配置，用户提交的数据首先进入这个队列，然后通过多个工作线程从这个队列中拉取数据进行处理
				.setBulkSizes(10)//按批处理数据记录数
				.setFlushInterval(5000)//强制bulk操作时间，单位毫秒，如果自上次bulk操作flushInterval毫秒后，数据量没有满足BulkSizes对应的记录数，但是有记录，那么强制进行bulk处理
				.setRefreshOption("refresh")//数据bulk操作结果强制refresh入elasticsearch，便于实时查看数据，测试环境可以打开，生产不要设置
				.setWarnMultsRejects(1000)//bulk处理操作被每被拒绝WarnMultsRejects次（1000次），在日志文件中输出拒绝告警信息
				.setWorkThreads(100)//bulk处理工作线程数
				.setWorkThreadQueue(100)//bulk处理工作线程池缓冲队列大小
				.setBulkProcessorName("test_bulkprocessor")//工作线程名称，实际名称为BulkProcessorName-+线程编号
				.setBulkRejectMessage("Reject test bulkprocessor")//bulk处理操作被每被拒绝WarnMultsRejects次（1000次），在日志文件中输出拒绝告警信息提示前缀
				.setElasticsearch("default")//指定Elasticsearch集群数据源名称，bboss可以支持多数据源
				.addBulkInterceptor(new BulkInterceptor() {
					public void beforeBulk(BulkCommand bulkCommand) {
						System.out.println("beforeBulk");
					}

					public void afterBulk(BulkCommand bulkCommand, String result) {
						System.out.println("afterBulk："+result);
					}

					public void errorBulk(BulkCommand bulkCommand, Throwable exception) {
						System.out.println("errorBulk：");
						exception.printStackTrace();
					}
				});
		/**
		 * 构建BulkProcessor批处理组件，一般作为单实例使用，单实例多线程安全，可放心使用
		 */
		bulkProcessor = bulkProcessorBuilder.build();//构建批处理作业组件
	}
	public void testBulkDatas(){
		System.out.println("testBulkDatas");
		ClientOptions clientOptions = new ClientOptions();
		clientOptions.setIdField("id");
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("name","duoduo1");
		data.put("id","1");
		bulkProcessor.insertData("bulkdemo",data,clientOptions);
		data = new HashMap<String,Object>();
		data.put("name","duoduo2");
		data.put("id","2");
		bulkProcessor.insertData("bulkdemo",data,clientOptions);
		data = new HashMap<String,Object>();
		data.put("name","duoduo3");
		data.put("id","3");
		bulkProcessor.insertData("bulkdemo",data,clientOptions);
		data = new HashMap<String,Object>();
		data.put("name","duoduo4");
		data.put("id","4");
		bulkProcessor.insertData("bulkdemo",data,clientOptions);
		data = new HashMap<String,Object>();
		data.put("name","duoduo5");
		data.put("id","5");

		bulkProcessor.insertData("bulkdemo",data,clientOptions);
		bulkProcessor.deleteData("bulkdemo","1");
		List<Object> datas = new ArrayList<Object>();
		for(int i = 6; i < 106; i ++) {
			data = new HashMap<String,Object>();
			data.put("name","duoduo"+i);
			data.put("id",""+i);
			datas.add(data);
		}
		bulkProcessor.insertDatas("bulkdemo",datas,clientOptions);
		data = new HashMap<String,Object>();
		data.put("name","updateduoduo5");
		data.put("id","5");
		bulkProcessor.updateData("bulkdemo",data,clientOptions);

	}

}
