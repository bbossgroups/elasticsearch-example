package org.bboss.elasticsearchtest.thirddslcontainer;/*
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

import org.bboss.elasticsearchtest.script.DynamicPriceTemplate;
import org.bboss.elasticsearchtest.script.Rule;
import org.frameworkset.elasticsearch.client.ClientInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptImpl {
	private ClientInterface clientInterface;

	public ScriptImpl(ClientInterface clientInterface) {
		this.clientInterface = clientInterface;
	}
	public void updateDocumentByScriptPath(){
		//初始化数据，会创建type为demo的indice demo，并添加docid为2的文档
		DocumentCRUD documentCRUD = new DocumentCRUD(clientInterface);
		documentCRUD.testCreateIndice();
		documentCRUD.testBulkAddDocument();
		//创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
		Map<String,Object> params = new HashMap<String,Object>();
		DynamicPriceTemplate dynamicPriceTemplate = new DynamicPriceTemplate();
		dynamicPriceTemplate.setGoodsId(1);
		List<Rule> ruleList = new ArrayList<Rule>();
		Rule rule = new Rule();
		rule.setRuleCount(100);
		rule.setRuleExist(true);
		rule.setRuleId("asdfasdfasdf");
		ruleList.add(rule);

		rule = new Rule();
		rule.setRuleCount(101);
		rule.setRuleExist(false);
		rule.setRuleId("bbb$b");
		ruleList.add(rule);

		rule = new Rule();
		rule.setRuleCount(103);
		rule.setRuleExist(true);
		rule.setRuleId("ccccc");
		ruleList.add(rule);
		dynamicPriceTemplate.setRules(ruleList);

		//为id为2的文档增加last和nick两个属性
		params.put("last","gaudreau");
		params.put("nick","hockey");
		params.put("dynamicPriceTemplate",dynamicPriceTemplate);
		//通过script脚本为id为2的文档增加last和nick两个属性，为了演示效果强制refresh，实际环境慎用
		clientInterface.updateByPath("demo/demo/2/_update?refresh","scriptDsl",params);
		//获取更新后的文档，会看到新加的2个字段属性
		String doc = clientInterface.getDocument("demo","demo","2");
		System.out.println(doc);

	}


	public void updateDocumentByScriptQueryPath(){
		//初始化数据，会创建type为demo的indice demo，并添加docid为2的文档
		DocumentCRUD documentCRUD = new DocumentCRUD(clientInterface);
		documentCRUD.testCreateIndice();
		documentCRUD.testBulkAddDocument();
		//创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
		Map<String,Object> params = new HashMap<String,Object>();
		DynamicPriceTemplate dynamicPriceTemplate = new DynamicPriceTemplate();
		dynamicPriceTemplate.setGoodsId(1);
		dynamicPriceTemplate.setGoodName("asd\"国家");
		List<Rule> ruleList = new ArrayList<Rule>();
		Rule rule = new Rule();
		rule.setRuleCount(100);
		rule.setRuleExist(true);
		rule.setRuleId("asdfasd$fasdf");
		ruleList.add(rule);

		rule = new Rule();
		rule.setRuleCount(101);
		rule.setRuleExist(false);
		rule.setRuleId("bbbb");
		ruleList.add(rule);

		rule = new Rule();
		rule.setRuleCount(103);
		rule.setRuleExist(true);
		rule.setRuleId(null);
		ruleList.add(rule);
		dynamicPriceTemplate.setRules(ruleList);


		//为id为2的文档增加last和nick两个属性
		params.put("last","gaudre$au");
		params.put("nick","hockey");
		params.put("id",3);
		params.put("dynamicPriceTemplate",dynamicPriceTemplate);
		//通过script脚本为id为2的文档增加last和nick两个属性，为了演示效果强制refresh，实际环境慎用
		clientInterface.updateByPath("demo/demo/_update_by_query?refresh","scriptDslByQuery",params);
		//获取更新后的文档，会看到新加的2个字段属性
		String doc = clientInterface.getDocument("demo","demo","3");
		System.out.println(doc);

	}


}
