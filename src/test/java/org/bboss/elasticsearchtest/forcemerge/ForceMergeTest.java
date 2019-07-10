package org.bboss.elasticsearchtest.forcemerge;
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
import org.frameworkset.elasticsearch.entity.MergeOption;
import org.junit.Test;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/7/7 16:11
 * @author biaoping.yin
 * @version 1.0
 */
public class ForceMergeTest {
	@Test
	public void testMerge(){
		ClientInterface clientInterface =  ElasticSearchHelper.getRestClientUtil();

		clientInterface.forcemerge();//强制合并所以索引的segment

		MergeOption mergeOption = new MergeOption();
		mergeOption.setFlush(true);
		mergeOption.setMaxnumSegments(1);
		mergeOption.setOnlyExpungeDeletes(false);

		clientInterface.forcemerge(mergeOption );//强制合并所以索引的segment，并指定合并参数选项

		clientInterface.forcemerge("indice1");//强制合并索引indice1的segment

		clientInterface.forcemerge("indice1,indice2");//强制合并索引indice1和indice2的segment

		mergeOption = new MergeOption();
		mergeOption.setFlush(false);
		mergeOption.setMaxnumSegments(1);
		mergeOption.setOnlyExpungeDeletes(false);

		clientInterface.forcemerge("indice1",mergeOption );//强制合并索引indice1的segment，并指定合并参数选项

		clientInterface.forcemerge("indice1,indice2",mergeOption );//强制合并索引indice1和indice2的segment，并指定合并参数选项
	}
}
