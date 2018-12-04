package org.bboss.elasticsearchtest.searchafter;/*
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

import org.apache.commons.collections.map.HashedMap;
import org.bboss.elasticsearchtest.crud.Demo;
import org.bboss.elasticsearchtest.crud.DocumentCRUD;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DocumentSearchAfter {
	/**
	 * 创建索引表并导入searchAfter分页测试数据
	 */
	public void initIndiceAndData(){
		DocumentCRUD documentCRUD = new DocumentCRUD();
		documentCRUD.testCreateIndice();
		documentCRUD.testBulkAddDocuments();
	}
	public void doSeachAfter() throws ParseException {
		//创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/searchafter.xml");
		Map params = new HashedMap();
		params.put("pageSize",100);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		params.put("startTime",dateFormat.parse("2017-09-02 00:00:00").getTime());
		params.put("endTime",new Date().getTime());
		//执行查询，demo为索引表，_search为检索操作action
		ESDatas<Demo> esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
				clientUtil.searchList("demo_*/_search",//demo为索引表，_search为检索操作action:
						// demo/_search
						// demo_*/search
						// demo_1,demo_2/search
						"searchAfterDSL",//esmapper/demo.xml中定义的dsl语句
						params,//变量参数
						Demo.class);//返回的文档封装对象类型
		//获取结果对象列表，最多返回1000条记录
		List<Demo> demos = esDatas.getDatas();
		//获取总记录数
		long totalSize = esDatas.getTotalSize();

		do{
			if(demos != null)
				System.out.println("返回当前页记录数:"+demos.size());
			if(demos != null && demos.size() == 100) { //还有数据，则通过searchAfter继续获取下一页数据
				String searchAfterId =   demos.get(99).getDemoId()+"";//获取最后一条记录的_id值
				params.put("searchAfterId", searchAfterId);//设置searchAfterId为分页起点_id值
				long demoId =  demos.get(99).getDemoId();//获取最后一条记录的demoId值
				params.put("demoId", demoId);//设置searchAfterId为分页起点demoId值
				esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
						clientUtil.searchList("demo/_search",//demo为索引表，_search为检索操作action
								"searchAfterDSL",//esmapper/demo.xml中定义的dsl语句
								params,//变量参数
								Demo.class);//返回的文档封装对象类型

				demos = esDatas.getDatas();

			}
			else{//如果是最后一页，没有数据返回或者获取的记录条数少于100结束分页操作
				break;
			}
		}while(true);
		System.out.println("总记录数:"+totalSize);

	}
}
