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

import org.frameworkset.elasticsearch.client.ConfigHolder;
import org.frameworkset.elasticsearch.template.ConfigDSLUtil;
import org.frameworkset.elasticsearch.template.ESTemplateHelper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/3/15 13:08
 * @author biaoping.yin
 * @version 1.0
 */
public class TestPianduan {
	@Test
	public void testoutPianduan(){
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
		//加载配置文件中的dsl信息，解析dsl语句dynamicInnerDsl
		ConfigHolder configHolder = new ConfigHolder();
		ConfigDSLUtil esUtil = configHolder.getConfigDSLUtil("esmapper/outpianduanref.xml");
		String parseResult = ESTemplateHelper.evalTemplate(esUtil,"testoutPianduan",params);
		//打印解析结果
		System.out.println(parseResult);

	}

	@Test
	public void testinnerPianduan(){
		Map<String,Object> params = new HashMap<String,Object>();
		//设置参数
		params.put("cityId","sss/+\"sss");
		params.put("titleId",new String[]{"5",
				"6",
				"4",
				"9"});
		params.put("deptId1","65");
		params.put("deptId2","65");
		params.put("deptId3","65");
		//设置size，最多返回1000条记录
		params.put("size",1000);
		//加载配置文件中的dsl信息，解析dsl语句dynamicInnerDsl
		ConfigHolder configHolder = new ConfigHolder();
		ConfigDSLUtil esUtil = configHolder.getConfigDSLUtil("esmapper/outpianduanref.xml");
		String parseResult = ESTemplateHelper.evalTemplate(esUtil,"testinnerPianduan",params);
		//打印解析结果
		System.out.println(parseResult);

	}
}
