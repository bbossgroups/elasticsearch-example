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
import org.junit.Test;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/7/25 22:59
 * @author biaoping.yin
 * @version 1.0
 */
public class AliasTest {
	@Test
	public void testAddAlias(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		String response  = clientInterface.addAlias("demo","demoalias");
		System.out.println(response);
		response = clientInterface.getIndexMapping("demoalias",true);
		System.out.println(response);
		long count  = clientInterface.countAll("demoalias");
		System.out.println(count);
	}

	@Test
	public void testRemoveAlias(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		String response  = clientInterface.removeAlias("demo","demoalias");
		System.out.println(response);
		try {
			response = clientInterface.getIndexMapping("demoalias", true);
			System.out.println(response);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		long count  = clientInterface.countAll("demo");
		System.out.println(count);
	}
}
