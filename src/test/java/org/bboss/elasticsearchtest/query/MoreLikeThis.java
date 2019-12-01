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

import java.util.List;
import java.util.Map;

/**
 * <p>Description: https://www.elastic.co/guide/en/elasticsearch/reference/7.4/query-dsl-mlt-query.html
 * </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/12/1 11:30
 * @author biaoping.yin
 * @version 1.0
 */
public class MoreLikeThis {
	@Test
	public void testMoreLikeThis(){
		/**
		 * {
		 *     "query": {
		 *         "more_like_this" : {
		 *             "fields" : ["title", "description"],
		 *             "like" : "Once upon a time",
		 *             "min_term_freq" : 1,
		 *             "max_query_terms" : 12
		 *         }
		 *     }
		 * }
		 */
		ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/morelikethis.xml");
		// O/R mapping方式检索，将结果解析为对象类型
		ESDatas<Map> esDatas = clientInterface.searchList("dbdemo/_search","moreLikeThisDsl", Map.class);
		List<Map> datas = esDatas.getDatas();
		long totalSize = esDatas.getTotalSize();
		System.out.println(datas);
		// 原始报文方式检索，直接返回json报文
		String json = clientInterface.executeHttp("dbdemo/_search","moreLikeThisDsl", Map.class,ClientInterface.HTTP_POST);
		System.out.println(json);
	}
}
