package org.bboss.elasticsearchtest.sql;
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
import org.frameworkset.elasticsearch.entity.sql.SQLResult;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/9/17 16:22
 * @author biaoping.yin
 * @version 1.0
 */
public class SQLPagineTest {
	/**
	 * 代码中的sql检索，返回Map类型集合，亦可以返回自定义的对象集合
	 */
	@Test
	public void testMapQuery(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		SQLResult<Map> sqlResult = clientUtil.fetchQuery(Map.class,"{\"query\": \"SELECT * FROM dbclobdemo\",\"fetch_size\": 1}");

		do{
			List<Map> datas = sqlResult.getDatas();
			if(datas == null || datas.size() == 0){
				break;
			}
			else{
				System.out.println(datas.size());//处理数据
				sqlResult = sqlResult.nextPage();//获取下一页数据

			}

		}while(true);



	}
	/**
	 * 配置文件中的sql dsl检索,返回Map类型集合，亦可以返回自定义的对象集合
	 */
	@Test
	public void testMapSQLQueryFromDSL(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/sql.xml");//初始化一个加载sql配置文件的es客户端接口
		//设置sql查询的参数
		Map params = new HashMap();
		params.put("channelId",1);
		params.put("fetchSize",1);
		SQLResult<Map> sqlResult = clientUtil.fetchQuery(Map.class,"sqlPagineQuery",params);

		do{
			List<Map> datas = sqlResult.getDatas();
			if(datas == null || datas.size() == 0){
				break;
			}
			else{
				System.out.println(datas.size());//处理数据
				sqlResult = sqlResult.nextPage();//获取下一页数据

			}

		}while(true);

	}




	/**
	 * 代码中的sql检索，返回Map类型集合，亦可以返回自定义的对象集合
	 */
	@Test
	public void testObjectListQuery(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();

		SQLResult<DocObject> sqlResult = clientUtil.fetchQuery(DocObject.class,"{\"query\": \"SELECT * FROM dbclobdemo\",\"fetch_size\": 1}");

		do{
			List<DocObject> datas = sqlResult.getDatas();
			if(datas == null || datas.size() == 0){
				break;
			}
			else{
				System.out.println(datas.size());//处理数据
				sqlResult = sqlResult.nextPage();//获取下一页数据

			}

		}while(true);


	}


	/**
	 * 配置文件中的sql dsl检索,返回Map类型集合，亦可以返回自定义的对象集合
	 */
	@Test
	public void testObjectSQLQueryFromDSL(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/sql.xml");//初始化一个加载sql配置文件的es客户端接口
		//设置sql查询的参数

		Map params = new HashMap();
		params.put("channelId",1);
		params.put("fetchSize",1);
		SQLResult<DocObject> sqlResult = clientUtil.fetchQuery(DocObject.class,"sqlPagineQuery",params);

		do{
			List<DocObject> datas = sqlResult.getDatas();
			if(datas == null || datas.size() == 0){
				System.out.println(0);//处理数据
				break;
			}
			else{
				System.out.println(datas.size());//处理数据
				sqlResult = sqlResult.nextPage();//获取下一页数据

			}

		}while(true);

	}

	/**
	 * 配置文件中的sql dsl检索,返回Map类型集合，亦可以返回自定义的对象集合
	 */
	@Test
	public void testObjectSQLQueryFromDSL1(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/sql.xml");//初始化一个加载sql配置文件的es客户端接口
		//设置sql查询的参数

		Map params = new HashMap();
		params.put("channelId",1);
		params.put("fetchSize",1);
		SQLResult<DocObject> sqlResult = clientUtil.fetchQuery(DocObject.class,"sqlPagineQuery",params);

		do{
			List<DocObject> datas = sqlResult.getDatas();
			if(datas == null || datas.size() == 0){
				System.out.println(0);//处理数据
				break;
			}
			else{
				System.out.println(datas.size());//处理数据
				sqlResult = clientUtil.fetchQueryByCursor(DocObject.class,sqlResult);//获取下一页数据,通过api获取下一页数据

			}

		}while(true);

	}

	/**
	 * 配置文件中的sql dsl检索,返回Map类型集合，亦可以返回自定义的对象集合
	 * 测试没有返回数据的情况
	 */
	@Test
	public void testNodataSQLQueryFromDSL1(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/sql.xml");//初始化一个加载sql配置文件的es客户端接口
		//设置sql查询的参数

		Map params = new HashMap();
		params.put("channelId",2);
		params.put("fetchSize",1);
		SQLResult<DocObject> sqlResult = clientUtil.fetchQuery(DocObject.class,"sqlPagineQuery",params);


		do{
			List<DocObject> datas = sqlResult.getDatas();
			if(datas == null || datas.size() == 0){
				System.out.println(0);//处理数据
				break;
			}
			else{
				System.out.println(datas.size());//处理数据
				sqlResult = clientUtil.fetchQueryByCursor(DocObject.class,sqlResult);//获取下一页数据,通过api获取下一页数据

			}

		}while(true);

	}

	/**
	 * 配置文件中的sql dsl检索,返回Map类型集合，亦可以返回自定义的对象集合
	 */
	@Test
	public void testObjectSQLQueryFromDSL2(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/sql.xml");//初始化一个加载sql配置文件的es客户端接口
		//设置sql查询的参数

		Map params = new HashMap();
		params.put("channelId",1);
		params.put("fetchSize",1);
		SQLResult<DocObject> sqlResult = clientUtil.fetchQuery(DocObject.class,"sqlPagineQuery",params);

		do{
			List<DocObject> datas = sqlResult.getDatas();
			if(datas == null || datas.size() == 0){
				System.out.println(0);//处理数据
				break;
			}
			else{
				System.out.println(datas.size());//处理数据
				sqlResult = clientUtil.fetchQueryByCursor(DocObject.class,sqlResult.getCursor(),sqlResult.getColumns());//获取下一页数据,通过api获取下一页数据

			}

		}while(true);

	}

	/**
	 * 代码中的sql检索，返回 DocObject类型集合
	 */
	@Test
	public void testCloseCursor(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();

		SQLResult<DocObject> sqlResult = clientUtil.fetchQuery(DocObject.class,"{\"query\": \"SELECT * FROM dbclobdemo\",\"fetch_size\": 1}");
		List<DocObject> datas = sqlResult.getDatas();
		System.out.println(datas.size());//处理数据
		System.out.println(sqlResult.closeCursor());//只处理第一页数据，就主动关闭分页游标
	}

	/**
	 * 代码中的sql检索，返回DocObject类型集合
	 */
	@Test
	public void testCloseCursor1(){
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();

		SQLResult<DocObject> sqlResult = clientUtil.fetchQuery(DocObject.class,"{\"query\": \"SELECT * FROM dbclobdemo\",\"fetch_size\": 1}");
		List<DocObject> datas = sqlResult.getDatas();
		System.out.println(datas.size());//处理数据
		String ret = clientUtil.closeSQLCursor(sqlResult.getCursor());
		System.out.println(ret);//只处理第一页数据，就主动关闭分页游标

	}


}
