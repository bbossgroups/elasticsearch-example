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

	@Test
	public void termAgg(){
		ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/testagg.xml");
		//ESDatas<Map> traces = clientInterface.searchAll("trace-*",1000,Map.class);//获取总记录集合
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
		//一行代码，执行每个服务的访问量总数统计
		ESAggDatas<LongAggHit> response = clientInterface.searchAgg("trace-*/_search",//从trace-开头的索引表中检索数据
																	"termAgg", //配置在esmapper/testagg.xml中的dsl语句
																	params,    //dsl语句termAgg中需要的查询参数
																	LongAggHit.class,  //封装聚合统计中每个服务地址及服务访问量的地址
																	"traces");  //term统计桶的名称，参见dsl语句
		List<LongAggHit> aggHitList = response.getAggDatas();//每个服务的访问量
		long totalSize = response.getTotalSize();//总访问量

	}

	@Test
	public void candicateAgg(){
		ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/testagg.xml");
		Map params = null;//单值聚合统计条件参数
		//一行代码，执行服务基数统计
		//支持的数据封装类有：SingleIntegergAggHit，SingleLongAggHit，SingleDoubleAggHit，SingleFloatAggHit，SingleObjectAggHit
		ESAggDatas<SingleLongAggHit> response = clientInterface.searchAgg("trace-*/_search","candicateAgg",params,SingleLongAggHit.class,"traces");
		SingleLongAggHit aggHitList = response.getSingleAggData();
		long value = aggHitList.getValue();
		long totalSize = response.getTotalSize();//总访问量

	}
}
