package org.bboss.elasticsearchtest.searchall;
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
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.scroll.ScrollHandler;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * <p>Description: 检索所有文档数据测试用例</p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/10/14 20:07
 * @author biaoping.yin
 * @version 1.0
 */
public class SearchAllTest {
	/**
	 * 统计索引中有多少文档
	 */
	@Test
	public void testCountAll(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		long esDatas = clientInterface.countAll("demo");
		System.out.println("TotalSize:"+esDatas);

	}
	/**
	 * 简单的检索索引表所有文档数据，默认分5000条记录一批从es获取数据
	 */
	@Test
	public void testSearchAll(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		ESDatas<Map> esDatas = clientInterface.searchAll("demo",Map.class);
		List<Map> dataList = esDatas.getDatas();
		System.out.println("TotalSize:"+esDatas.getTotalSize());
		if(dataList != null) {
			System.out.println("dataList.size:" + dataList.size());
		}
		else
		{
			System.out.println("dataList.size:0");
		}
	}
	/**
	 * 简单的检索索引表所有文档数据，按指定的10000条记录一批从es获取数据
	 */
	@Test
	public void testSearchAllFethchSize(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		ESDatas<Map> esDatas = clientInterface.searchAll("demo",10000,Map.class);
		List<Map> dataList = esDatas.getDatas();
		System.out.println("TotalSize:"+esDatas.getTotalSize());
		if(dataList != null) {
			System.out.println("dataList.size:" + dataList.size());
		}
		else
		{
			System.out.println("dataList.size:0");
		}
	}
	/**
	 * 检索索引表所有文档数据，默认分5000条记录一批从es获取数据，分批获取的数据交个一ScrollHandler来处理
	 */
	@Test
	public void testSearchAllHandler(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		ESDatas<Map> esDatas = clientInterface.searchAll("demo", new ScrollHandler<Map>() {
			public void handle(ESDatas<Map> esDatas) throws Exception {
				List<Map> dataList = esDatas.getDatas();
				System.out.println("TotalSize:"+esDatas.getTotalSize());
				if(dataList != null) {
					System.out.println("dataList.size:" + dataList.size());
				}
				else
				{
					System.out.println("dataList.size:0");
				}
			}
		},Map.class);
		List<Map> dataList = esDatas.getDatas();
		System.out.println("TotalSize:"+esDatas.getTotalSize());
		if(dataList != null) {
			System.out.println("dataList.size:" + dataList.size());
		}
		else
		{
			System.out.println("dataList.size:0");
		}
	}
	/**
	 * 检索索引表所有文档数据，按指定的10000条记录一批从es获取数据，分批获取的数据交个一ScrollHandler来处理
	 */
	@Test
	public void testSearchAllFethchSizeHandler(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		ESDatas<Map> esDatas = clientInterface.searchAll("demo",10000,new ScrollHandler<Map>() {
			public void handle(ESDatas<Map> esDatas) throws Exception {
				List<Map> dataList = esDatas.getDatas();
				System.out.println("TotalSize:"+esDatas.getTotalSize());
				if(dataList != null) {
					System.out.println("dataList.size:" + dataList.size());
				}
				else
				{
					System.out.println("dataList.size:0");
				}
			}
		},Map.class);
		List<Map> dataList = esDatas.getDatas();
		System.out.println("TotalSize:"+esDatas.getTotalSize());
		if(dataList != null) {
			System.out.println("dataList.size:" + dataList.size());
		}
		else
		{
			System.out.println("dataList.size:0");
		}
	}
	/**
	 * 并行检索索引表所有文档数据，默认分5000条记录一批从es获取数据，指定了并行的线程数为6
	 */
	@Test
	public void testSearchAllParrrel(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		ESDatas<Map> esDatas = clientInterface.searchAllParallel("demo",Map.class,6);
		List<Map> dataList = esDatas.getDatas();
		System.out.println("TotalSize:"+esDatas.getTotalSize());
		if(dataList != null) {
			System.out.println("dataList.size:" + dataList.size());
		}
		else
		{
			System.out.println("dataList.size:0");
		}
	}
	/**
	 * 并行检索索引表所有文档数据，按指定的10000条记录一批从es获取数据，指定了并行的线程数为6
	 */
	@Test
	public void testSearchAllFethchSizeParrrel(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		ESDatas<Map> esDatas = clientInterface.searchAllParallel("demo",10000,Map.class,6);
		List<Map> dataList = esDatas.getDatas();
		System.out.println("TotalSize:"+esDatas.getTotalSize());
		if(dataList != null) {
			System.out.println("dataList.size:" + dataList.size());
		}
		else
		{
			System.out.println("dataList.size:0");
		}
	}
	/**
	 * 并行检索索引表所有文档数据，默认分5000条记录一批从es获取数据，分批获取的数据交个一ScrollHandler来处理，指定了并行的线程数为6
	 */
	@Test
	public void testSearchAllHandlerParrrel(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		ESDatas<Map> esDatas = clientInterface.searchAllParallel("demo", new ScrollHandler<Map>() {
			public void handle(ESDatas<Map> esDatas) throws Exception {
				List<Map> dataList = esDatas.getDatas();
				System.out.println("TotalSize:"+esDatas.getTotalSize());
				if(dataList != null) {
					System.out.println("dataList.size:" + dataList.size());
				}
				else
				{
					System.out.println("dataList.size:0");
				}
			}
		},Map.class,6);
		List<Map> dataList = esDatas.getDatas();
		System.out.println("TotalSize:"+esDatas.getTotalSize());
		if(dataList != null) {
			System.out.println("dataList.size:" + dataList.size());
		}
		else
		{
			System.out.println("dataList.size:0");
		}
	}
	/**
	 * 并行检索索引表所有文档数据，按指定的10000条记录一批从es获取数据，分批获取的数据交个一ScrollHandler来处理，指定了并行的线程数为6
	 */
	@Test
	public void testSearchAllFethchSizeHandlerParrrel(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		ESDatas<Map> esDatas = clientInterface.searchAllParallel("demo",10000,new ScrollHandler<Map>() {
			public void handle(ESDatas<Map> esDatas) throws Exception {
				List<Map> dataList = esDatas.getDatas();
				System.out.println("TotalSize:"+esDatas.getTotalSize());
				if(dataList != null) {
					System.out.println("dataList.size:" + dataList.size());
				}
				else
				{
					System.out.println("dataList.size:0");
				}
			}
		},Map.class,6);
		List<Map> dataList = esDatas.getDatas();
		System.out.println("TotalSize:"+esDatas.getTotalSize());
		if(dataList != null) {
			System.out.println("dataList.size:" + dataList.size());
		}
		else
		{
			System.out.println("dataList.size:0");
		}
	}
}
