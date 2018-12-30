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
import org.frameworkset.elasticsearch.client.ClientUtil;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class TestScrollQuery {

	@Test
	public void testScroll(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/scroll.xml");
		//scroll分页检索
		List<String > scrollIds = new ArrayList<String>();
		long starttime = System.currentTimeMillis();
		Map params = new HashMap();

		params.put("size", 100);//每页100条记录
		ESDatas<Map> response = clientUtil.searchList("demo/_search?scroll=10s","scrollQuery",params,Map.class);
		List<Map> datas = response.getDatas();
		long realTotalSize = datas.size();
		long totalSize = response.getTotalSize();
		String scrollId = response.getScrollId();
		if(scrollId != null)
			scrollIds.add(scrollId);
		System.out.println("totalSize:"+totalSize);
		System.out.println("scrollId:"+scrollId);
		if(datas != null && datas.size() > 0) {//每页1000条记录，迭代scrollid，遍历scroll分页结果
			do {

				response = clientUtil.searchScroll("10s",scrollId,Map.class);
//				if(scrollIds != null && scrollIds.size() > 0) {
//					clientUtil.deleteScrolls(scrollIds);
//					scrollIds.clear();
//				}
				scrollId = response.getScrollId();
				totalSize = response.getTotalSize();
				System.out.println("scroll totalSize:"+totalSize);
				if(scrollId != null) {
					scrollIds.add(scrollId);

				}
				datas = response.getDatas();

				if(datas == null || datas.size() == 0){
					break;
				}
				realTotalSize = realTotalSize + datas.size();
			} while (true);
		}
		long endtime = System.currentTimeMillis();
		System.out.println("耗时："+(endtime - starttime)+",realTotalSize："+realTotalSize+",totalSize:"+totalSize);
		//查询存在es服务器上的scroll上下文信息
		String scrolls = clientUtil.executeHttp("_nodes/stats/indices/search?pretty", ClientUtil.HTTP_GET);
//		System.out.println(scrolls);
		//处理完毕后清除scroll上下文信息
		if(scrollIds.size() > 0) {
			try {
				scrolls = clientUtil.deleteScrolls(scrollIds);
			}
			catch (Exception e){

			}
			System.out.println(scrolls);
		}
		//清理完毕后查看scroll上下文信息
		scrolls = clientUtil.executeHttp("_nodes/stats/indices/search?pretty", ClientUtil.HTTP_GET);
		System.out.println(scrolls);
	}

	/**
	 * 串行方式执行slice scroll操作
	 */
	@Test
	public void testSliceScroll() {
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/scroll.xml");
		List<String> scrollIds = new ArrayList<String>();
		long starttime = System.currentTimeMillis();
		//scroll slice分页检索
		int max = 6;
		long realTotalSize = 0;
		for (int i = 0; i < max; i++) {
			Map params = new HashMap();
			params.put("sliceId", i);
			params.put("sliceMax", max);//最多6个slice，不能大于share数
			params.put("size", 100);//每页100条记录
			ESDatas<Map> sliceResponse = clientUtil.searchList("demo/_search?scroll=1m",
					"scrollSliceQuery", params,Map.class);
			List<Map> sliceDatas = sliceResponse.getDatas();
			realTotalSize = realTotalSize + sliceDatas.size();
			long totalSize = sliceResponse.getTotalSize();
			String scrollId = sliceResponse.getScrollId();
			if (scrollId != null)
				scrollIds.add(scrollId);
			System.out.println("totalSize:" + totalSize);
			System.out.println("scrollId:" + scrollId);
			if (sliceDatas != null && sliceDatas.size() >= 100) {//每页100条记录，迭代scrollid，遍历scroll分页结果
				do {
					sliceResponse = clientUtil.searchScroll("1m", scrollId, Map.class);
					String sliceScrollId = sliceResponse.getScrollId();
					if (sliceScrollId != null)
						scrollIds.add(sliceScrollId);
					sliceDatas = sliceResponse.getDatas();
					if (sliceDatas == null || sliceDatas.size() < 100) {
						break;
					}
					realTotalSize = realTotalSize + sliceDatas.size();
				} while (true);
			}
		}
		long endtime = System.currentTimeMillis();
		System.out.println("耗时："+(endtime - starttime)+",realTotalSize："+realTotalSize);
		//查询存在es服务器上的scroll上下文信息
		String scrolls = clientUtil.executeHttp("_nodes/stats/indices/search", ClientUtil.HTTP_GET);
		System.out.println(scrolls);
		//处理完毕后清除scroll上下文信息
		if(scrollIds.size() > 0) {
			scrolls = clientUtil.deleteScrolls(scrollIds);
			System.out.println(scrolls);
		}
		//清理完毕后查看scroll上下文信息
		scrolls = clientUtil.executeHttp("_nodes/stats/indices/search", ClientUtil.HTTP_GET);
		System.out.println(scrolls);
	}


	//用来存放实际slice检索总记录数
	long realTotalSize ;
	//辅助方法，用来累计每次scroll获取到的记录数
	synchronized void incrementSize(int size){
		this.realTotalSize = this.realTotalSize + size;
	}
	/**
	 * 并行方式执行slice scroll操作
	 */
	@Test
	public void testParralSliceScroll() {
		final ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/scroll.xml");
		final List<String> scrollIds = new ArrayList<String>();
		long starttime = System.currentTimeMillis();
		//scroll slice分页检索
		final int max = 6;
		final CountDownLatch countDownLatch = new CountDownLatch(max);//线程任务完成计数器，每个线程对应一个sclice,每运行完一个slice任务,countDownLatch计数减去1

		for (int j = 0; j < max; j++) {
			final int i = j;
			Thread sliceThread = new Thread(new Runnable() {//多线程并行执行scroll操作做，每个线程对应一个sclice

				public void run() {
					Map params = new HashMap();
					params.put("sliceId", i);
					params.put("sliceMax", max);//最多6个slice，不能大于share数
					params.put("size", 100);//每页100条记录
					ESDatas<Map> sliceResponse = clientUtil.searchList("demo/_search?scroll=1m",
							"scrollSliceQuery", params,Map.class);
					List<Map> sliceDatas = sliceResponse.getDatas();
					if(sliceDatas != null)
						incrementSize( sliceDatas.size());//统计实际处理的文档数量
					long totalSize = sliceResponse.getTotalSize();
					String scrollId = sliceResponse.getScrollId();
					if (scrollId != null)
						scrollIds.add(scrollId);
					System.out.println("totalSize:" + totalSize);
					System.out.println("scrollId:" + scrollId);
					if (sliceDatas != null && sliceDatas.size() >= 100) {//每页100条记录，迭代scrollid，遍历scroll分页结果
						do {
							sliceResponse = clientUtil.searchScroll("1m", scrollId, Map.class);
							String sliceScrollId = sliceResponse.getScrollId();
							if (sliceScrollId != null)
								scrollIds.add(sliceScrollId);
							sliceDatas = sliceResponse.getDatas();
							if (sliceDatas == null || sliceDatas.size() < 100) {
								break;
							}
							incrementSize( sliceDatas.size());//统计实际处理的文档数量
						} while (true);
					}
					countDownLatch.countDown();//slice检索完毕后计数器减1
				}

			});
			sliceThread.start();
		}
		try {
			countDownLatch.await();//等待所有的线程执行完毕,计数器变成0
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long endtime = System.currentTimeMillis();
		System.out.println("耗时："+(endtime - starttime)+",realTotalSize："+realTotalSize);
		//查询存在es服务器上的scroll上下文信息
		String scrolls = clientUtil.executeHttp("_nodes/stats/indices/search", ClientUtil.HTTP_GET);
//		System.out.println(scrolls);
		//处理完毕后清除scroll上下文信息
		if(scrollIds.size() > 0) {
			scrolls = clientUtil.deleteScrolls(scrollIds);
//			System.out.println(scrolls);
		}
		//清理完毕后查看scroll上下文信息
		scrolls = clientUtil.executeHttp("_nodes/stats/indices/search", ClientUtil.HTTP_GET);
//		System.out.println(scrolls);
	}

}
