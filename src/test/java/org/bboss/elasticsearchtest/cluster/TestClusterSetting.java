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
import org.junit.Test;

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
		System.out.println(clientInterface.getClusterSetting());
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
		clientInterface.updateNumberOfReplicas("cms_document",0);//直接设置cms_document索引
		System.out.println(clientInterface.getIndiceSetting("cms_document","pretty"));//获取索引cms_document配置

	}
}
