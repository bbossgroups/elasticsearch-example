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
public class CustormInitAndBoot1 {
    private void init(){
        Map properties = new HashMap();
        //认证账号和口令配置，如果启用了安全认证才需要，支持xpack和searchguard
        properties.put("elasticsearch.serverNames","default");
        properties.put("elasticUser","elastic");
        properties.put("elasticPassword","changeme");
        //es服务器地址和端口，多个用逗号分隔
        properties.put("elasticsearch.rest.hostNames","es.iisc.com:9200");
        //是否在控制台打印dsl语句，log4j组件日志级别为INFO或者DEBUG
        properties.put("elasticsearch.showTemplate","true");
        //集群节点自动发现
        properties.put("elasticsearch.discoverHost","false");
        properties.put("http.timeoutSocket","60000");
        properties.put("http.timeoutConnection","40000");
        properties.put("http.connectionRequestTimeout","70000");

        ElasticSearchBoot.boot(properties);
    }
	/**
	 * 初始化一个Elasticsearch数据源
	 */
	@Test
	public void testSingleESDatasourceBoot(){
        init();
        
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ElasticSearchHelper.stopElasticsearch("default");
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                init();
            }
        });
        t.start();
        
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
        while (true) {
            //获取ES版本信息
            try {
                Thread.sleep(1000L);

// 清除DNS缓存
                String result = clientUtil.executeHttp("/?pretty", ClientInterface.HTTP_GET);
                
            }
            catch (Exception e){
                e.printStackTrace();
            }            
        }
       
	}
	 
}
