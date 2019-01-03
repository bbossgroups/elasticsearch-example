package org.bboss.elasticsearchtest.badquery;
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

import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/12/30 20:39
 * @author biaoping.yin
 * @version 1.0
 */
public class TestBadQuery {
	private ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/badquery.xml");

	/**
	 *
	 * @throws SQLException
	 */
	@Test
	public void testBadQuery() throws SQLException
	{
		Map<String,Object> params = new HashMap<String,Object>();


		String result = null;

		for(int i = 0;  i < 6001; i ++){
			//设置applicationName1和applicationName2两个变量的值
			params.put("applicationName1","blackcatdemo"+i);
			params.put("applicationName2","blackcatdemo"+i);
			result = clientInterface.executeRequest("demo/_search","testBadQuery",params);

		}
	}

	/**
	 *
	 * @throws SQLException
	 */
	@Test
	public void testGoodDsl() throws SQLException
	{
		Map<String,Object> params = new HashMap<String,Object>();


		String result = null;

		for(int i = 0;  i < 6001; i ++){
			//设置applicationName1和applicationName2两个变量的值
			params.put("applicationName1","blackcatdemo"+i);
			params.put("applicationName2","blackcatdemo"+i);
			result = clientInterface.executeRequest("demo/_search","testGoodDsl",params);

		}
	}
}
