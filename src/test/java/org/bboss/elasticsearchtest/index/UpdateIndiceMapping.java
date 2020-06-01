package org.bboss.elasticsearchtest.index;
/**
 * Copyright 2020 bboss
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
 * <p>Copyright (c) 2020</p>
 * @Date 2020/6/1 9:56
 * @author biaoping.yin
 * @version 1.0
 */
public class UpdateIndiceMapping {
	@Test
	public void testUpdateIndiceMapping(){
		ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/demo7.xml");
		if(!clientInterface.isVersionUpper7())

			clientInterface.updateIndiceMapping("/demo/_doc/_mapping","updateDemoIndice");
		else{
			clientInterface.updateIndiceMapping("/demo/_mapping","updateDemoIndice");//无需指定type
		}
	}
}
