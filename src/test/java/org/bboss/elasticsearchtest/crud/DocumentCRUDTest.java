package org.bboss.elasticsearchtest.crud;/*
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
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class DocumentCRUDTest {
	private static Logger logger = LoggerFactory.getLogger(DocumentCRUDTest.class);
	public static void main(String[] args) throws ParseException {
		logger.info("aaaa {},{}",0,null);
		DocumentCRUD documentCRUD = new DocumentCRUD();
		//删除/创建文档索引表
		documentCRUD.testCreateIndice();
		//添加/修改单个文档
		documentCRUD.testAddAndUpdateDocument();
		//批量添加文档
		documentCRUD.testBulkAddDocument();
		//检索文档
		documentCRUD.testSearch();
		//批量修改文档
		documentCRUD.testBulkUpdateDocument();

		//检索批量修改后的文档
		documentCRUD.testSearch();
		//带list复杂参数的文档检索操作
		documentCRUD.testSearchArray();
		//带from/size分页操作的文档检索操作
		documentCRUD.testPagineSearch();
		//带sourcefilter的文档检索操作
		documentCRUD.testSearchSourceFilter();

		documentCRUD.updateDemoIndice();
		documentCRUD.testBulkAddDocuments();
		for(int i = 0; i < 10; i ++){
			documentCRUD.testBulkAddUUIDDocuments(i,10);
		}
//		DocumentCRUDTest documentCRUDTest = new DocumentCRUDTest();
//		documentCRUDTest.testC();

		/**
		DocumentCRUD documentCRUD = new DocumentCRUD();
//		//删除/创建文档索引表
//		documentCRUD.testCreateIndice();
////		//添加/修改单个文档
//		documentCRUD.testAddAndUpdateDocument();
//
//		documentCRUD.testBulkAddDocument();
////		//批量修改文档
		documentCRUD.testBulkAddDocuments();
		for(int i = 0; i < 10; i ++){
			documentCRUD.testBulkAddUUIDDocuments(i,10);
		}*/
//		BaseApplicationContext.shutdown();
		ElasticSearchHelper.stopElasticsearch("default");
		System.out.println();
	}
	@Test
	public void testMetaMap() throws ParseException {
		DocumentCRUD documentCRUD = new DocumentCRUD();
		//删除/创建文档索引表
		documentCRUD.testCreateIndice();
//		//添加/修改单个文档
		documentCRUD.testAddAndUpdateDocument();
		documentCRUD.testMetaMap();
	}
	@Test
	public void testBulkAddDocumentsWithESIndex(){
		DocumentCRUD documentCRUD = new DocumentCRUD();
		documentCRUD.testBulkAddDocumentsWithESIndex();
	}
	@Test
	public void testDirectDslQuery(){
		String queryAll = "{\"query\": {\"match_all\": {}}}";
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		ESDatas<Map> esDatas =clientUtil.searchList("dbdemo/_search",//demo为索引表，_search为检索操作action
				queryAll,//queryAll变量对应的dsl语句
				Map.class);
		//获取结果对象列表，最多返回1000条记录
		List<Map> demos = esDatas.getDatas();
		System.out.println(demos);
		//获取总记录数
		long totalSize = esDatas.getTotalSize();
		System.out.println(totalSize);
	}


	//先完整执行一边，ok
	//现在单步debug功能，整个功能演示完毕
	@Test
	public void testCRUD() throws ParseException {
		DocumentCRUD documentCRUD = new DocumentCRUD();
		//删除/创建文档索引表
		documentCRUD.testCreateIndice();
		//添加/修改单个文档
		documentCRUD.testAddAndUpdateDocument();
		//批量添加文档
		documentCRUD.testBulkAddDocument();
		//检索文档
		documentCRUD.testSearch();
		//批量修改文档
		documentCRUD.testBulkUpdateDocument();

		//检索批量修改后的文档
		documentCRUD.testSearch();
		//带list复杂参数的文档检索操作
		documentCRUD.testSearchArray();
		//带from/size分页操作的文档检索操作
		documentCRUD.testPagineSearch();
		//带sourcefilter的文档检索操作
		documentCRUD.testSearchSourceFilter();

		documentCRUD.updateDemoIndice();
		documentCRUD.testTerm();



	}

	//先完整执行一边，ok
	//现在单步debug功能，整个功能演示完毕
	@Test
	public void testThreadCRUD() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				DocumentCRUD documentCRUD = new DocumentCRUD();
				//删除/创建文档索引表
				documentCRUD.testCreateIndice();
				//添加/修改单个文档
				try {
					documentCRUD.testAddAndUpdateDocument();

					//批量添加文档
					documentCRUD.testBulkAddDocument();
					//检索文档
					documentCRUD.testSearch();
					//批量修改文档
					documentCRUD.testBulkUpdateDocument();

					//检索批量修改后的文档
					documentCRUD.testSearch();
					//带list复杂参数的文档检索操作
					documentCRUD.testSearchArray();
					//带from/size分页操作的文档检索操作
					documentCRUD.testPagineSearch();
					//带sourcefilter的文档检索操作
					documentCRUD.testSearchSourceFilter();

					documentCRUD.updateDemoIndice();
					documentCRUD.testTerm();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();





	}

	//先完整执行一边，ok
	//现在单步debug功能，整个功能演示完毕
	@Test
	public void testSearchWithCustomEscape() throws ParseException {
		DocumentCRUD documentCRUD = new DocumentCRUD();
		//删除/创建文档索引表
		documentCRUD.testCreateIndice();
		//添加/修改单个文档
		documentCRUD.testAddAndUpdateDocument();
		//批量添加文档
		documentCRUD.testBulkAddDocument();
		//检索文档
		documentCRUD.testSearch();
		//批量修改文档
		documentCRUD.testBulkUpdateDocument();



		//特殊字符检索操作
		documentCRUD.testSearchWithCustomEscape();
	}

	@Test
	public void testSearchSourceFilter() throws ParseException {
		DocumentCRUD documentCRUD = new DocumentCRUD();
		//删除/创建文档索引表
		documentCRUD.testCreateIndice();
		//添加/修改单个文档
		documentCRUD.testAddAndUpdateDocument();
		//批量添加文档
		documentCRUD.testBulkAddDocument();
		//不带sourceFilter检索文档
		documentCRUD.testSearch();
		//批量修改文档
		documentCRUD.testBulkUpdateDocument();

		//带sourcefilter的文档检索操作
		documentCRUD.testSearchSourceFilter();
	}


	//测试批量插入和查询的性能
	//现在单步debug功能，整个功能演示完毕
	@Test
	public void testC() throws ParseException {
		DocumentCRUD documentCRUD = new DocumentCRUD();
		//删除/创建文档索引表
		documentCRUD.testCreateIndice();

		//批量添加文档
		documentCRUD.testBulkAddDocuments();




	}
	@Test
	public void testBulkAddDocumentsSetIdField() throws ParseException {
		DocumentCRUD documentCRUD = new DocumentCRUD();
		//删除/创建文档索引表
		documentCRUD.testCreateIndice();

		//批量添加文档
		documentCRUD.testBulkAddDocumentsSetIdField();




	}

	@Test
	public void testBulkAddDocumentsSetIdField1() throws ParseException {
		DocumentCRUD documentCRUD = new DocumentCRUD();
		//删除/创建文档索引表
//		documentCRUD.testCreateIndice();

		//批量添加文档
		documentCRUD.testBulkAddDocument();




	}
}
