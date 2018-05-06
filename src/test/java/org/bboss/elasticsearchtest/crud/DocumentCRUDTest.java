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

public class DocumentCRUDTest {

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
}
