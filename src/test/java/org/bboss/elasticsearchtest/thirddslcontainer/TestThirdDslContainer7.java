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
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.runtime.BaseStarter;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: Example for elasticsearch 7</p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2020/1/23 19:52
 * @author biaoping.yin
 * @version 1.0
 */
public class TestThirdDslContainer7 {
	private static Logger logger = LoggerFactory.getLogger(TestThirdDslContainer7.class);
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

		//将配置文件中的sql转存到数据库中
		String dslpath = "esmapper/demo7.xml";
	    final String namespace = "testnamespace7";
		//ioc容器对象ApplicationContext增加start方法，用于根据传入的回调接口Starter运行相关的业务功能,例如：
		final BaseApplicationContext context = DefaultApplicationContext.getApplicationContext(dslpath);
		int perKeyDSLStructionCacheSize = context.getIntProperty(TemplateContainer.NAME_perKeyDSLStructionCacheSize, ESUtil.defaultPerKeyDSLStructionCacheSize);
		boolean alwaysCacheDslStruction = context.getBooleanProperty(TemplateContainer.NAME_alwaysCacheDslStruction, ESUtil.defaultAlwaysCacheDslStruction);
		final List<BaseTemplateMeta> templateMetaList = new ArrayList<BaseTemplateMeta>();
		context.start(new BaseStarter() {
			public void start(Pro pro, BaseApplicationContext ioc) {
				Object _service = ioc.getBeanObject(pro.getName());
				if(_service == null || pro.getName().equals(TemplateContainer.NAME_perKeyDSLStructionCacheSize) || pro.getName().equals(TemplateContainer.NAME_alwaysCacheDslStruction))
					return;
				BaseTemplateMeta baseTemplateMeta = new BaseTemplateMeta();
				baseTemplateMeta.setName(pro.getName());
				baseTemplateMeta.setNamespace(namespace);
				String templateFile = (String)pro.getExtendAttribute(TemplateContainer.NAME_templateFile);
				if(templateFile == null)
				{
					Object o = pro.getObject();
					if(o != null && o instanceof String) {

						String value = (String) o;
						baseTemplateMeta.setDslTemplate(value);
						baseTemplateMeta.setVtpl(pro.getBooleanExtendAttribute(TemplateContainer.NAME_istpl, true));//如果sql语句为velocity模板，则在批处理时是否需要每条记录都需要分析sql语句;
						//标识sql语句是否为velocity模板;
						baseTemplateMeta.setMultiparser(pro.getBooleanExtendAttribute(TemplateContainer.NAME_multiparser, baseTemplateMeta.getVtpl()));
						templateMetaList.add(baseTemplateMeta);
					}
				}
				else
				{
					String templateName = (String)pro.getExtendAttribute(TemplateContainer.NAME_templateName);;
					if(templateName == null)
					{
						logger.warn(new StringBuilder().append("Ignore this DSL template ")
								.append(pro.getName()).append(" in the DSl file ")
								.append(context.getConfigfile())
								.append(" is defined as a reference to the DSL template in another configuration file ")
								.append(templateFile)
								.append(", but the name of the DSL template statement to be referenced is not specified by the templateName attribute, for example:\r\n")
								.append("<property name= \"querySqlTraces\"\r\n")
								.append("templateFile= \"esmapper/estrace/ESTracesMapper.xml\"\r\n")
								.append("templateName= \"queryTracesByCriteria\"/>").toString());
					}
					else
					{
						baseTemplateMeta.setReferenceNamespace(templateFile);
						baseTemplateMeta.setReferenceTemplateName(templateName);
						baseTemplateMeta.setVtpl(false);
						baseTemplateMeta.setMultiparser(false);
						templateMetaList.add(baseTemplateMeta);
					}
				}
			}
		});
		//保存dsl到表dslconfig
		SQLExecutor.insertBeans("testdslconfig",
				"insert into dslconfig(ID,name,namespace,dslTemplate,vtpl,multiparser,referenceNamespace,referenceTemplateName) " +
						"values(#[id],#[name],#[namespace],#[dslTemplate],#[vtpl],#[multiparser],#[referenceNamespace],#[referenceTemplateName])",
				templateMetaList);
	}
	@Test
	public void testThirdDslContainer(){
		//创建一个从数据库加载命名空间为clientInterface7的所有dsl语句的ClientInterface组件实例
		ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil(new BaseTemplateContainerImpl("testnamespace7") {
			@Override
			protected Map<String, TemplateMeta> loadTemplateMetas(String namespace) {
				try {
					List<BaseTemplateMeta> templateMetas = SQLExecutor.queryListWithDBName(BaseTemplateMeta.class,"testdslconfig","select * from dslconfig where namespace = ?",namespace);
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
				// 模拟每次都更新，返回当前时间戳
				return System.currentTimeMillis();
			}
		});
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
		testScriptImpl( clientInterface);
	}

	private void testScriptImpl(ClientInterface clientInterface)  {
		ScriptImpl7 scriptImpl7 = new ScriptImpl7(clientInterface);
		scriptImpl7.updateDocumentByScriptPath();
		scriptImpl7.updateDocumentByScriptQueryPath();
	}
	private void testHighlightSearch(ClientInterface clientInterface) throws ParseException {
		HighlightSearch7 highlightSearch = new HighlightSearch7(clientInterface);
		highlightSearch.initIndiceAndData();
		highlightSearch.highlightSearch();
		highlightSearch.highlightSearchOther();
	}
	private void testCRUD(ClientInterface clientInterface) throws ParseException {
		DocumentCRUD7 documentCRUD = new DocumentCRUD7(clientInterface);
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
