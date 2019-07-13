package org.bboss.elasticsearchtest.query;
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
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.junit.Test;

import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/7/13 12:05
 * @author biaoping.yin
 * @version 1.0
 */
public class TestQuery {

	@Test
	public void testQueryObject(){
		//batchUuid:b13e998a-78c7-48f5-b067-d4b6d0b044a4

		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		Map data  = clientInterface.searchObject("terminalcontent-*/_search?q=batchUuid:b13e998a-78c7-48f5-b067-d4b6d0b044a4&size=1&terminate_after=1",Map.class);
		System.out.println(data);
	}

	@Test
	public void testQueryList(){
		//batchUuid:b13e998a-78c7-48f5-b067-d4b6d0b044a4

		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		ESDatas<Map> data  = clientInterface.searchList("terminalcontent-*/_search?q=requestType:httprequest",Map.class);
		System.out.println(data.getDatas());
		System.out.println(data.getTotalSize());
	}
}
