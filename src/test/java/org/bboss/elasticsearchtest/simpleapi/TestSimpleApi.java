package org.bboss.elasticsearchtest.simpleapi;
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

import org.frameworkset.elasticsearch.ElasticSearchException;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2020/4/30 17:42
 * @author biaoping.yin
 * @version 1.0
 */
public class TestSimpleApi {
	@Test

	/**
	 * 根据属性获取文档json报文
	 * @param indexName
	 * @param fieldName
	 * @param blackcatdemo2
	 * @return
	 * @throws ElasticSearchException
	 */
	public void getDocumentByField() {
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		String document = clientInterface.getDocumentByField("demo","applicationName.keyword","blackcatdemo2");
	}
	@Test
	public void getDocumentByField1() {
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		Map<String,Object> options = new HashMap<String, Object>();
		String document = clientInterface.getDocumentByField("demo","applicationName.keyword","blackcatdemo2",options);
	}

	/**
	 * 根据属性获取文档json报文
	 * @return
	 * @throws ElasticSearchException
	 */
	@Test
	public void getDocumentByFieldLike3() {
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		String document = clientInterface.getDocumentByFieldLike("demo","applicationName.keyword","blackcatdemo2");
	}

	@Test
	public void getDocumentByFieldLike1(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		Map<String,Object> options = new HashMap<String, Object>();
		String document = clientInterface.getDocumentByFieldLike("demo","applicationName.keyword","blackcatdemo2",options);
	}
	@Test
	public void getDocumentByField2() throws ElasticSearchException{
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		Map document = clientInterface.getDocumentByField("demo","applicationName.keyword","blackcatdemo2",Map.class);
	}
	/**
	 * 根据属性获取type类型文档对象

	 * @return
	 * @throws ElasticSearchException
	 */
	@Test
	public void getDocumentByField3() {
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		Map<String,Object> options = new HashMap<String, Object>();
		Map document = clientInterface.getDocumentByField("demo","applicationName.keyword","blackcatdemo2",Map.class,options);
	}

	@Test
	public void getDocumentByFieldLike() {
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		Map document = clientInterface.getDocumentByFieldLike("demo","applicationName.keyword","blackcatdemo2",Map.class,null);
	}

	/**
	 * 根据属性获取type类型文档对象

	 * @return
	 * @throws ElasticSearchException
	 */
	@Test
	public void getDocumentByFieldLike2() {
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		Map<String,Object> options = new HashMap<String, Object>();
		Map document = clientInterface.getDocumentByFieldLike("demo","applicationName.keyword","blackcatdemo2",Map.class,null);
	}




	@Test
	public void searchListByField() {
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		ESDatas<Map> documents = clientInterface.searchListByField("demo","applicationName.keyword","blackcatdemo2",Map.class,0,10);
	}

	/**
	 * 根据属性获取type类型文档对象

	 * @return
	 * @throws ElasticSearchException
	 */
	@Test
	public void searchListByField1() {
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		Map<String,Object> options = new HashMap<String, Object>();
		ESDatas<Map> documents = clientInterface.searchListByField("demo","applicationName.keyword","blackcatdemo2",Map.class,0,10,options);
	}


	@Test
	public void searchListByFieldLike1() {
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		ESDatas<Map> documents = clientInterface.searchListByFieldLike("demo","applicationName.keyword","blackcatdemo2",Map.class,0,10);
	}

	/**
	 * 根据属性获取type类型文档对象

	 * @return
	 * @throws ElasticSearchException
	 */
	@Test
	public void searchListByFieldLike() {
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		Map<String,Object> options = new HashMap<String, Object>();
		ESDatas<Map> documents = clientInterface.searchListByFieldLike("demo","applicationName.keyword","blackcatdemo2",Map.class,0,10,options);
	}




}
