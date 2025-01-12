package org.bboss.elasticsearchtest.cluster;
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
import org.frameworkset.elasticsearch.entity.ClusterSetting;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description:
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/indices-update-settings.html
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/index-modules.html
 *
 * </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/3/22 11:21
 * @author biaoping.yin
 * @version 1.0
 */
public class TestClusterSetting {
	@Test
	public void testGetClusterSetting(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		System.out.println(clientInterface.getClusterSettings());

	}
	@Test
	public void updateClusterSetting(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		ClusterSetting clusterSetting = new ClusterSetting();
		clusterSetting.setKey("indices.recovery.max_bytes_per_sec");
		clusterSetting.setValue("50mb");
		clusterSetting.setPersistent(true);
		clientInterface.updateClusterSetting(clusterSetting);
		System.out.println(clientInterface.getClusterSettings());
		clusterSetting = new ClusterSetting();
		clusterSetting.setKey("indices.recovery.max_bytes_per_sec");
		clusterSetting.setValue(null);
		clusterSetting.setPersistent(true);
		clientInterface.updateClusterSetting(clusterSetting);
		System.out.println(clientInterface.getClusterSettings());
	}

    @Test
    public void diskthresholdEnabledfalse(){
        ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
        ClusterSetting clusterSetting = new ClusterSetting();
        clusterSetting.setKey("cluster.routing.allocation.disk.threshold_enabled");
        clusterSetting.setValue(false);
        clusterSetting.setPersistent(true);
        clientInterface.updateClusterSetting(clusterSetting);
        System.out.println(clientInterface.getClusterSettings());
        
    }




	@Test
	public void updateClusterSettings(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		List<ClusterSetting> clusterSettingList = new ArrayList<ClusterSetting>();

		ClusterSetting clusterSetting = new ClusterSetting();
		clusterSetting.setKey("indices.recovery.max_bytes_per_sec");
		clusterSetting.setValue("50mb");
		clusterSetting.setPersistent(true);
		clusterSettingList.add(clusterSetting);

		clusterSetting = new ClusterSetting();
		clusterSetting.setKey("xpack.monitoring.collection.enabled");
		clusterSetting.setValue("true");
		clusterSetting.setPersistent(true);
		clusterSettingList.add(clusterSetting);

		clusterSetting = new ClusterSetting();
		clusterSetting.setKey("xpack.monitoring.collection.enabled");
		clusterSetting.setValue("true");
		clusterSetting.setPersistent(false);
		clusterSettingList.add(clusterSetting);

		clusterSetting = new ClusterSetting();
		clusterSetting.setKey("indices.recovery.max_bytes_per_sec");
		clusterSetting.setValue("50mb");
		clusterSetting.setPersistent(false);
		clusterSettingList.add(clusterSetting);

		clientInterface.updateClusterSettings(clusterSettingList);
		System.out.println(clientInterface.getClusterSettings(false));
		clusterSettingList = new ArrayList<ClusterSetting>();

		clusterSetting = new ClusterSetting();
		clusterSetting.setKey("xpack.monitoring.collection.enabled");
		clusterSetting.setValue(null);
		clusterSetting.setPersistent(true);
		clusterSettingList.add(clusterSetting);

		clusterSetting = new ClusterSetting();
		clusterSetting.setKey("indices.recovery.max_bytes_per_sec");
		clusterSetting.setValue(null);
		clusterSetting.setPersistent(false);
		clusterSettingList.add(clusterSetting);

        clusterSetting = new ClusterSetting();
        clusterSetting.setKey("cluster.max_shards_per_node");
        clusterSetting.setValue(20000);
        clusterSetting.setPersistent(true);
        clusterSettingList.add(clusterSetting);

		clientInterface.updateClusterSettings(clusterSettingList);
		System.out.println(clientInterface.getClusterSettings(false));
	}

    @Test
    public void updateClusterSettingsShards(){
        ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
        List<ClusterSetting> clusterSettingList = new ArrayList<ClusterSetting>();

        ClusterSetting clusterSetting = new ClusterSetting();

        clusterSetting.setKey("cluster.max_shards_per_node");
        clusterSetting.setValue(20000);
        clusterSetting.setPersistent(true);
        clusterSettingList.add(clusterSetting);

        clientInterface.updateClusterSettings(clusterSettingList);
        System.out.println(clientInterface.getClusterSettings(false));
    }
	@Test
	public void updateUnassigned(){
		ClientInterface clientInterface =  ElasticSearchHelper.getRestClientUtil();
		clientInterface.unassignedNodeLeftDelayedTimeout("2d"); //全局设置
		clientInterface.unassignedNodeLeftDelayedTimeout("demo","3d");//直接设置demo索引
		System.out.println(clientInterface.executeHttp("demo/_settings?pretty",ClientInterface.HTTP_GET));//获取索引demo配置
		clientInterface.unassignedNodeLeftDelayedTimeout("demo","3d");//直接设置
		System.out.println(clientInterface.getIndiceSetting("demo","pretty"));//获取索引demo配置
	}

