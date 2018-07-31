package org.bboss.elasticsearchtest;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/7/31 11:30
 * @author biaoping.yin
 * @version 1.0
 */
public class MatchPhrasePrefixQueryTest {
	ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/matchPhrasePrefixQuery.xml");
	@Test
	public void testMatchPhrasePrefixQueryTest(){

		Map params = new HashMap();
		params.put("queryCondition","bl");
		params.put("maxExpansions",10);
		params.put("from",10);
		params.put("to",20);
		ESDatas<Map> searchResult = clientInterface.searchList("megacorp/_search",//检索的请求地址
														"testMatchPhrasePrefixQueryTest",//对应的dsl配置语句
														 params, //dsl中需要的参数
														 Map.class);//返回结果封装的对象类型，可以为自定义的实体Bean对象类型
		List<Map> datas = searchResult.getDatas();
		long totalSize = searchResult.getTotalSize();

	}
}
