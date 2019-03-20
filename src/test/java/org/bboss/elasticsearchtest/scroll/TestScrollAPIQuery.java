package org.bboss.elasticsearchtest.scroll;/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.scroll.HandlerInfo;
import org.frameworkset.elasticsearch.scroll.ScrollHandler;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestScrollAPIQuery {
	@Test
	public void testSimleScrollAPI(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/scroll.xml");
		//scroll分页检索
		Map params = new HashMap();
		params.put("size", 1000);//每页10000条记录
		//scroll上下文有效期1分钟
		ESDatas<Map> response = clientUtil.scroll("demo/_search","scrollQuery","1m",params,Map.class);
		List<Map> datas = response.getDatas();
		long realTotalSize = datas.size();
		long totalSize = response.getTotalSize();
		System.out.println("totalSize:"+totalSize);
		System.out.println("realTotalSize:"+realTotalSize);
		System.out.println("countAll:"+clientUtil.countAll("demo"));
	}

	/**
	 * 串行方式执行slice scroll操作
	 */
	@Test
	public void testSimpleSliceScrollApi() {
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/scroll.xml");
		//scroll slice分页检索,max对应并行度
		int max = 6;
		Map params = new HashMap();
		params.put("sliceMax", max);//最多6个slice，不能大于share数
		params.put("size", 100);//每页100条记录
		//scroll上下文有效期1分钟
		ESDatas<Map> sliceResponse = clientUtil.scrollSlice("demo/_search",
				"scrollSliceQuery", params,"1m",Map.class);//串行
		System.out.println("totalSize:"+sliceResponse.getTotalSize());
		System.out.println("realSize size:"+sliceResponse.getDatas().size());
	}

	/**
	 * 并行方式执行slice scroll操作
	 */
	@Test
	public void testSimpleSliceScrollApiParral() {
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/scroll.xml");
		//scroll slice分页检索,max对应并行度
		int max = 6;
		Map params = new HashMap();
		params.put("sliceMax", max);//最多6个slice，不能大于share数，必须使用sliceMax作为变量名称
		params.put("size", 100);//每页100条记录
		//scroll上下文有效期2分钟
		ESDatas<Map> sliceResponse = clientUtil.scrollSliceParallel("demo/_search",
				"scrollSliceQuery", params,"2m",Map.class);//true表示并行
		System.out.println("totalSize:"+sliceResponse.getTotalSize());
		System.out.println("realSize size:"+sliceResponse.getDatas().size());

	}


	@Test
	public void testSimleScrollAPIHandler(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/scroll.xml");
		//scroll分页检索
		Map params = new HashMap();
		params.put("size", 5000);//每页5000条记录
		//采用自定义handler函数处理每个scroll的结果集后，response中只会包含总记录数，不会包含记录集合
		//scroll上下文有效期1分钟
		ESDatas<Map> response = clientUtil.scroll("demo/_search", "scrollQuery", "1m", params, Map.class, new ScrollHandler<Map>() {
			public void handle(ESDatas<Map> response, HandlerInfo handlerInfo) throws Exception {//自己处理每次scroll的结果
				List<Map> datas = response.getDatas();
				long totalSize = response.getTotalSize();
				System.out.println("totalSize:"+totalSize+",datas.size:"+datas.size());
			}
		});

		System.out.println("response realzie:"+response.getTotalSize());

	}

	@Test
	public void testSimleScrollParallelAPIHandler(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/scroll.xml");
		//scroll分页检索
		Map params = new HashMap();
		params.put("size", 10);//每页5000条记录
		//采用自定义handler函数处理每个scroll的结果集后，response中只会包含总记录数，不会包含记录集合
		//scroll上下文有效期1分钟
		ESDatas<Map> response = clientUtil.scrollParallel("demo/_search", "scrollQuery", "1m", params, Map.class, new ScrollHandler<Map>() {
			public void handle(ESDatas<Map> response, HandlerInfo handlerInfo) throws Exception {//自己处理每次scroll的结果
				List<Map> datas = response.getDatas();
				long totalSize = response.getTotalSize();
				System.out.println("totalSize:"+totalSize+",datas.size:"+datas.size());
			}
		});

		System.out.println("response realzie:"+response.getTotalSize());

	}

	/**
	 * 串行方式执行slice scroll操作
	 */
	@Test
	public void testSimpleSliceScrollApiHandler() {
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/scroll.xml");
		//scroll slice分页检索,max对应并行度
		int max = 6;
		Map params = new HashMap();
		params.put("sliceMax", max);//最多6个slice，不能大于share数，必须使用sliceMax作为变量名称
		params.put("size", 1000);//每页1000条记录
		//采用自定义handler函数处理每个slice scroll的结果集后，sliceResponse中只会包含总记录数，不会包含记录集合
		//scroll上下文有效期1分钟
		ESDatas<Map> sliceResponse = clientUtil.scrollSlice("demo/_search",
				"scrollSliceQuery", params,"1m",Map.class, new ScrollHandler<Map>() {
					public void handle(ESDatas<Map> response, HandlerInfo handlerInfo) throws Exception {//自己处理每次scroll的结果
						List<Map> datas = response.getDatas();
						long totalSize = response.getTotalSize();
						System.out.println("totalSize:"+totalSize+",datas.size:"+datas.size());
					}
				});//串行
		long totalSize = sliceResponse.getTotalSize();

		System.out.println("totalSize:"+totalSize);
	}

	/**
	 * 并行方式执行slice scroll操作
	 */
	@Test
	public void testSimpleSliceScrollApiParralHandler() {
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/scroll.xml");
		//scroll slice分页检索,max对应并行度
		int max = 6;
		Map params = new HashMap();
		params.put("sliceMax", max);//最多6个slice，不能大于share数，必须使用sliceMax作为变量名称
		params.put("size", 1000);//每页1000条记录
		//采用自定义handler函数处理每个slice scroll的结果集后，sliceResponse中只会包含总记录数，不会包含记录集合
		//scroll上下文有效期1分钟
		ESDatas<Map> sliceResponse = clientUtil.scrollSliceParallel("demo/_search",
				"scrollSliceQuery", params,"1m",Map.class, new ScrollHandler<Map>() {
					public void handle(ESDatas<Map> response, HandlerInfo handlerInfo) throws Exception {//自己处理每次scroll的结果,注意结果是异步检索的
						List<Map> datas = response.getDatas();
						long totalSize = response.getTotalSize();
						System.out.println("totalSize:"+totalSize+",datas.size:"+datas.size());
					}
				});//并行

		long totalSize = sliceResponse.getTotalSize();
		System.out.println("totalSize:"+totalSize);

	}

	/**
	 * 并行方式执行slice scroll操作：将一个es的数据导入另外一个es数据，需要在application.properties文件中定义一个es233的集群
	 */
	@Test
	public void testSimpleSliceScrollApiParralHandlerExport() {
		ClientInterface clientUtil522 = ElasticSearchHelper.getConfigRestClientUtil("esmapper/scroll.xml");

		final ClientInterface clientUtil234 = ElasticSearchHelper.getRestClientUtil("es233");
		//scroll slice分页检索,max对应并行度
		int max = 6;
		Map params = new HashMap();
		params.put("sliceMax", max);//最多6个slice，不能大于share数，必须使用sliceMax作为变量名称
		params.put("size", 1000);//每页1000条记录
		//采用自定义handler函数处理每个slice scroll的结果集后，sliceResponse中只会包含总记录数，不会包含记录集合
		//scroll上下文有效期1分钟
		ESDatas<Map> sliceResponse = clientUtil522.scrollSliceParallel("demo/_search",
				"scrollSliceQuery", params,"1m",Map.class, new ScrollHandler<Map>() {
					public void handle(ESDatas<Map> response, HandlerInfo handlerInfo) throws Exception {//自己处理每次scroll的结果,注意结果是异步检索的
						List<Map> datas = response.getDatas();
						clientUtil234.addDocuments("index233","indextype233",datas);
						long totalSize = response.getTotalSize();
						System.out.println("totalSize:"+totalSize+",datas.size:"+datas.size());
					}
				});//并行

		long totalSize = sliceResponse.getTotalSize();
		System.out.println("totalSize:"+totalSize);

	}





}
