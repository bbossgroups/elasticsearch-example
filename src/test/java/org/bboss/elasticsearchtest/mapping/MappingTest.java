package org.bboss.elasticsearchtest.mapping;
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
import org.frameworkset.elasticsearch.entity.ESIndice;
import org.junit.Test;

import java.util.List;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/11/13 9:55
 * @author biaoping.yin
 * @version 1.0
 */
public class MappingTest {
	@Test
	public void createMapping(){
		ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/arryaymapping.xml");
		//从配置文件esmapper/arryaymapping.xml中获取mapping结构，并创建hostagentinfo结构
		String result = clientInterface.createIndiceMapping("hostagentinfotest","hostAgentInfoIndice");
		System.out.println(result);
	}

	@Test
	public void deleteMapping(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		//删除mapping结构
		String result = clientInterface.dropIndice("hostagentinfotest");
		System.out.println(result);
	}

	@Test
	public void getMapping(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		//获取hostagentinfo的mapping结构
		String mapping = clientInterface.getIndexMapping("hostagentinfotest");
		System.out.println(mapping);
	}

	@Test
	public void getAllMapping(){
		//获取所有的indice mappings
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		List<ESIndice> mapping = clientInterface.getIndexes();
		System.out.println(mapping);
	}
}
