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

import org.junit.Test;

import java.text.ParseException;

public class DocumentCRUD7Test {
	public static void main(String[] args) throws ParseException {
//		DocumentCRUD7 documentCRUD = new DocumentCRUD7();
//		//删除/创建文档索引表
//		documentCRUD.testCreateIndice();
//		//添加/修改单个文档
//		documentCRUD.testAddAndUpdateDocument();
//		//批量添加文档
//		documentCRUD.testBulkAddDocument();
//		//检索文档
//		documentCRUD.testSearch();
//		//批量修改文档
//		documentCRUD.testBulkUpdateDocument();
//
//		//检索批量修改后的文档
//		documentCRUD.testSearch();
//		//带list复杂参数的文档检索操作
//		documentCRUD.testSearchArray();
//		//带from/size分页操作的文档检索操作
//		documentCRUD.testPagineSearch();
//		//带sourcefilter的文档检索操作
//		documentCRUD.testSearchSourceFilter();
//
//		documentCRUD.updateDemoIndice();
//		DocumentCRUD7Test documentCRUDTest = new DocumentCRUD7Test();
//		documentCRUDTest.testC();

		DocumentCRUD7 documentCRUD = new DocumentCRUD7();
		//删除/创建文档索引表
		documentCRUD.testCreateIndice();
		documentCRUD.getIndiceMapping();
//		//添加/修改单个文档
		documentCRUD.testAddAndUpdateDocument();
		documentCRUD.testBulkAddDocumentWithESIndex();

//		//批量修改文档
		documentCRUD.testBulkAddDocuments();
		documentCRUD.testBulkAddDocumentsWithESIndex();
		documentCRUD.testBulkAddDocumentsWithESIndexUseBatchName();
		//检索文档
		documentCRUD.testSearch();
		//BaseApplicationContext.shutdown();

	}
	@Test
	public void testBulkAddDocument() throws ParseException {
		DocumentCRUD7 documentCRUD = new DocumentCRUD7();

		//批量添加文档
		documentCRUD.testBulkAddDocument();
	}
	@Test
	public void testBulkAddDocumentsWithESIndexUseBatchName() throws ParseException {
		DocumentCRUD7 documentCRUD = new DocumentCRUD7();

		//批量添加文档
		documentCRUD.testBulkAddDocumentsWithESIndexUseBatchName();
	}

	@Test
	public void testAddAndUpdateDocument() throws ParseException {
		DocumentCRUD7 documentCRUD = new DocumentCRUD7();

		documentCRUD.testAddAndUpdateDocument();
	}
	//先完整执行一边，ok
	//现在单步debug功能，整个功能演示完毕
	@Test
	public void testCRUD() throws ParseException {
		DocumentCRUD7 documentCRUD = new DocumentCRUD7();
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
				DocumentCRUD7 documentCRUD = new DocumentCRUD7();
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
		DocumentCRUD7 documentCRUD = new DocumentCRUD7();
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
		DocumentCRUD7 documentCRUD = new DocumentCRUD7();
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
		DocumentCRUD7 documentCRUD = new DocumentCRUD7();
		//删除/创建文档索引表
		documentCRUD.testCreateIndice();

		//批量添加文档
		documentCRUD.testBulkAddDocuments();




	}
	@Test
	public void testBulkAddDocumentsSetIdField() throws ParseException {
		DocumentCRUD7 documentCRUD = new DocumentCRUD7();
		//删除/创建文档索引表
		documentCRUD.testCreateIndice();

		//批量添加文档
		documentCRUD.testBulkAddDocumentsSetIdField();




	}
	@Test
	public void testCreateTempate() throws ParseException {
		DocumentCRUD7 documentCRUD = new DocumentCRUD7();
		documentCRUD.testCreateTempate();
	}
	@Test
	public void updateDemoIndice() throws ParseException {
		DocumentCRUD7 documentCRUD = new DocumentCRUD7();
		documentCRUD.updateDemoIndice();
	}

}
