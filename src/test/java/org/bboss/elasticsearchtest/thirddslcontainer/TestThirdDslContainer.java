package org.bboss.elasticsearchtest.thirddslcontainer;
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

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.util.SQLUtil;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.template.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: Example for elasticsearch 5,6</p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2020/1/23 19:52
 * @author biaoping.yin
 * @version 1.0
 */
public class TestThirdDslContainer {
	private static Logger logger = LoggerFactory.getLogger(TestThirdDslContainer.class);
	@Before
	public void init() throws SQLException {
		/**
		 * "org.sqlite.JDBC",
		 * 							"jdbc:sqlite://" + dbpath.getCanonicalPath(),
		 * 							"root", "root",
		 * 							null,//"false",
		 * 							null,// "READ_UNCOMMITTED",
		 * 							"select 1",
		 * 							dbJNDIName,
		 * 							10,
		 * 							10,
		 * 							20,
		 * 							true,
		 * 							false,
		 * 							null, false, false
		 */
		//定义保存dsl的数据源testdslconfig
		SQLUtil.startPool("testdslconfig",//数据源名称
				"org.sqlite.JDBC",
				"jdbc:sqlite:testdslconfig" ,
				"root", "root",
				null,//"false",
				null,// "READ_UNCOMMITTED",
				"select 1",
				"test-jndi",
				10,
				10,
				20,
				true,
				false,
				null, true, false
		);

		//在sqlite数据源testdslconfig中创建保存dsl语句的数据库表dslconfig
		String createStatusTableSQL = new StringBuilder()
				.append("create table dslconfig (ID string,name string,namespace string,dslTemplate TEXT,vtpl number(1),multiparser number(1) ")
				.append(",referenceNamespace string,referenceTemplateName string,PRIMARY KEY (ID))").toString();

		try {
			String exist = "select 1 from dslconfig";
			//SQLExecutor.updateWithDBName("gencode","drop table BBOSS_GENCODE");

			SQLExecutor.queryObjectWithDBName(int.class,"testdslconfig", exist);
			logger.info("重建建dslconfig表："+createStatusTableSQL+"。");
			SQLExecutor.updateWithDBName("testdslconfig","drop table dslconfig");
			SQLExecutor.updateWithDBName("testdslconfig",createStatusTableSQL);
			logger.info("重建建dslconfig表成功。");
		} catch (Exception e) {

			logger.info("dslconfig table 不存在，创建dslconfig表："+createStatusTableSQL+"。");
			try {
				SQLExecutor.updateWithDBName("testdslconfig",createStatusTableSQL);
				logger.info("创建dslconfig表成功："+createStatusTableSQL+"。");
			} catch (SQLException e1) {
				logger.info("创建dslconfig表失败："+createStatusTableSQL+"。",e1);
				e1.printStackTrace();
			}
		}

		//初始化dsl配置：将配置文件中的sql转存到数据库中
		String dslpath = "esmapper/demo.xml";
	    final String namespace = "testnamespace";
		AOPTemplateContainerImpl aopTemplateContainer = new AOPTemplateContainerImpl(dslpath);
		int perKeyDSLStructionCacheSize = aopTemplateContainer.getPerKeyDSLStructionCacheSize();
		boolean alwaysCacheDslStruction = aopTemplateContainer.isAlwaysCacheDslStruction();
		List<TemplateMeta> templateMetaList = aopTemplateContainer.getTemplateMetas(namespace);

		//保存dsl到表dslconfig
		SQLExecutor.insertBeans("testdslconfig",
				"insert into dslconfig(ID,name,namespace,dslTemplate,vtpl,multiparser,referenceNamespace,referenceTemplateName) " +
						"values(#[id],#[name],#[namespace],#[dslTemplate],#[vtpl],#[multiparser],#[referenceNamespace],#[referenceTemplateName])",
				templateMetaList);
	}
	@Test
	public void testThirdDslContainer(){
		//创建一个从数据库加载命名空间为testnamespace的所有dsl语句的ClientInterface组件实例
		ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil(new BaseTemplateContainerImpl("testnamespace") {
			@Override
			protected Map<String, TemplateMeta> loadTemplateMetas(String namespace) {
				try {
					List<BaseTemplateMeta> templateMetas = SQLExecutor.queryListWithDBName(BaseTemplateMeta.class,
																	"testdslconfig","select * from dslconfig where namespace = ?",namespace);
					if(templateMetas == null){
						return null;
					}
					else{
						Map<String,TemplateMeta> templateMetaMap = new HashMap<String, TemplateMeta>(templateMetas.size());
						for(BaseTemplateMeta baseTemplateMeta: templateMetas){
							templateMetaMap.put(baseTemplateMeta.getName(),baseTemplateMeta);
						}
						return templateMetaMap;
					}
				} catch (Exception e) {
					throw new DSLParserException(e);
				}
			}

			@Override
			protected long getLastModifyTime(String namespace) {
				// 获取dsl更新时间戳：模拟每次都更新，返回当前时间戳
				// 如果检测到时间戳有变化，框架就将调用loadTemplateMetas方法加载最新的dsl配置
				return System.currentTimeMillis();
			}
		});
		/**
		 * 使用clientinterface操作elasticsearch
		 */
		try{
			testHighlightSearch(clientInterface);
		}
		catch (Exception e){
			logger.error("",e);
		}
		try{
			testCRUD(clientInterface);
		}
		catch (Exception e){
			logger.error("",e);
		}
		// 脚本和片段引用测试
		testScriptImpl( clientInterface);
	}
	private void testScriptImpl(ClientInterface clientInterface)  {
		ScriptImpl scriptImpl = new ScriptImpl(clientInterface);
		scriptImpl.updateDocumentByScriptPath();
		scriptImpl.updateDocumentByScriptQueryPath();
	}
	private void testHighlightSearch(ClientInterface clientInterface) throws ParseException {
		HighlightSearch highlightSearch = new HighlightSearch(clientInterface);
		highlightSearch.initIndiceAndData();
		highlightSearch.highlightSearch();
		highlightSearch.highlightSearchOther();
	}
	private void testCRUD(ClientInterface clientInterface) throws ParseException {
		DocumentCRUD documentCRUD = new DocumentCRUD(clientInterface);
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
	}
}
