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
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 目前官方的jdbc驱动还没有放出来，先以rest sql api为例来介绍es 6.3.0的sql功能
 */
public class SQLTest {

	/**
	 * 代码中的sql检索
	 */
	@Test
	public void testQuery(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		String json = clientUtil.executeHttp("/_xpack/sql?format=txt",
				"{\"query\": \"SELECT * FROM dbclobdemo\"}",
				ClientInterface.HTTP_POST
				);
		System.out.println(json);

		json = clientUtil.executeHttp("/_xpack/sql?format=json",
				"{\"query\": \"SELECT * FROM dbclobdemo\"}",
				ClientInterface.HTTP_POST
		);
		System.out.println(json);
	}

	/**
	 * sql转换为dsl
	 */
	@Test
	public void testTranslate(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		String json = clientUtil.executeHttp("/_xpack/sql/translate",
				"{\"query\": \"SELECT * FROM agentinfo\"}",
				ClientInterface.HTTP_POST
		);
		System.out.println(json);

	}

	/**
	 * 配置文件中的sql dsl检索
	 */
	@Test
	public void testSQLQueryFromDSL(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/sql.xml");//初始化一个加载sql配置文件的es客户端接口
		//设置sql查询的参数
		Map params = new HashMap();
		params.put("channelId",1);
		String json = clientUtil.executeHttp("/_xpack/sql","sqlQuery",params,
				ClientInterface.HTTP_POST
		);
		System.out.println(json);

	}
}
