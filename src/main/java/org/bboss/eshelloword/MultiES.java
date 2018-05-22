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

public class MultiES {
	public static void main(String[] args){

		//创建es客户端工具，验证环境
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil("logs");
		ClientInterface configClientUtil = ElasticSearchHelper.getConfigRestClientUtil("logs","esmapper/demo.xml");
		ClientInterface defaultClientUtil = ElasticSearchHelper.getRestClientUtil();
		//验证环境,获取es状态
//		String response = clientUtil.executeHttp("_cluster/state?pretty",ClientInterface.HTTP_GET);

		//判断索引类型是否存在，抛出异常表示不存在，正常返回表示存在
		boolean exist = configClientUtil.existIndiceType("twitter","tweet");
		System.out.println("twitter,tweet exist:"+exist);
		//判读索引是否存在，抛出异常表示不存在，正常返回表示存在
		exist = configClientUtil.existIndice("twitter");
		System.out.println("twitter exist:"+exist);
		exist = configClientUtil.existIndice("agentinfo");
		System.out.println("agentinfo exist:"+exist);
//		System.out.println(response);
//		Map<String,Object> state = clientUtil.executeHttp("_cluster/state",ClientInterface.HTTP_GET,
//				new MapResponseHandler());//返回map结构
	}

}
