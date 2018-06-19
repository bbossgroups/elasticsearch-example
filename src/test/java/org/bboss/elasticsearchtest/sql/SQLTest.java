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

public class SQLTest {
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

	@Test
	public void testTranslate(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		String json = clientUtil.executeHttp("/_xpack/sql/translate",
				"{\"query\": \"SELECT * FROM dbclobdemo\"}",
				ClientInterface.HTTP_POST
		);
		System.out.println(json);


	}
}
