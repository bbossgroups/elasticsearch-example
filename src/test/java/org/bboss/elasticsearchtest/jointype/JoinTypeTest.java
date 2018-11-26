package org.bboss.elasticsearchtest.jointype;
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
import org.frameworkset.elasticsearch.client.ResultUtil;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.serial.ESInnerHitSerialThreadLocal;
import org.frameworkset.elasticsearch.template.ESInfo;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/9/11 11:47
 * @author biaoping.yin
 * @version 1.0
 */
public class JoinTypeTest {
	private ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/joinparentchild.xml");
	private void createPagerIndice(){
		try {
			clientInterface.dropIndice("pager");
		}
		catch (Exception e){

		}
		String response = clientInterface.createIndiceMapping("pager","createPagerIndice");
		System.out.println(response);
		String mapping = clientInterface.getIndexMapping("pager");
		System.out.println(mapping);

	}
	private void importDatas(){


		ClientInterface restClient = ElasticSearchHelper.getRestClientUtil();
		//导入数据,并且实时刷新，测试需要，实际环境不要带refresh
		ESInfo esInfo = clientInterface.getESInfo("bulkImportQuestionData");
		StringBuilder data = new StringBuilder();
		data.append(esInfo.getTemplate().trim());
		data.append("\n");
		restClient.executeHttp("pager/pagertype/_bulk?refresh",data.toString(), ClientUtil.HTTP_POST);
		//导入数据,并且实时刷新，测试需要，实际环境不要带refresh
		data.setLength(0);
		esInfo = clientInterface.getESInfo("bulkImportAnswerData");
		data = new StringBuilder();
		data.append(esInfo.getTemplate().trim());
		data.append("\n");
		restClient.executeHttp("pager/pagertype/_bulk?refresh",data.toString(),ClientUtil.HTTP_POST);
		//导入数据,并且实时刷新，测试需要，实际环境不要带refresh
		data.setLength(0);
		esInfo = clientInterface.getESInfo("bulkImportCommentData");
		data = new StringBuilder();
		data.append(esInfo.getTemplate().trim());
		data.append("\n");
		restClient.executeHttp("pager/pagertype/_bulk?refresh",data.toString(),ClientUtil.HTTP_POST);
		long companycount = clientInterface.countAll("pager/pagertype");
		System.out.println(companycount);

	}

	/**
	 * 根据问题名称检索答案和评论信息，同时返回问题及答案和评论信息
	 */
	public void hasParentSearchByCountryReturnParent2ndMultiChildren(){
		Map<String,Object> params = new HashMap<String,Object>();//没有检索条件，构造一个空的参数对象
//		params.put("name","Alice Smith");

		try {
			//设置子文档的类型和对象映射关系
			ESInnerHitSerialThreadLocal.setESInnerTypeReferences("answer", Answer.class);//指定inner查询结果对于exam类型和对应的对象类型Exam
			ESInnerHitSerialThreadLocal.setESInnerTypeReferences("comment", Comment.class);//指定inner查询结果对于diagnosis类型和对应的对象类型Diagnosis
			String response = clientInterface.executeRequest("pager/pagertype/_search","hasParentSearchByNameReturnAnswerAndComment",params);
			ESDatas<Question> escompanys = clientInterface.searchList("pager/pagertype/_search",
					"hasParentSearchByNameReturnAnswerAndComment",params,Question.class);
//			escompanys = clientUtil.searchAll("client_info",Basic.class);
			long totalSize = escompanys.getTotalSize();
			List<Question> clientInfos = escompanys.getDatas();//获取符合条件的数据
			//查看公司下面的雇员信息（符合检索条件的雇员信息）
			for (int i = 0; clientInfos != null && i < clientInfos.size(); i++) {
				Question question = clientInfos.get(i);
				List<Answer> answers = ResultUtil.getInnerHits(question.getInnerHits(), "answer");
				if(answers != null)
					System.out.println(answers.size());
				List<Comment> comments = ResultUtil.getInnerHits(question.getInnerHits(), "comment");
				if(comments != null)
					System.out.println(comments.size());


			}
		}
		finally{
			ESInnerHitSerialThreadLocal.clean();//清空inner查询结果对于雇员类型
		}
	}
	/**
	 * 根据问题名称，检索答案和评论信息以及对应的问题信息
	 */
	public void hasParentSearchByCountryReturnParent2ndChildren(){

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name","q4");

		try {
			ESInnerHitSerialThreadLocal.setESInnerTypeReferences(Question.class);//指定inner查询结果对于公司类型,公司只有一个文档类型，索引不需要显示指定company类型信息
			ESDatas<Answer> escompanys = clientInterface.searchList("pager/pagertype/_search",
					"hasParentSearchByQuestionNameReturnParent2ndChildren",params,Answer.class);
			List<Answer> employeeList = escompanys.getDatas();//获取符合条件的雇员数据
			long totalSize = escompanys.getTotalSize();
			//查看每个雇员对应的公司信息
			for(int i = 0;  i < employeeList.size(); i ++) {
				Answer employee = employeeList.get(i);
				List<Question> companies = ResultUtil.getInnerHits(employee.getInnerHits(), "question");
				System.out.println(companies.size());
			}
		}
		finally{
			ESInnerHitSerialThreadLocal.clean();//清空inner查询结果对于公司类型
		}
	}

	/**
	 * 根据问题名称，检索答案信息
	 */

	public void hasParentSearchByName(){

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name","q4");

		try {
			ESDatas<Answer> escompanys = clientInterface.searchList("pager/pagertype/_search",
					"hasParentSearchByName",params,Answer.class);
			List<Answer> employeeList = escompanys.getDatas();//获取符合条件的雇员数据
			long totalSize = escompanys.getTotalSize();
			//查看每个雇员对应的公司信息
			for(int i = 0;  i < employeeList.size(); i ++) {
				Answer employee = employeeList.get(i);
				System.out.println(employee.getDatatype());
			}
		}
		finally{
			ESInnerHitSerialThreadLocal.clean();//清空inner查询结果对于公司类型
		}
	}

	/**
	 * 根据问题id，检索答案信息
	 */
	public void hasParentIdSearch(){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id","1");

		try {
			ESDatas<Answer> escompanys = clientInterface.searchList("pager/pagertype/_search",
					"hasParentIdSearch",params,Answer.class);
			List<Answer> employeeList = escompanys.getDatas();//获取符合条件的雇员数据
			long totalSize = escompanys.getTotalSize();
			//查看每个雇员对应的公司信息
			for(int i = 0;  i < employeeList.size(); i ++) {
				Answer employee = employeeList.get(i);
				System.out.println(employee.getDatatype());
			}
		}
		finally{
			ESInnerHitSerialThreadLocal.clean();//清空inner查询结果对于公司类型
		}
	}
	@Test
	public void testJoin(){
		createPagerIndice();
		importDatas();
		hasParentIdSearch();
		hasParentSearchByName();
		hasParentSearchByCountryReturnParent2ndChildren();
		hasParentSearchByCountryReturnParent2ndMultiChildren();


	}
}
