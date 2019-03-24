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
 * <p>Description: </p>
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

		clientInterface.updateClusterSettings(clusterSettingList);
		System.out.println(clientInterface.getClusterSettings(false));
	}
	@Test
	public void updateUnassigned(){
		ClientInterface clientInterface =  ElasticSearchHelper.getRestClientUtil();
		clientInterface.unassignedNodeLeftDelayedTimeout("2d"); //全局设置
		clientInterface.unassignedNodeLeftDelayedTimeout("cms_document","3d");//直接设置cms_document索引
		System.out.println(clientInterface.executeHttp("cms_document/_settings?pretty",ClientInterface.HTTP_GET));//获取索引cms_document配置
		clientInterface.unassignedNodeLeftDelayedTimeout("cms_document","3d");//直接设置
		System.out.println(clientInterface.getIndiceSetting("cms_document","pretty"));//获取索引cms_document配置
	}

	@Test
	public void updateNumberOfReplicas(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();

		clientInterface.updateNumberOfReplicas(1);//全局设置
		clientInterface.updateNumberOfReplicas("cms_document",2);//直接设置cms_document索引
		System.out.println(clientInterface.executeHttp("cms_document/_settings?pretty",ClientInterface.HTTP_GET));//获取索引cms_document配置
		clientInterface.updateNumberOfReplicas("cms_document",3);//直接设置cms_document索引
		System.out.println(clientInterface.getIndiceSetting("cms_document","pretty"));//获取索引cms_document配置

	}

	@Test
	public void testSetting(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		clientInterface.updateIndiceSetting("cms_document","index.unassigned.node_left.delayed_timeout","1d");
		System.out.println(clientInterface.getIndiceSetting("cms_document","pretty"));//获取索引cms_document配置
		clientInterface.updateAllIndicesSetting("index.unassigned.node_left.delayed_timeout","2d");
		System.out.println(clientInterface.getIndiceSetting("cms_document","pretty"));//获取索引cms_document配置
		Map<String,Object> settings = new HashMap<String,Object>();
		settings.put("index.unassigned.node_left.delayed_timeout","5d");
		settings.put("index.number_of_replicas",5);
		clientInterface.updateAllIndicesSettings(settings);
		System.out.println(clientInterface.getIndiceSetting("cms_document","pretty"));//获取索引cms_document配置
		settings.put("index.unassigned.node_left.delayed_timeout","3d");
		settings.put("index.number_of_replicas",6);
		clientInterface.updateIndiceSettings("cms_document",settings);
		System.out.println(clientInterface.getIndiceSetting("cms_document","pretty"));//获取索引cms_document配置

	}
}
