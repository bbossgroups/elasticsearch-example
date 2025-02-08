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
public class CustormInitAndBootKerberosAuth {
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
		//es服务器地址和端口，多个用逗号分隔
		properties.put("es233.elasticsearch.rest.hostNames","192.168.137.1:9200");
		//是否在控制台打印dsl语句，log4j组件日志级别为INFO或者DEBUG
		properties.put("es233.elasticsearch.showTemplate","true");
		//集群节点自动发现
		properties.put("es233.elasticsearch.discoverHost","true");

        
//        # kerberos安全认证配置
        properties.put("es233.http.kerberos.principal","elastic/admin@BBOSSGROUPS.COM");
        properties.put("es233.http.kerberos.keytab","C:/environment/es/8.13.2/elasticsearch-8.13.2/config/elastic.keytab");
        properties.put("es233.http.kerberos.krb5Location","C:/environment/es/8.13.2/elasticsearch-8.13.2/config/krb5.conf");
        properties.put("es233.http.kerberos.useTicketCache","false");
        //#http.kerberos.useKeyTab=true
        
        //#Krb5 in GSS API needs to be refreshed so it does not throw the error
        //#Specified version of key is not available
        properties.put("es233.http.kerberos.refreshKrb5Config","true");
        
        properties.put("es233.http.kerberos.storeKey","true");
        properties.put("es233.http.kerberos.doNotPrompt","true");
        properties.put("es233.http.kerberos.isInitiator","true");
        properties.put("es233.http.kerberos.debug","false");
 

		ElasticSearchBoot.boot(properties);
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil("es233");
		//获取ES版本信息
		String result = clientUtil.executeHttp("/?pretty", ClientInterface.HTTP_GET);
		System.out.println(result);
		System.out.println(clientUtil.getClusterSettings());
	}

    @Test
    public void testSingleESDatasourceBootLoginConfig(){
        Map properties = new HashMap();
        /**
         * 这里只设置必须的配置项，其他的属性参考配置文件：resources/application.properties
         *
         */
        //认证账号和口令配置，如果启用了安全认证才需要，支持xpack和searchguard
        properties.put("elasticsearch.serverNames","es233");
        //es服务器地址和端口，多个用逗号分隔
        properties.put("es233.elasticsearch.rest.hostNames","192.168.137.1:9200");
        //是否在控制台打印dsl语句，log4j组件日志级别为INFO或者DEBUG
        properties.put("es233.elasticsearch.showTemplate","true");
        //集群节点自动发现
        properties.put("es233.elasticsearch.discoverHost","true");


//        # kerberos安全认证配置
        properties.put("es233.http.kerberos.krb5Location","C:/environment/es/8.13.2/elasticsearch-8.13.2/config/krb5.conf");
        properties.put("es233.http.kerberos.loginConfig","C:/environment/es/8.13.2/elasticsearch-8.13.2/config/jaas.conf");
        
        properties.put("es233.http.kerberos.debug","true");


        ElasticSearchBoot.boot(properties);
        ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil("es233");
        //获取ES版本信息
        String result = clientUtil.executeHttp("/?pretty", ClientInterface.HTTP_GET);
        System.out.println(result);
        System.out.println(clientUtil.getClusterSettings());
    }
	
}
