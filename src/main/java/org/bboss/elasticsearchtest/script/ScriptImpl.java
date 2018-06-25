package org.bboss.elasticsearchtest.script;/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import org.bboss.elasticsearchtest.crud.DocumentCRUD;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;

import java.util.HashMap;
import java.util.Map;

public class ScriptImpl {
	private String mappath = "esmapper/demo.xml";

	public void updateDocumentByScriptPath(){
		DocumentCRUD documentCRUD = new DocumentCRUD();
		documentCRUD.testCreateIndice();
		documentCRUD.testBulkAddDocument();
		//创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(mappath);
		Map<String,Object> params = new HashMap<String,Object>();
		//设置applicationName1和applicationName2两个变量的值
		params.put("last","gaudreau");
		params.put("nick","hockey");
		clientUtil.updateByPath("demo/demo/2/_update?refresh","scriptDsl",params);
		String doc = clientUtil.getDocument("demo","demo","2");
		System.out.println(doc);

	}
}
