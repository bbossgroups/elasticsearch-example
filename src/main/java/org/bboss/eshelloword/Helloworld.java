package org.bboss.eshelloword;/*
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
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.spi.BaseApplicationContext;

import java.util.Date;
import java.util.Map;

public class Helloworld {
	public static void main(String[] args){
		Date date = new Date(1521788460387L);//1520584740000 1520845980000

		date = new Date(1520930340000L);
		//创建es客户端工具，验证环境
//		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil("logs");
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		//验证环境,获取es状态
//		String response = clientUtil.executeHttp("_cluster/state?pretty",ClientInterface.HTTP_GET);

		//判断索引类型是否存在，false表示不存在，正常返回true表示存在
		boolean exist = clientUtil.existIndiceType("twitter","tweet");

		exist = clientUtil.existIndiceType("demo","demo");

		//判读索引是否存在，false表示不存在，正常返回true表示存在
		exist = clientUtil.existIndice("twitter");

		exist = clientUtil.existIndice("agentinfo");

		exist = clientUtil.existIndice("demo");
		if(exist) {
			long count = clientUtil.countAll("demo");
			System.out.println("count:"+count);
			ESDatas<Map> esDatas = clientUtil.searchAll("demo", Map.class);
		}
		BaseApplicationContext.shutdown();
//		long count = clientUtil.countAll("agentinfo");
//		System.out.println(response);
//		Map<String,Object> state = clientUtil.executeHttp("_cluster/state",ClientInterface.HTTP_GET,
//				new ESMapResponseHandler());//返回map结构
	}

}
