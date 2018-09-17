package org.bboss.elasticsearchtest.crud;/*
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
import org.frameworkset.elasticsearch.ElasticSearchException;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.client.ClientUtil;
import org.frameworkset.elasticsearch.entity.ESDatas;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DocumentCRUD {
	private String mappath = "esmapper/demo.xml";
	public void testCreateIndice(){
		//创建加载配置文件的客户端工具，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(mappath);
		try {
			//判读索引表demo是否存在，存在返回true，不存在返回false
			boolean exist = clientUtil.existIndice("demo");

			//如果索引表demo已经存在先删除mapping
			if(exist) {
				String r = clientUtil.dropIndice("demo");
				System.out.println(r);
				exist = clientUtil.existIndice("demo");
//				r = clientUtil.dropIndice("demo");
//				System.out.println(r);
				String demoIndice = clientUtil.getIndice("demo");//获取最新建立的索引表结构
				System.out.println(demoIndice);
			}
			//创建索引表demo
			clientUtil.createIndiceMapping("demo",//索引表名称
					"createDemoIndice");//索引表mapping dsl脚本名称，在esmapper/demo.xml中定义createDemoIndice

			String demoIndice = clientUtil.getIndice("demo");//获取最新建立的索引表结构
			System.out.println(demoIndice);
		} catch (ElasticSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateDemoIndice(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(mappath);
		//修改索引表demo中type为demo的mapping结构，增加email字段，对应的dsl片段updateDemoIndice定义在esmapper/demo.xml文件中
		String response = clientUtil.executeHttp("demo/_mapping/demo","updateDemoIndice",ClientUtil.HTTP_PUT);
		System.out.println(response);
		//获取修改后的索引mapping结构
		String mapping = clientUtil.getIndice("demo");
		System.out.println(mapping);
	}
	public void testCreateTempate() throws ParseException{

		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(mappath);
		//创建模板
		String response = clientUtil.createTempate("demotemplate_1",//模板名称
				"demoTemplate");//模板对应的脚本名称，在esmapper/demo.xml中配置
		System.out.println("createTempate-------------------------");
		System.out.println(response);
		//获取模板
		/**
		 * 指定模板
		 * /_template/demoTemplate_1
		 * /_template/demoTemplate*
		 * 所有模板 /_template
		 *
		 */
		String template = clientUtil.executeHttp("/_template/demotemplate_1",ClientUtil.HTTP_GET);
		System.out.println("HTTP_GET-------------------------");
		System.out.println(template);

	}
	public void testAddAndUpdateDocument() throws ParseException {
		//创建创建/修改/获取/删除文档的客户端对象，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		//构建一个对象，日期类型，字符串类型属性演示
		Demo demo = new Demo();
		demo.setDemoId(2l);//文档id，唯一标识，@PrimaryKey注解标示,如果demoId已经存在做修改操作，否则做添加文档操作
		demo.setAgentStarttime(new Date());
		demo.setApplicationName("blackcatdemo2");
		demo.setContentbody("this is content body2");
		demo.setName("刘德华\"\n\t\r");


		//向固定index demo添加或者修改文档,如果demoId已经存在做修改操作，否则做添加文档操作，返回处理结果
		String response = clientUtil.addDocument("demo",//索引表
				"demo",//索引类型
				demo);
//		//强制刷新
//		String response = clientUtil.addDocument("demo",//索引表
//				"demo",//索引类型
//				demo,"refresh=true");


		//向动态index demo-yyyy.MM.dd这种添加或者修改文档,如果demoId已经存在做修改操作，否则做添加文档操作，返回处理结果
		//elasticsearch.dateFormat=yyyy.MM.dd 按照日期生成动态index名称，例如：
		// 到月 elasticsearch.dateFormat=yyyy.MM demo-2018.03
		// 到天 elasticsearch.dateFormat=yyyy.MM.dd demo-2018.03.14
		// 到小时 elasticsearch.dateFormat=yyyy.MM.dd.HH demo-2018.03.14.11
		// 到分钟 elasticsearch.dateFormat=yyyy.MM.dd.HH.mm demo-2018.03.14.11.18
//		String response = clientUtil.addDateDocument("demo",//索引表
//				"demo",//索引类型
//				demo);

		System.out.println("打印结果：addDocument-------------------------");
		System.out.println(response);
		//根据文档id获取文档对象，返回json报文字符串
		response = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"2");//w

		System.out.println("打印结果：getDocument-------------------------");
		System.out.println(response);
		//根据文档id获取文档对象，返回Demo对象
		demo = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"2",//文档id
				Demo.class);

		//update文档
		demo = new Demo();
		demo.setDemoId(2l);//文档id，唯一标识，@PrimaryKey注解标示,如果demoId已经存在做修改操作，否则做添加文档操作
		demo.setAgentStarttime(new Date());
		demo.setApplicationName("blackcatdemo2");
		demo.setContentbody("this is modify content body2");
		demo.setName("刘德华modify\t");

		//执行update操作
		response = clientUtil.addDocument("demo",//索引表
				"demo",//索引类型
				demo);
		//根据文档id获取修改后的文档对象，返回json报文字符串
		response = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"2");//文档id
		System.out.println("打印修改后的结果：getDocument-------------------------");
		System.out.println(response);

