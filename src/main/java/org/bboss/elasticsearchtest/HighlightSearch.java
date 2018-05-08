package org.bboss.elasticsearchtest;/*
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

import org.bboss.elasticsearchtest.crud.Demo;
import org.bboss.elasticsearchtest.crud.DocumentCRUD;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HighlightSearch {
	private String mappath = "esmapper/demo.xml";
	/**
	 * 创建索引表并导入高亮检索功能需要的测试数据
	 */
	public void initIndiceAndData(){
		DocumentCRUD documentCRUD = new DocumentCRUD();
		documentCRUD.testCreateIndice();
		documentCRUD.testBulkAddDocuments();
	}

	/**
	 * 高亮显示测试用例
	 "highlight": {
	 "pre_tags": [
	 "<mark>"
	 ],
	 "post_tags": [
	 "</mark>"
	 ],
	 "fields": {
	 "*": {}
	 },
	 "fragment_size": 2147483647
	 }
	 }
	 */

	public void highlightSearch() throws ParseException {
		//创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(mappath);
		//设定查询条件,通过map传递变量参数值,key对于dsl中的变量名称
		//dsl中有三个变量
		//        condition
		//        startTime
		//        endTime
		Map<String,Object> params = new HashMap<String,Object>();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//设置时间范围,时间参数接受long值
		params.put("startTime",dateFormat.parse("2017-09-02 00:00:00"));
		params.put("endTime",new Date());
		params.put("condition","喜欢唱歌");//全文检索条件，匹配上的记录的字段值对应的匹配内容都会被高亮显示
		//执行查询，demo为索引表，_search为检索操作action
		ESDatas<Demo> esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
				clientUtil.searchList("demo/_search",//demo为索引表，_search为检索操作action
						"testHighlightSearch",//esmapper/demo.xml中定义的dsl语句
						params,//变量参数
						Demo.class);//返回的文档封装对象类型
		//获取总记录数
		long totalSize = esDatas.getTotalSize();
		System.out.println(totalSize);
		//获取结果对象列表，最多返回1000条记录
		List<Demo> demos = esDatas.getDatas();
		for(int i = 0; demos != null && i < demos.size(); i ++){//遍历检索结果列表
			Demo demo = demos.get(i);
			//记录中匹配上检索条件的所有字段的高亮内容
			Map<String,List<Object>> highLights = demo.getHighlight();
			Iterator<Map.Entry<String, List<Object>>> entries = highLights.entrySet().iterator();
			while(entries.hasNext()){
				Map.Entry<String, List<Object>> entry = entries.next();
				String fieldName = entry.getKey();
				System.out.print(fieldName+":");
				List<Object> fieldHighLightSegments = entry.getValue();
				for (Object highLightSegment:fieldHighLightSegments){
					/**
					 * 在dsl中通过<mark></mark>来标识需要高亮显示的内容，然后传到web ui前端的时候，通过为mark元素添加css样式来设置高亮的颜色背景样式
					 * 例如：
					 * <style type="text/css">
					 *     .mark,mark{background-color:#f39c12;padding:.2em}
					 * </style>
					 */
					System.out.println(highLightSegment);
				}
			}
		}

//		String json = clientUtil.executeRequest("demo/_search",//demo为索引表，_search为检索操作action
//				"searchDatas",//esmapper/demo.xml中定义的dsl语句
//				params);

//		String json = com.frameworkset.util.SimpleStringUtil.object2json(demos);

	}

	public void highlightSearchOther() throws ParseException {
		//创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(mappath);
		//设定查询条件,通过map传递变量参数值,key对于dsl中的变量名称
		//dsl中有四个变量
		//        applicationName1
		//        applicationName2
		//        startTime
		//        endTime
		Map<String,Object> params = new HashMap<String,Object>();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//设置时间范围,时间参数接受long值
		params.put("startTime",dateFormat.parse("2017-09-02 00:00:00"));
		params.put("endTime",new Date());
		params.put("condition","不喜欢唱歌");//全文检索条件，匹配上的记录的字段值对应的匹配内容都会被高亮显示
		//执行查询，demo为索引表，_search为检索操作action
		ESDatas<Demo> esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
				clientUtil.searchList("demo/_search",//demo为索引表，_search为检索操作action
						"testHighlightSearch",//esmapper/demo.xml中定义的dsl语句
						params,//变量参数
						Demo.class);//返回的文档封装对象类型
		//获取结果对象列表，最多返回1000条记录
		List<Demo> demos = esDatas.getDatas();
		for(int i = 0; demos != null && i < demos.size(); i ++){//遍历检索结果列表
			Demo demo = demos.get(i);
			//记录中匹配上检索条件的所有字段的高亮内容
			Map<String,List<Object>> highLights = demo.getHighlight();
			Iterator<Map.Entry<String, List<Object>>> entries = highLights.entrySet().iterator();
			while(entries.hasNext()){
				Map.Entry<String, List<Object>> entry = entries.next();
				String fieldName = entry.getKey();
				System.out.print(fieldName+":");
				List<Object> fieldHighLightSegments = entry.getValue();
				for (Object highLightSegment:fieldHighLightSegments){
					/**
					 * 在dsl中通过<mark></mark>来标识需要高亮显示的内容，然后传到web ui前端的时候，通过为mark元素添加css样式来设置高亮的颜色背景样式
					 * 例如：
					 * <style type="text/css">
					 *     .mark,mark{background-color:#f39c12;padding:.2em}
					 * </style>
					 */
					System.out.println(highLightSegment);
				}
			}
		}
//		String json = clientUtil.executeRequest("demo/_search",//demo为索引表，_search为检索操作action
//				"searchDatas",//esmapper/demo.xml中定义的dsl语句
//				params);

//		String json = com.frameworkset.util.SimpleStringUtil.object2json(demos);
		//获取总记录数
		long totalSize = esDatas.getTotalSize();
		System.out.println(totalSize);
	}

}
