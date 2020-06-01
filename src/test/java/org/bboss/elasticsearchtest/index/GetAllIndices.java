package org.bboss.elasticsearchtest.index;
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
import org.frameworkset.elasticsearch.entity.IndexField;
import org.junit.Test;

import java.util.List;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/3/21 11:57
 * @author biaoping.yin
 * @version 1.0
 */
public class GetAllIndices {
	@Test
	public void testGetAllIndices(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		List<ESIndice> indices = clientInterface.getIndexes();
	}
	@Test
	public void testGetIndice(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		List<IndexField> indexFields = clientInterface.getIndexMappingFields("demo","demo");
		System.out.println(indexFields.size());
	}
	@Test
	public void testIndiceCloseOpen(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		clientInterface.closeIndex("demo");//关闭索引
		List<ESIndice> indices = clientInterface.getIndexes();
		List<IndexField> indexFields = clientInterface.getIndexMappingFields("demo","demo");
		clientInterface.openIndex("demo");//打开索引
		indices = clientInterface.getIndexes();
		clientInterface.openIndex("demo");//打开索引

	}
}
