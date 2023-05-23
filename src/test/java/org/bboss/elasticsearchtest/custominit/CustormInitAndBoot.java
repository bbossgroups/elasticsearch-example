package org.bboss.elasticsearchtest.custominit;
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
import org.frameworkset.elasticsearch.boot.ElasticSearchBoot;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/5/30 15:23
 * @author biaoping.yin
 * @version 1.0
 */
public class CustormInitAndBoot {
	/**
	 * 初始化一个Elasticsearch数据源
	 */
	@Test
	public void testSingleESDatasourceBoot(){
		Map properties = new HashMap();
		/**
		 * 这里只设置必须的配置项，其他的属性参考配置文件：resources/application.properties
		 *
		 */
		//认证账号和口令配置，如果启用了安全认证才需要，支持xpack和searchguard
		properties.put("elasticsearch.serverNames","es233");
		properties.put("es233.elasticUser","elastic");
		properties.put("es233.elasticPassword","changeme");
		//es服务器地址和端口，多个用逗号分隔
		properties.put("es233.elasticsearch.rest.hostNames","127.0.0.1:9200");
		//是否在控制台打印dsl语句，log4j组件日志级别为INFO或者DEBUG
		properties.put("es233.elasticsearch.showTemplate","true");
		//集群节点自动发现
		properties.put("es233.elasticsearch.discoverHost","true");
//		properties.put("http.timeoutSocket",60000);
//		properties.put("http.timeoutConnection",40000);
//		properties.put("http.connectionRequestTimeout",70000);

		ElasticSearchBoot.boot(properties);
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil("es233");
		//获取ES版本信息
		String result = clientUtil.executeHttp("/?pretty", ClientInterface.HTTP_GET);
		System.out.println(result);
		System.out.println(clientUtil.getClusterSettings());
	}
	/**
	 * 初始化多个Elasticsearch数据源
	 */
	@Test
	public void testMultiESDatasourceBoot(){
		Map properties = new HashMap();

		/**
		 * 多集群配置样例
		 * 这里只设置必须的配置项，其他的属性参考配置文件：resources/application.properties.multicluster
		 */

		// 注意：多数据源配置时，首先必须声明每个数据源的名称
		// 声明两个es数据源的名称，代码里面通过这个名称指定对应的数据源
		//default为默认的Elasitcsearch数据源名称，es233对应了一个elasticsearch 2.3.3的Elasitcsearch集群数据源
		properties.put("elasticsearch.serverNames","default,es233");

		/**
		 * 默认的default数据源配置，每个配置项可以加default.前缀，也可以不加
		 */
		//认证账号和口令配置，如果启用了安全认证才需要，支持xpack和searchguard
		//properties.put("default.elasticUser","elastic");
		//properties.put("default.elasticPassword","changeme");
		properties.put("default.elasticUser","elastic");
		properties.put("default.elasticPassword","changeme");
		//es服务器地址和端口，多个用逗号分隔
		properties.put("default.elasticsearch.rest.hostNames","127.0.0.1:9200");
		//是否在控制台打印dsl语句，log4j组件日志级别为INFO或者DEBUG
		properties.put("default.elasticsearch.showTemplate","true");
		//集群节点自动发现
		properties.put("default.elasticsearch.discoverHost","true");

		/**
		 * es233数据源配置，每个配置项必须以es233.前缀开始
		 */
		properties.put("es233.elasticUser","elastic");
		properties.put("es233.elasticPassword","changeme");
		//es服务器地址和端口，多个用逗号分隔
		properties.put("es233.elasticsearch.rest.hostNames","127.0.0.1:9201");
		//是否在控制台打印dsl语句，log4j组件日志级别为INFO或者DEBUG
		properties.put("es233.elasticsearch.showTemplate","true");
		//集群节点自动发现
		properties.put("es233.elasticsearch.discoverHost","true");
		ElasticSearchBoot.boot(properties);

		//验证default数据源，构建es233数据源的client api实例
		ClientInterface defaultClientUtil = ElasticSearchHelper.getRestClientUtil("default");
		//获取ES版本信息
		String result = defaultClientUtil.executeHttp("/?pretty", ClientInterface.HTTP_GET);
		System.out.println(result);

		//验证es233数据源，构建es233数据源的client api实例
		ClientInterface es233ClientUtil = ElasticSearchHelper.getRestClientUtil("es233");
		//获取ES版本信息
		result = es233ClientUtil.executeHttp("/?pretty", ClientInterface.HTTP_GET);
		System.out.println(result);
	}
}
