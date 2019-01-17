package org.bboss.elasticsearchtest.dsl;
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

import org.bboss.elasticsearchtest.crud.Demo;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.template.ESTemplateHelper;
import org.frameworkset.elasticsearch.template.ESUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/1/3 16:10
 * @author biaoping.yin
 * @version 1.0
 */
public class DslTest {
	@Test
	public void dslTest(){

		ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/dsl.xml");
		Map<String,Object> params = new HashMap<String,Object>();
		//设置参数
		params.put("cityId","10002");
		params.put("titleId",new String[]{"5",
				"6",
				"4",
				"9"});
		params.put("deptId1","65");
		params.put("deptId2","65");
		params.put("deptId3","65");
		//设置size，最多返回1000条记录
		params.put("size",1000);

		//执行查询，demo为索引表，_search为检索操作action
		ESDatas<Demo> esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
				clientInterface.searchList("demo/_search",//demo为索引表，_search为检索操作action
						"testGoodDsl",//esmapper/dsl.xml中定义的dsl语句
						params,//变量参数
						Demo.class);//返回的文档封装对象类型Demo
		//获取结果对象列表，最多返回1000条记录
		List<Demo> demos = esDatas.getDatas();

//		String json = clientUtil.executeRequest("demo/_search",//demo为索引表，_search为检索操作action
//				"searchDatas",//esmapper/demo.xml中定义的dsl语句
//				params);

//		String json = com.frameworkset.util.SimpleStringUtil.object2json(demos);
		//获取总记录数
		long totalSize = esDatas.getTotalSize();
		System.out.println(totalSize);
		
	}

	@Test
	public void dynamicInnerDsl(){
		Map conditions = new HashMap<String,Map<String,Object>>();

		Map<String,Object> term = new HashMap<String, Object>();
		term.put("terma","tavalue");
		term.put("termb","tbvalue");
		term.put("termc","tcvalue");
		conditions.put("term",term);

		Map<String,Object> terms = new HashMap<String, Object>();
		terms.put("termsa",new String[]{"tavalue","tavalue1"});
		terms.put("termsb",new String[]{"tbvalue","tbvalue1"});
		terms.put("termsc",new String[]{"tcvalue","tcvalue1"});
		conditions.put("terms",terms);
		ESUtil esUtil = ESUtil.getInstance("esmapper/dsl.xml");

		Map params = new HashMap();
		params.put("conditions",conditions);
		params.put("size",1000);
//		Iterator<Map.Entry<String,Map<String,Object>>> iterable = conditions.entrySet().iterator();
//		Map.Entry<String,Map<String,Object>> entry = iterable.next();
//		entry.getKey()
		System.out.println(ESTemplateHelper.evalTemplate(esUtil,"dynamicInnerDsl",params));

	}
}
