package org.bboss.elasticsearchtest.license;
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
import org.frameworkset.elasticsearch.client.ClientUtil;
import org.junit.Test;

/**
 * <p>Description: import x-pack license</p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/8/18 23:33
 * @author biaoping.yin
 * @version 1.0
 */
public class ImportLicense {
	@Test
	public void testLicense(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/license.xml");

		String ttt = clientUtil.executeHttp("_xpack/license?acknowledge=true","license",ClientUtil.HTTP_PUT);
		System.out.println(ttt);
//		ttt = clientUtil.createTempate("tracesql_template","traceSQLTemplate");
	}
}
