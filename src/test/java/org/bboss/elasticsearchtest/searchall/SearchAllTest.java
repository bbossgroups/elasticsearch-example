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
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/10/14 20:07
 * @author biaoping.yin
 * @version 1.0
 */
public class SearchAllTest {
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
}
