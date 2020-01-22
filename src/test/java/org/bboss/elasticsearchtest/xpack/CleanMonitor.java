package org.bboss.elasticsearchtest.xpack;
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
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/10/24 18:48
 * @author biaoping.yin
 * @version 1.0
 */
public class CleanMonitor {
	@Test
	public void cleanXPaxkMonitor() throws UnsupportedEncodingException {
		ElasticSearchHelper.getRestClientUtil().cleanAllXPackIndices();
		System.out.println(ElasticSearchHelper.getRestClientUtil().getIndice(".kibana_task_manager"));
//		System.out.println(ElasticSearchHelper.getRestClientUtil().executeHttp(java.net.URLEncoder.encode(".kibana_*", "UTF-8") + "?pretty",HTTP_DELETE));
//		ElasticSearchHelper.getRestClientUtil().deleteTempate("demotemplate_1");
//		/.kibana_task_manager/_doc/_search

//		System.out.println(ElasticSearchHelper.getRestClientUtil().searchAll(".kibana_task_manager", MetaMap.class));
	}
}
