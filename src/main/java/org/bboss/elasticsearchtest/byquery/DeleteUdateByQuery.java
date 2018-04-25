package org.bboss.elasticsearchtest.byquery;/*
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DeleteUdateByQuery {

	/**
	 * 创建索引表并导入测试数据
	 */
	public void initIndiceAndData(){
		DocumentCRUD documentCRUD = new DocumentCRUD();
		documentCRUD.testCreateIndice();
		documentCRUD.testBulkAddDocument();
	}

	/**
	 * 验证DeleteByQuery功能
	 * @throws ParseException
	 */
	public void deleteByQuery() throws ParseException {
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/byquery.xml");
		//设定DeleteByQuery查询条件,通过map传递变量参数值,key对于dsl中的变量名称
		//dsl中有四个变量
		//        applicationName1
		//        applicationName2
		//        startTime
		//        endTime
		Map<String,Object> params = new HashMap<String,Object>();
		//设置applicationName1和applicationName2两个变量的值
		params.put("applicationName1","blackcatdemo2");
		params.put("applicationName2","blackcatdemo3");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//设置时间范围,时间参数接受long值
		params.put("startTime",dateFormat.parse("2017-09-02 00:00:00").getTime());
		params.put("endTime",new Date().getTime());
		//通过count api先查询数据是否存在
		long totalSize = clientUtil.count("demo","deleteByQuery",params);

		System.out.println("---------------------------------删除前查询，验证数据是否存在:totalSize="+totalSize);
		if(totalSize > 0) {//如果有数据，则通过by query删除之，为了实时查看删除效果，启用了强制刷新机制
			//执行DeleteByQuery操作
			String result = clientUtil.deleteByQuery("demo/_delete_by_query?refresh", "deleteByQuery", params);
			System.out.println(result);

			//删除后再次查询，验证数据是否被删除成功
			totalSize = clientUtil.count("demo","deleteByQuery",params);
			System.out.println("---------------------------------删除后再次查询，验证数据是否被删除:totalSize="+totalSize);
		}

	}


	/**
	 * 验证simpleUpdateByQuery功能
	 */
	public void simpleUpdateByQuery(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		String result = clientUtil.updateByQuery("demo/_update_by_query?conflicts=proceed");
		System.out.println("******************simpleUpdateByQuery result:"+result);
	}

	/**
	 * 验证带查询条件UpdateByQuery功能
	 * @throws ParseException
	 */
	public void updateByQueryWithCondition() throws ParseException {
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/byquery.xml");
		//设定查询条件,通过map传递变量参数值,key对于dsl中的变量名称
		//dsl中有四个变量
		//        applicationName1
		//        applicationName2
		//        startTime
		//        endTime
		Map<String,Object> params = new HashMap<String,Object>();
		//设置applicationName1和applicationName2两个变量的值
		params.put("applicationName1","blackcatdemo2");
		params.put("applicationName2","blackcatdemo3");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//设置时间范围,时间参数接受long值
		params.put("startTime",dateFormat.parse("2017-09-02 00:00:00").getTime());
		params.put("endTime",new Date().getTime());
		String result = clientUtil.updateByQuery("demo/_update_by_query?conflicts=proceed","updateByQuery",params);
		System.out.println("******************updateByQueryWithCondition result:"+result);

	}


}
