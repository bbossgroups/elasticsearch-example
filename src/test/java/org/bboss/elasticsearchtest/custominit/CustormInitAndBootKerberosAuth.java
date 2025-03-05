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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(CustormInitAndBootKerberosAuth.class);
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
        properties.put("es233.http.kerberos.refreshKrb5Config","false");
        
        properties.put("es233.http.kerberos.storeKey","true");
        properties.put("es233.http.kerberos.doNotPrompt","true");
        properties.put("es233.http.kerberos.isInitiator","true");
        properties.put("es233.http.kerberos.debug","true");
        properties.put("es233.http.kerberos.loginContextName","Krb5Login");
        properties.put("es233.http.kerberos.useSubjectCredsOnly","true");
        

		ElasticSearchBoot.boot(properties);
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil("es233");
		//获取ES版本信息
		String result = clientUtil.executeHttp("/?pretty", ClientInterface.HTTP_GET);
        logger.info(result);
        logger.info(clientUtil.getClusterSettings());
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
        properties.put("es233.http.kerberos.loginContextName","test");
        properties.put("es233.http.kerberos.debug","true");


        ElasticSearchBoot.boot(properties);
        ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil("es233");
        //获取ES版本信息
        String result = clientUtil.executeHttp("/?pretty", ClientInterface.HTTP_GET);
        logger.info(result);
        logger.info(clientUtil.getClusterSettings());
        boolean exist = clientUtil.existIndice("xxxxx");
        
        logger.info(exist+"");

        String doc = clientUtil.getDocument("xxxxx","1");

        logger.info(doc);
    }

    /**
     * 华为云Elasticsearch对接案例
     */
    @Test
    public void testServerrealmKerberos(){
        Map properties = new HashMap();
        /**
         * 配置Elasticsearch数据源参数，这里只设置必须的配置项，更多配置参考文件：
         * https://gitee.com/bboss/elasticsearchdemo/blob/master/src/main/resources/application.properties
         */
        //定义Elasticsearch数据源名称：esDS，后续通过esDS获取对应数据源的客户端API操作和访问Elasticsearch
        properties.put("elasticsearch.serverNames","esDS");
        //es服务器地址和端口，多个用逗号分隔
        properties.put("esDS.elasticsearch.rest.hostNames","192.168.137.1:8200");
        //是否在控制台打印dsl语句，log4j组件日志级别为INFO或者DEBUG
        properties.put("esDS.elasticsearch.showTemplate","true");
        //集群节点自动发现,关闭服务发现机制
        properties.put("esDS.elasticsearch.discoverHost","false");

        //Kerberos安全认证配置--开始
        
        properties.put("esDS.http.kerberos.serverRealmPath","/elasticsearch/serverrealm");//配置华为云Elasticsearch服务端Princpal查询服务地址
        properties.put("esDS.http.kerberos.useSubjectCredsOnly","false");
        //华为云Elasticsearch krb5.conf文件，由华为提供
        properties.put("esDS.http.kerberos.krb5Location","C:/environment/es/8.13.2/elasticsearch-8.13.2/config/krb5.conf");
        //华为云Elasticsearch jaas.conf文件，由华为提供
        properties.put("esDS.http.kerberos.loginConfig","C:/environment/es/8.13.2/elasticsearch-8.13.2/config/jaas.conf");

        //配置登录模块名称，与华为云Elasticsearch jaas.conf文件中的模块名称一致
        properties.put("esDS.http.kerberos.loginContextName","ESClient");
        
        //配置是否debug Kerberos认证详细日志
        properties.put("esDS.http.kerberos.debug","true");

        //Kerberos安全认证配置--结束
        
        //启动和初始化Elasticsearch数据源
        ElasticSearchBoot.boot(properties);
        
        //通过Elasticsearch数据源名称esDS获取对应数据源的客户端API，操作和访问Elasticsearch
        ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil("esDS");
        
        //验证客户端：通过Elasticsearch rest服务获取ES集群信息
        String result = clientInterface.executeHttp("/?pretty", ClientInterface.HTTP_GET);
        logger.info(result);
        
        //验证客户端：通过API获取ES集群配置参数
        logger.info(clientInterface.getClusterSettings());

        //验证客户端：通过API判断索引demo是否存在
        boolean exist = clientInterface.existIndice("demo");

        logger.info(exist+"");
        //验证客户端：通过API从索引demo获取文档id为1的文档数据（String报文）
        String doc = clientInterface.getDocument("demo","1");

        logger.info(doc+"");

        //验证客户端：通过API从索引demo获取文档id为1的文档数据（or mapping示例：返回Map结构的数据，亦可以转换为PO对象）
        Map mapdoc = clientInterface.getDocument("demo","1",Map.class);
        
    }
	
}
