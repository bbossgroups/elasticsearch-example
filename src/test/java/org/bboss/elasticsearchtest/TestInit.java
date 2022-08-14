package org.bboss.elasticsearchtest;
/**
 * Copyright 2020 bboss
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
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.scroll.HandlerInfo;
import org.frameworkset.elasticsearch.scroll.ScrollHandler;
import org.junit.Test;

import java.util.List;
import java.util.Map;
/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2020/4/23 17:09
 * @author biaoping.yin
 * @version 1.0
 */
public class TestInit {
	@Test
	public void init(){

		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		//get elasticsearch cluster state
		String result = clientUtil.executeHttp("_cluster/state?pretty",ClientInterface.HTTP_GET);

		//check indice twitter and index type tweet exist or not.
		//适用于elasticsearch 6及以下版本有类型，7以上版本无类型
		boolean exist1 = clientUtil.existIndiceType("twitter","tweet");
		System.out.println("twitter  tweet type exist:"+exist1);
		//适用于 Elasticsearch7以上的版本，check indice twitter exist or not，
		exist1 = clientUtil.existIndice("twitter");
		System.out.println("twitter exist:"+exist1);
		//count documents in indice twitter
		long count = clientUtil.countAll("twitter");
		System.out.println(count);

		//Get All documents of indice twitter,DEFAULT_FETCHSIZE is 5000
		//返回对象类型为Map，也可以指定为特定的PO对象，适用于记录数量不大的表
		ESDatas<Map> esDatas = clientUtil.searchAll("twitter", Map.class);
		//从esDatas中获取检索到的记录集合
		List<Map> datas = esDatas.getDatas();
		//从esDatas中获取检索到的记录总数
		long totalSize = esDatas.getTotalSize();

		//Get All documents of indice twitter,Set fetchsize to 10000, Using ScrollHandler to process each batch of datas.
		//指定批处理器分批次处理数据，适用于记录数量比较大的全表表数据查询
		clientUtil.searchAll("twitter",10000,new ScrollHandler<Map>() {
			public void handle(ESDatas<Map> esDatas, HandlerInfo handlerInfo) throws Exception {
				List<Map> dataList = esDatas.getDatas();
				System.out.println("TotalSize:"+esDatas.getTotalSize());
				if(dataList != null) {
					System.out.println("dataList.size:" + dataList.size());
				}
				else
				{
					System.out.println("dataList.size:0");
				}
				//do something other such as do a db query.
				//SQLExecutor.queryList(Map.class,"select * from td_sm_user");
			}
		},Map.class);
		//Use slice parallel scoll query all documents of indice  twitter by 2 thread tasks. DEFAULT_FETCHSIZE is 5000
		//You can also use ScrollHandler to process each batch of datas on your own.
		esDatas = clientUtil.searchAllParallel("twitter", Map.class,2);
		//指定批处理器分批次处理数据（适用于数据量比较大的表），并行检索和处理表数据源，线程数量为2，
		clientUtil.searchAllParallel("twitter",10000,new ScrollHandler<Map>() {
			public void handle(ESDatas<Map> esDatas, HandlerInfo handlerInfo) throws Exception {
				List<Map> dataList = esDatas.getDatas();
				System.out.println("TotalSize:"+esDatas.getTotalSize());
				if(dataList != null) {
					System.out.println("dataList.size:" + dataList.size());
				}
				else
				{
					System.out.println("dataList.size:0");
				}
				//do something other such as do a db query.
				//SQLExecutor.queryList(Map.class,"select * from td_sm_user");
			}
		},Map.class,2);
	}
}


