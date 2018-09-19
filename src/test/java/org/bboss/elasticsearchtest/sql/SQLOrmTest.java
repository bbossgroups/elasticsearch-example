package org.bboss.elasticsearchtest.sql;/*
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
import org.frameworkset.elasticsearch.entity.sql.SQLRestResponse;
import org.frameworkset.elasticsearch.entity.sql.SQLRestResponseHandler;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 目前官方的jdbc驱动还没有放出来，先以rest sql api为例来介绍es 6.3.0的sql功能
 */
public class SQLOrmTest {

	/**
	 * 代码中的sql检索，返回Map类型集合，亦可以返回自定义的对象集合
	 */
	@Test
	public void testDemoQuery(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		List<Map> json = clientUtil.sql(Map.class,"{\"query\": \"SELECT * FROM demo\"}");


		System.out.println(json);
	}

	/**
	 * 代码中的sql检索，返回Map类型集合，亦可以返回自定义的对象集合
	 */
	@Test
	public void testMapQuery(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		List<Map> json = clientUtil.sql(Map.class,"{\"query\": \"SELECT * FROM dbclobdemo\"}");


		System.out.println(json);
	}
	/**
	 * 配置文件中的sql dsl检索,返回Map类型集合，亦可以返回自定义的对象集合
	 */
	@Test
	public void testMapSQLQueryFromDSL(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/sql.xml");//初始化一个加载sql配置文件的es客户端接口
		//设置sql查询的参数
		Map params = new HashMap();
		params.put("channelId",1);
		List<Map> json = clientUtil.sql(Map.class,"sqlQuery",params);
		System.out.println(json);

	}

	/**
	 * 代码中的sql检索，返回Map类型对象，亦可以返回自定义的对象
	 */
	@Test
	public void testMapObjectQuery(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		Map json = clientUtil.sqlObject(Map.class,"{\"query\": \"SELECT * FROM dbclobdemo\"}");


		System.out.println(json);
	}
	/**
	 * 配置文件中的sql dsl检索,返回Map类型对象，亦可以返回自定义的对象
	 */
	@Test
	public void testMapObjectSQLQueryFromDSL(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/sql.xml");//初始化一个加载sql配置文件的es客户端接口
		//设置sql查询的参数
		Map params = new HashMap();
		params.put("channelId",1);
		Map json = clientUtil.sqlObject(Map.class,"sqlQuery",params);
		System.out.println(json);

	}


	/**
	 * 代码中的sql检索，返回Map类型集合，亦可以返回自定义的对象集合
	 */
	@Test
	public void testObjectListQuery(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		List<DocObject> json = clientUtil.sql(DocObject.class,"{\"query\": \"SELECT * FROM dbclobdemo\"}");


		System.out.println(json);
	}
	/**
	 * 配置文件中的sql dsl检索,返回Map类型集合，亦可以返回自定义的对象集合
	 */
	@Test
	public void testObjectSQLQueryFromDSL(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/sql.xml");//初始化一个加载sql配置文件的es客户端接口
		//设置sql查询的参数
		Map params = new HashMap();
		params.put("channelId",1);
		List<DocObject> json = clientUtil.sql(DocObject.class,"sqlQuery",params);
		System.out.println(json);

	}

	/**
	 * 代码中的sql检索，返回Map类型对象，亦可以返回自定义的对象
	 */
	@Test
	public void testObjectQuery(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		DocObject json = clientUtil.sqlObject(DocObject.class,"{\"query\": \"SELECT * FROM dbclobdemo where documentId = 1\"}");
		System.out.println(json);
	}
	/**
	 * 配置文件中的sql dsl检索,返回Map类型对象，亦可以返回自定义的对象
	 */
	@Test
	public void testConditionObjectSQLQueryFromDSL(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/sql.xml");//初始化一个加载sql配置文件的es客户端接口
		//设置sql查询的参数
		Map params = new HashMap();
		params.put("channelId",1);
		DocObject json = clientUtil.sqlObject(DocObject.class,"sqlQuery",params);
		System.out.println(json);

	}
	/**
	 * sql转换为dsl
	 */
	@Test
	public void testTranslate(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		String json = clientUtil.executeHttp("/_xpack/sql/translate",
				"{\"query\": \"SELECT * FROM dbclobdemo limit 5\",\"fetch_size\": 5}",
				ClientInterface.HTTP_POST
		);
		System.out.println(json);

	}

	/**
	 * 低阶的检索方法
	 */
	@Test
	public void testSQLRestResponse(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		SQLRestResponse sqlRestResponse = clientUtil.executeHttp("/_xpack/sql",
																	"{\"query\": \"SELECT * FROM dbclobdemo where documentId = 1\"}",
																	ClientInterface.HTTP_POST,
																		new SQLRestResponseHandler());
		System.out.println(sqlRestResponse);
	}


}
