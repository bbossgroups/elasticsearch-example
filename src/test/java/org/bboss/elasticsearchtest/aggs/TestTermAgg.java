package org.bboss.elasticsearchtest.aggs;
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
import org.frameworkset.elasticsearch.entity.ESAggDatas;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.entity.LongAggHit;
import org.frameworkset.elasticsearch.entity.SingleLongAggHit;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/1/14 16:13
 * @author biaoping.yin
 * @version 1.0
 */
public class TestTermAgg {
	ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/testagg.xml");
	@Test
	public void termAgg(){


		//一行代码，执行每个服务的访问量总数统计
		ESDatas<Map> traces = clientInterface.searchAll("trace-*",1000,Map.class);
		Map params = new HashMap();//聚合统计条件参数
		params.put("application","testweb");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
		try {
			params.put("startTime",format.parse("1999-01-01 00:00:00").getTime());
			params.put("endTime",new Date().getTime());
			params.put("rpc","/testweb/jsp/logoutredirect.jsp");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ESAggDatas<LongAggHit> response = clientInterface.searchAgg("trace-*/_search","termAgg",params,LongAggHit.class,"traces");
		List<LongAggHit> aggHitList = response.getAggDatas();//每个服务的访问量
		long totalSize = response.getTotalSize();//总访问量

	}

	@Test
	public void candicateAgg(){

		Map params = null;//聚合统计条件参数
		//一行代码，执行每个服务的访问量总数统计
		ESAggDatas<SingleLongAggHit> response = clientInterface.searchAgg("trace-*/_search","candicateAgg",params,SingleLongAggHit.class,"traces");
		SingleLongAggHit aggHitList = response.getSingleAggData();//服务的访问量
		long totalSize = response.getTotalSize();//总访问量

	}
}