//		//删除文档
//		clientUtil.deleteDocument("demo",//索引表
//				"demo",//索引类型
//				"2");//文档id

		//根据文档id获取修改后的文档对象，返回json报文字符串
		response = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"2");//文档id
		System.out.println("打印修改后的结果：getDocument-------------------------");
		System.out.println(response);
		//批量删除文档
		clientUtil.deleteDocuments("demo",//索引表
				"demo",//索引类型
				"2","3");//批量删除文档ids

	}


	public void testBulkAddDocument() {
		//创建批量创建文档的客户端对象，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		List<Demo> demos = new ArrayList<Demo>();
		Demo demo = new Demo();//定义第一个对象
		demo.setDemoId(2l);
		demo.setAgentStarttime(new Date());
		demo.setApplicationName("blackcatdemo2");
		demo.setContentbody("this is content body2");
		demo.setName("刘德\"华\t");
		DynamicPriceTemplate dynamicPriceTemplate = new DynamicPriceTemplate();
		dynamicPriceTemplate.setGoodsId(2);
		List<Rule> ruleList = new ArrayList<Rule>();
		Rule rule = new Rule();
		rule.setRuleCount(100);
		rule.setRuleExist(true);
		rule.setRuleId("asdfasdfasdf");
		ruleList.add(rule);

		rule = new Rule();
		rule.setRuleCount(101);
		rule.setRuleExist(false);
		rule.setRuleId("bbbb");
		ruleList.add(rule);

		rule = new Rule();
		rule.setRuleCount(103);
		rule.setRuleExist(true);
		rule.setRuleId("ccccc");
		ruleList.add(rule);
		dynamicPriceTemplate.setRules(ruleList);

		demo.setDynamicPriceTemplate(dynamicPriceTemplate);

		demos.add(demo);//添加第一个对象到list中



		demo = new Demo();//定义第二个对象
		demo.setDemoId(3l);
		demo.setAgentStarttime(new Date());
		demo.setApplicationName("blackcatdemo3");
		demo.setContentbody("四大\"天王，这种文化很好，中华人民共和国");
		demo.setName("张学友\t\n\r");
		demos.add(demo);//添加第二个对象到list中
		demo.setDynamicPriceTemplate(new HashMap());
//		dynamicPriceTemplate.setGoodsId(3);
//		dynamicPriceTemplate.setRules(new ArrayList<Rule>());
		//批量添加或者修改文档，将两个对象添加到索引表demo中
		String response = clientUtil.addDocuments("demo",//索引表
				"demo",//索引类型
				demos,"refresh");//为了测试效果,启用强制刷新机制

		System.out.println("addDocument-------------------------");
		System.out.println(response);
		//获取第一个文档
		response = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"2");//w
		System.out.println("getDocument-------------------------");
		System.out.println(response);
		//获取第二个文档
		response = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"3"//文档id
				 );
		System.out.println(response);
		//获取第二个文档
		NewDemo newDemo = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"3",//文档id
				NewDemo.class
		);
		//获取不存在的文档
		response = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"-3"//文档id
		);
		System.out.println(response);

		//获取不存在的文档
		Demo demo1 = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"-3"//文档id
		,Demo.class);

		System.out.println(demo1);

		//删除不存在的文档
		response = clientUtil.deleteDocument("demo",//索引表
				"demo",//索引类型
				"-3"//文档id
		);
		System.out.println(response);
		//更新不存在的文档
		response = clientUtil.updateDocument("demo",//索引表
				"demo",//索引类型
				"-3",//文档id
				demo
		);
		System.out.println(response);
	}
	public void updateDocumentByScriptQuery(){
		//创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(mappath);
		clientUtil.updateByQuery("demo/demo/2/_update","scriptDsl");
		Map<String,Object> params = new HashMap<String,Object>();
		//设置applicationName1和applicationName2两个变量的值
		params.put("applicationName1","blackcatdemo2");
		params.put("applicationName2","blackcatdemo3");
	}

	/**
	 * 批量导入20002条数据
	 */
	public void testBulkAddDocuments() {
		//创建批量创建文档的客户端对象，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		List<Demo> demos = new ArrayList<Demo>();
		Demo demo = null;
		long start = System.currentTimeMillis();
		for(int i = 0 ; i < 20002; i ++) {
			demo = new Demo();//定义第一个对象
			demo.setDemoId((long)i);
			demo.setAgentStarttime(new Date());
			demo.setApplicationName("blackcatdemo"+i);
			demo.setContentbody("this is content body"+i);
			if(i % 2 == 0) {
				demo.setName("刘德华喜欢唱歌" + i);
			}
			else{
				demo.setName("张学友不喜欢唱歌" + i);
			}
			demos.add(demo);//添加第一个对象到list中
		}


		//批量添加或者修改文档，将两个对象添加到索引表demo中
		String response = clientUtil.addDocuments("demo",//索引表
				"demo",//索引类型
				demos,"refresh=true");//为了测试效果,启用强制刷新机制
		long end = System.currentTimeMillis();
		System.out.println("BulkAdd 20002 Documents elapsed:"+(end - start)+"毫秒");

		long count = clientUtil.countAll("demo");

		System.out.println("addDocuments-------------------------" +count);
		System.out.println(response);
		//获取第一个文档
		response = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"2");//w
		System.out.println("getDocument-------------------------");
		System.out.println(response);
		//获取第二个文档
		demo = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"3",//文档id
				Demo.class);
	}

	/**
	 * 批量修改文档，和批量添加文档操作一样，只是定义的文档对象标识都是刚才批量添加的文档
	 * @throws ParseException
	 */
	public void testBulkUpdateDocument() {
		//创建批量修改文档的客户端对象，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
//		List<ESIndice> indices = clientUtil.getIndexes();
//		clientUtil.getIndexMappingFields(indice,type);
		List<Demo> demos = new ArrayList<Demo>();
		Demo demo = new Demo();
		demo.setDemoId(2l);
		demo.setAgentStarttime(new Date());
		demo.setApplicationName("blackcatdemo2");
		demo.setContentbody("bulk update content body2");
		demo.setName("刘\n德华bulk update ");
		demos.add(demo);

		demo = new Demo();
		demo.setDemoId(3l);
		demo.setAgentStarttime(new Date());
		demo.setApplicationName("blackcatdemo3");
		demo.setContentbody("bulk update 四大天王，这种文化很好，中华人民共和国");
		demo.setName("张学友bulk update ");
		demos.add(demo);

		//批量修改文档
		String response = clientUtil.addDocuments("demo",//索引表
				"demo",//索引类型
				demos);

		System.out.println("addDateDocument-------------------------");
		System.out.println(response);
		//根据文档id获取修改后的文档json串
		response = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"2");//w
		System.out.println("getDocument-------------------------");
		System.out.println(response);
		//根据文档id获取修改后的文档对象
		demo = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"3",//文档id
				Demo.class);
	}


	/**
	 * 检索文档
	 * @throws ParseException
	 */
	public void testSearch() throws ParseException {
		//创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(mappath);
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
		params.put("startTime",dateFormat.parse("2017-09-02 00:00:00"));
		params.put("endTime",new Date());
		//执行查询，demo为索引表，_search为检索操作action
		ESDatas<Demo> esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
				clientUtil.searchList("demo/_search",//demo为索引表，_search为检索操作action
				"searchDatas",//esmapper/demo.xml中定义的dsl语句
				params,//变量参数
				Demo.class);//返回的文档封装对象类型
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

	/**
	 * 检索文档
	 * @throws ParseException
	 */
	public void testSearchWithCustomEscape() throws ParseException {
		//创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(mappath);
		//设定查询条件,通过map传递变量参数值,key对于dsl中的变量名称
		//dsl中有四个变量
		//        applicationName1
		//        applicationName2
		//        startTime
		//        endTime
		Map<String,Object> params = new HashMap<String,Object>();
		//设置applicationName1和applicationName2两个变量的值
		params.put("applicationName1","blackca\"tdemo2");
		params.put("applicationName2","blackcat\"demo3");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//设置时间范围,时间参数接受long值
		params.put("startTime",dateFormat.parse("2017-09-02 00:00:00"));
		params.put("endTime",new Date());
		//执行查询，demo为索引表，_search为检索操作action
		ESDatas<Demo> esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
				clientUtil.searchList("demo/_search",//demo为索引表，_search为检索操作action
						"searchWithCustomEscape",//esmapper/demo.xml中定义的dsl语句
						params,//变量参数
						Demo.class);//返回的文档封装对象类型
		//获取结果对象列表，最多返回1000条记录
		List<Demo> demos = esDatas.getDatas();

//		String json = clientUtil.executeRequest("demo/_search",//demo为索引表，_search为检索操作action
//				"searchDatas",//esmapper/demo.xml中定义的dsl语句
//				params);

//		String json = com.frameworkset.util.SimpleStringUtil.object2json(demos);
		//获取总记录数
		long totalSize = esDatas.getTotalSize();
		System.out.println(totalSize);

		try {
			//执行查询，demo为索引表，_search为检索操作action
			esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
					clientUtil.searchList("demo/_search",//demo为索引表，_search为检索操作action
							"searchWithCustomEscapeWithError",//esmapper/demo.xml中定义的dsl语句
							params,//变量参数
							Demo.class);//返回的文档封装对象类型
			//获取结果对象列表，最多返回1000条记录
			demos = esDatas.getDatas();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * SourceFilter检索文档
	 * @throws ParseException
	 */
	public void testSearchSourceFilter() throws ParseException {
		//创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/demo.xml");
		//设定查询条件,通过map传递变量参数值,key对于dsl中的变量名称
		//dsl中有四个变量
		//        applicationName1
		//        applicationName2
		//        startTime
		//        endTime
		Map<String,Object> params = new HashMap<String,Object>();
		//设置applicationName1和applicationName2两个变量的值，将多个应用名称放到list中，通过list动态传递参数
		List<String> datas = new ArrayList<String>();
		datas.add("blackcatdemo2");
		datas.add("blackcatdemo3");
		params.put("applicationNames",datas);

		List<String> includes = new ArrayList<String>(); //定义要返回的source字段
		includes.add("agentStarttime");
		includes.add("applicationName");
		params.put("includes",includes);

		List<String> excludes = new ArrayList<String>(); //定义不需要返回的source字段
		excludes.add("contentbody");
		excludes.add("demoId");
		params.put("excludes",excludes);


		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//设置时间范围,时间参数接受long值或者日期类型
		//说明： 也可以接受日期类型，如果传入Date类型的时间并且通过map传参，则需要手动进行日期格式转换成字符串格式的日期串，通过entity传参则不需要
		params.put("startTime",dateFormat.parse("2017-09-02 00:00:00") );
		params.put("endTime",new Date() );
		params.put("pageSize",1000);//设置每页返回的记录条数

		//执行查询，demo为索引表，_search为检索操作action
		ESDatas<Demo> esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
				clientUtil.searchList("demo/_search",//demo为索引表，_search为检索操作action
						"searchSourceFilter",//esmapper/demo.xml中定义的dsl语句
						params,//变量参数
						Demo.class);//返回的文档封装对象类型
		//获取总记录数
		long totalSize = esDatas.getTotalSize();

		//获取结果对象列表，最多返回1000条记录
		List<Demo> demos = esDatas.getDatas();

		System.out.println(totalSize);

		//以下是返回原始检索json报文检索代码
//		String json = clientUtil.executeRequest("demo/_search",//demo为索引表，_search为检索操作action
//				"searchSourceFilter",//esmapper/demo.xml中定义的dsl语句
//				params);

//		String json = com.frameworkset.util.SimpleStringUtil.object2json(demos);

	}


	/**
	 * 分页检索文档
	 * @throws ParseException
	 */
	public void testPagineSearch() throws ParseException {
		//创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(mappath);
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
		params.put("startTime",dateFormat.parse("2017-09-02 00:00:00"));
		params.put("endTime",new Date());

		ESDatas<Demo> esDatas =  null;//返回的文档封装对象类型
		//保存总记录数
		long totalSize = 0;
		//保存每页结果对象列表，最多返回1000条记录
		List<Demo> demos = null;
		int i = 0; //页码
		do{//遍历获取每页的记录
			//设置分页参数
			params.put("from",i * 1000);//分页起点
			params.put("size",1000);//每页返回1000条
			i ++;//往前加页码
			//执行查询，demo为索引表，_search为检索操作action
			 esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
					clientUtil.searchList("demo/_search",//demo为索引表，_search为检索操作action
							"searchPagineDatas",//esmapper/demo.xml中定义的dsl语句
							params,//变量参数
							Demo.class);//返回的文档封装对象类型
			demos = esDatas.getDatas();//每页结果对象列表，最多返回1000条记录
			totalSize = esDatas.getTotalSize();//总记录数
			if(i * 1000 > totalSize)
				break;
		}while(true);

//		String json = clientUtil.executeRequest("demo/_search",//demo为索引表，_search为检索操作action
//				"searchDatas",//esmapper/demo.xml中定义的dsl语句
//				params);

//		String json = com.frameworkset.util.SimpleStringUtil.object2json(demos);

		System.out.println(totalSize);
	}

	/**
	 * 检索文档
	 * @throws ParseException
	 */
	public void testSearchArray() throws ParseException {
		//创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(mappath);

		//设定查询条件,通过map传递变量参数值,key对于dsl中的变量名称
		//dsl中有四个变量
		//        applicationName1
		//        applicationName2
		//        startTime
		//        endTime
		Map<String,Object> params = new HashMap<String,Object>();
		//设置applicationName1和applicationName2两个变量的值
		List<String> datas = new ArrayList<String>();
		datas.add("blackcatdemo2");
		datas.add("blackcatdemo3");
		params.put("applicationNames",datas);
//		params.put("applicationName2","blackcatdemo3");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//设置时间范围,时间参数接受long值
		params.put("startTime",dateFormat.parse("2017-09-02 00:00:00"));
		params.put("endTime",new Date());
		//执行查询，demo为索引表，_search为检索操作action
		ESDatas<Demo> esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
				clientUtil.searchList("demo/_search",//demo为索引表，_search为检索操作action
						"searchDatasArray",//esmapper/demo.xml中定义的dsl语句
						params,//变量参数
						Demo.class);//返回的文档封装对象类型
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

	public void testpons(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(mappath);
		Demo demo = new Demo();
		demo.setDemoId(2l);
		demo.setAgentStarttime(new Date());
		demo.setApplicationName("blackcatdemo2");
		demo.setContentbody("bulk update content body2");
		demo.setName("刘德华bulk update ");
		clientUtil.addDocument("demo",//索引表
				"demo",//索引类型
				"updatePartDocument",
				demo);
	}
}