	@Test
	public void updateNumberOfReplicas(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();

		clientInterface.updateNumberOfReplicas(1);//全局设置
		clientInterface.updateNumberOfReplicas("demo",2);//直接设置demo索引
		System.out.println(clientInterface.executeHttp("demo/_settings?pretty",ClientInterface.HTTP_GET));//获取索引demo配置
		clientInterface.updateNumberOfReplicas("demo",3);//直接设置demo索引
		System.out.println(clientInterface.getIndiceSetting("demo","pretty"));//获取索引demo配置

	}

	@Test
	public void testSetting(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		clientInterface.updateIndiceSetting("demo","index.unassigned.node_left.delayed_timeout","1d");
		System.out.println(clientInterface.getIndiceSetting("demo","pretty"));//获取索引demo配置
		clientInterface.updateAllIndicesSetting("index.unassigned.node_left.delayed_timeout","2d");
		System.out.println(clientInterface.getIndiceSetting("demo","pretty"));//获取索引demo配置
		Map<String,Object> settings = new HashMap<String,Object>();
		settings.put("index.unassigned.node_left.delayed_timeout","5d");
		settings.put("index.number_of_replicas",5);
		clientInterface.updateAllIndicesSettings(settings);
		System.out.println(clientInterface.getIndiceSetting("demo","pretty"));//获取索引demo配置
		settings.put("index.unassigned.node_left.delayed_timeout","3d");
		settings.put("index.number_of_replicas",6);
		clientInterface.updateIndiceSettings("demo",settings);
		System.out.println(clientInterface.getIndiceSetting("demo","pretty"));//获取索引demo配置
		//获取指定的配置项的值
		System.out.println(clientInterface.getIndiceSettingByName("demo","index.number_of_shards"));
	}

	/**
	 * https://www.elastic.co/guide/en/elasticsearch/reference/6.3/rolling-upgrades.html
	 */
	@Test
	public void enableShared(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
//		System.out.println(clientInterface.flushSynced("demo"));//https://www.elastic.co/guide/en/elasticsearch/reference/6.3/indices-synced-flush.html
		System.out.println(clientInterface.flushSynced());//https://www.elastic.co/guide/en/elasticsearch/reference/6.3/indices-synced-flush.html
		System.out.println(clientInterface.disableClusterRoutingAllocation());//禁用share allocation
		System.out.println(clientInterface.getClusterSettings(false));//获取人工设置的集群配置，看看刚才的修改是否生效
		System.out.println(clientInterface.enableClusterRoutingAllocation());//启用share allocation
		System.out.println(clientInterface.getClusterSettings(false));//获取人工设置的集群配置，看看刚才的修改是否生效

	}

	@Test
	public void disableShared(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
//		System.out.println(clientInterface.flushSynced("demo"));//https://www.elastic.co/guide/en/elasticsearch/reference/6.3/indices-synced-flush.html
		System.out.println(clientInterface.flushSynced());//https://www.elastic.co/guide/en/elasticsearch/reference/6.3/indices-synced-flush.html
		System.out.println(clientInterface.disableClusterRoutingAllocation());//禁用share allocation

		System.out.println(clientInterface.getClusterSettings(false));//获取人工设置的集群配置，看看刚才的修改是否生效

	}
    
    

	@Test
	public void getClusterInfo(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		//获取Elasticsearch集群信息
		System.out.println(clientInterface.getClusterInfo());
		//获取Elasticsearch集群版本号
		System.out.println(clientInterface.getElasticsearchVersion());

		System.out.println(clientInterface.getClusterInfo());

		try {
			System.out.println(clientInterface.getClusterSettings(false));//获取人工设置的集群配置，看看刚才的修改是否生效

		}
		catch (Exception e){
			e.printStackTrace();
		}

		try {
			System.out.println(clientInterface.getClusterSettings(false));//获取人工设置的集群配置，看看刚才的修改是否生效

		}
		catch (Exception e){
			e.printStackTrace();
		}



	}
}
