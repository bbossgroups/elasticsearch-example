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
import org.frameworkset.elasticsearch.boot.ElasticSearchBoot;
import org.frameworkset.elasticsearch.client.ClientInterface;

public class MultiESBooter {
	public static void main(String[] args){

		/**
		 * boot操作必须在所有的ClientInterface组件创建之前调用
		 *  按照默认的配置文件初始化elasticsearch客户端工具
		 * 	conf/elasticsearch.properties,application.properties,config/application.properties
		 */
		//ElasticSearchBoot.boot();

		/**
		 * boot操作必须在所有的ClientInterface组件创建之前调用
		 * 根据指定的配置文件初始化elasticsearch客户端工具
		 * @param configFile 指定1到多个多个ElasticSearch属性配置文件，对应的路径格式为（多个用逗号分隔），例如：
		 * conf/elasticsearch.properties,application.properties,config/application.properties
		 * 上述的文件都是在classpath下面即可，如果需要指定绝对路径，格式为：
		 * file:d:/conf/elasticsearch.properties,file:d:/application.properties,config/application.properties
		 *
		 * 说明：带file:前缀表示后面的路径为绝对路径
		 */
		ElasticSearchBoot.boot("application.properties");


		ElasticSearchBoot.boot("file:/xxx/xxx/application.properties");
		//创建es客户端工具，验证环境

		ClientInterface configClientUtil = ElasticSearchHelper.getConfigRestClientUtil("logs","esmapper/demo.xml");
		ClientInterface defaultClientUtil = ElasticSearchHelper.getRestClientUtil();
		//验证环境,获取es状态
//		String response = clientUtil.executeHttp("_cluster/state?pretty",ClientInterface.HTTP_GET);
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil("logs");
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
