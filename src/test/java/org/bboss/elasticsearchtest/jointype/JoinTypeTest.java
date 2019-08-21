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
import org.frameworkset.elasticsearch.entity.JoinSon;
import org.frameworkset.elasticsearch.serial.ESInnerHitSerialThreadLocal;
import org.frameworkset.elasticsearch.template.ESInfo;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 *
 * @author biaoping.yin
 * @version 1.0
 * @Date 2018/9/11 11:47
 */
public class JoinTypeTest {
	private ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/joinparentchild.xml");

	/**
	 * 创建join父子关系indice ：pager
	 */
	private void createPagerIndice() {
		try {
			clientInterface.dropIndice("pager");
		} catch (Exception e) {

		}
		String response = clientInterface.createIndiceMapping("pager", "createPagerIndice");
		System.out.println(response);
		String mapping = clientInterface.getIndexMapping("pager");
		System.out.println(mapping);

	}


	private List<Question> buildQuestions(){
		List<Question> questions = new ArrayList<Question>();
		Question question = new Question();
		question.setQid("1");
		question.setName("q1");
		question.setContent("This is a question 1");
		question.setPerson("john");
		question.setDatatype(0);//数据类型为：0 问题类型
		question.setCreatedDate(new Date());
		JoinSon joinSon = new JoinSon();
		joinSon.setName("question");
		question.setQuestionJoin(joinSon);
		questions.add(question);

		question = new Question();
		question.setQid("2");
		question.setName("q2");
		question.setContent("This is a question 2");
		question.setPerson("john");
		question.setDatatype(0);//数据类型为：0 问题类型
		question.setCreatedDate(new Date());
		joinSon = new JoinSon();
		joinSon.setName("question");
		question.setQuestionJoin(joinSon);
		questions.add(question);

		question = new Question();
		question.setQid("3");
		question.setName("q3");
		question.setContent("This is a question 3");
		question.setPerson("john");
		question.setDatatype(0);//数据类型为：0 问题类型
		question.setCreatedDate(new Date());
		joinSon = new JoinSon();
		joinSon.setName("question");
		question.setQuestionJoin(joinSon);
		questions.add(question);

		question = new Question();
		question.setQid("4");
		question.setName("q4");
		question.setContent("This is a question 4");
		question.setPerson("john");
		question.setDatatype(0);//数据类型为：0 问题类型
		question.setCreatedDate(new Date());
		joinSon = new JoinSon();
		joinSon.setName("question");
		question.setQuestionJoin(joinSon);
		questions.add(question);

		question = new Question();
		question.setQid("5");
		question.setName("q5");
		question.setContent("This is a question 5");
		question.setPerson("john");
		question.setDatatype(0);//数据类型为：0 问题类型
		question.setCreatedDate(new Date());
		joinSon = new JoinSon();
		joinSon.setName("question");
		question.setQuestionJoin(joinSon);
		questions.add(question);
		return questions;
	}

	private List<Comment> buildComments()  {
		try {
			List<Comment> comments = new ArrayList<Comment>();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Comment comment = new Comment();
			comment.setCid("18");
			comment.setRoutingId("1");
			comment.setName("c1");
			comment.setContent("This is a comment 1");
			comment.setPerson("john");
			comment.setDatatype(2);//数据类型为：2 评论类型
			comment.setCreatedDate(new Date());
			JoinSon joinSon = new JoinSon();
			joinSon.setName("comment");
			joinSon.setParent("1");
			comment.setQuestionJoin(joinSon);
			comments.add(comment);

			comment = new Comment();
			comment.setCid("19");
			comment.setRoutingId("1");
			comment.setName("c2");
			comment.setContent("This is a comment 2");
			comment.setPerson("john");
			comment.setDatatype(2);//数据类型为：2 评论类型
			comment.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("comment");
			joinSon.setParent("1");
			comment.setQuestionJoin(joinSon);
			comments.add(comment);

			comment = new Comment();
			comment.setCid("20");
			comment.setRoutingId("2");
			comment.setName("c3");
			comment.setContent("This is a comment 3");
			comment.setPerson("john");
			comment.setDatatype(2);//数据类型为：2 评论类型
			comment.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("comment");
			joinSon.setParent("2");
			comment.setQuestionJoin(joinSon);
			comments.add(comment);

			comment = new Comment();
			comment.setCid("21");
			comment.setRoutingId("2");
			comment.setName("c4");
			comment.setContent("This is a comment 4");
			comment.setPerson("john");
			comment.setDatatype(2);//数据类型为：2 评论类型
			comment.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("comment");
			joinSon.setParent("2");
			comment.setQuestionJoin(joinSon);
			comments.add(comment);

			comment = new Comment();
			comment.setCid("22");
			comment.setRoutingId("3");
			comment.setName("c5");
			comment.setContent("This is a comment 5");
			comment.setPerson("john");
			comment.setDatatype(2);//数据类型为：2 评论类型
			comment.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("comment");
			joinSon.setParent("3");
			comment.setQuestionJoin(joinSon);
			comments.add(comment);

			comment = new Comment();
			comment.setCid("23");
			comment.setRoutingId("3");
			comment.setName("c6");
			comment.setContent("This is a comment 6");
			comment.setPerson("john");
			comment.setDatatype(2);//数据类型为：2 评论类型
			comment.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("comment");
			joinSon.setParent("3");
			comment.setQuestionJoin(joinSon);
			comments.add(comment);

			comment = new Comment();
			comment.setCid("24");
			comment.setRoutingId("4");
			comment.setName("c7");
			comment.setContent("This is a comment 7");
			comment.setPerson("john");
			comment.setDatatype(2);//数据类型为：2 评论类型
			comment.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("comment");
			joinSon.setParent("4");
			comment.setQuestionJoin(joinSon);
			comments.add(comment);

			comment = new Comment();
			comment.setCid("25");
			comment.setRoutingId("4");
			comment.setName("c8");
			comment.setContent("This is a comment 8");
			comment.setPerson("john");
			comment.setDatatype(2);//数据类型为：2 评论类型
			comment.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("comment");
			joinSon.setParent("4");
			comment.setQuestionJoin(joinSon);
			comments.add(comment);

			comment = new Comment();
			comment.setCid("26");
			comment.setRoutingId("5");
			comment.setName("c9");
			comment.setContent("This is a comment 9");
			comment.setPerson("john");
			comment.setDatatype(2);//数据类型为：2 评论类型
			comment.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("comment");
			joinSon.setParent("5");
			comment.setQuestionJoin(joinSon);
			comments.add(comment);

			comment = new Comment();
			comment.setCid("27");
			comment.setRoutingId("5");
			comment.setName("c10");
			comment.setContent("This is a comment 10");
			comment.setPerson("john");
			comment.setDatatype(2);//数据类型为：2 评论类型
			comment.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("comment");
			joinSon.setParent("5");
			comment.setQuestionJoin(joinSon);
			comments.add(comment);

			comment = new Comment();
			comment.setCid("28");
			comment.setRoutingId("5");
			comment.setName("c11");
			comment.setContent("This is a comment 11");
			comment.setPerson("john");
			comment.setDatatype(2);//数据类型为：2 评论类型
			comment.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("comment");
			joinSon.setParent("5");
			comment.setQuestionJoin(joinSon);
			comments.add(comment);

			comment = new Comment();
			comment.setCid("29");
			comment.setRoutingId("5");
			comment.setName("c12");
			comment.setContent("This is a comment 12");
			comment.setPerson("john");
			comment.setDatatype(2);//数据类型为：2 评论类型
			comment.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("comment");
			joinSon.setParent("5");
			comment.setQuestionJoin(joinSon);
			comments.add(comment);

			return comments;
		}
		catch (Exception e){
			return null;
		}
	}

	private List<Answer> buildAnswers()  {
		try {
			List<Answer> answers = new ArrayList<Answer>();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Answer answer = new Answer();
			answer.setAid("6");
			answer.setRoutingId("1");
			answer.setName("c1");
			answer.setContent("This is a answer 1");
			answer.setPerson("john");
			answer.setDatatype(1);//数据类型为：1 答案类型
			answer.setCreatedDate(new Date());
			JoinSon joinSon = new JoinSon();
			joinSon.setName("answer");
			joinSon.setParent("1");
			answer.setQuestionJoin(joinSon);
			answers.add(answer);

			answer = new Answer();
			answer.setAid("7");
			answer.setRoutingId("1");
			answer.setName("c2");
			answer.setContent("This is a answer 2");
			answer.setPerson("john");
			answer.setDatatype(1);//数据类型为：1 答案类型
			answer.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("answer");
			joinSon.setParent("1");
			answer.setQuestionJoin(joinSon);
			answers.add(answer);

			answer = new Answer();
			answer.setAid("8");
			answer.setRoutingId("2");
			answer.setName("c3");
			answer.setContent("This is a answer 3");
			answer.setPerson("john");
			answer.setDatatype(1);//数据类型为：1 答案类型
			answer.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("answer");
			joinSon.setParent("2");
			answer.setQuestionJoin(joinSon);
			answers.add(answer);

			answer = new Answer();
			answer.setAid("9");
			answer.setRoutingId("2");
			answer.setName("c4");
			answer.setContent("This is a answer 4");
			answer.setPerson("john");
			answer.setDatatype(1);//数据类型为：1 答案类型
			answer.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("answer");
			joinSon.setParent("2");
			answer.setQuestionJoin(joinSon);
			answers.add(answer);

			answer = new Answer();
			answer.setAid("10");
			answer.setRoutingId("3");
			answer.setName("c5");
			answer.setContent("This is a answer 5");
			answer.setPerson("john");
			answer.setDatatype(1);//数据类型为：1 答案类型
			answer.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("answer");
			joinSon.setParent("3");
			answer.setQuestionJoin(joinSon);
			answers.add(answer);

			answer = new Answer();
			answer.setAid("11");
			answer.setRoutingId("3");
			answer.setName("c6");
			answer.setContent("This is a answer 6");
			answer.setPerson("john");
			answer.setDatatype(1);//数据类型为：1 答案类型
			answer.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("answer");
			joinSon.setParent("3");
			answer.setQuestionJoin(joinSon);
			answers.add(answer);

			answer = new Answer();
			answer.setAid("12");
			answer.setRoutingId("4");
			answer.setName("c7");
			answer.setContent("This is a answer 7");
			answer.setPerson("john");
			answer.setDatatype(1);//数据类型为：1 答案类型
			answer.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("answer");
			joinSon.setParent("4");
			answer.setQuestionJoin(joinSon);
			answers.add(answer);

			answer = new Answer();
			answer.setAid("13");
			answer.setRoutingId("4");
			answer.setName("c8");
			answer.setContent("This is a answer 8");
			answer.setPerson("john");
			answer.setDatatype(1);//数据类型为：1 答案类型
			answer.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("answer");
			joinSon.setParent("4");
			answer.setQuestionJoin(joinSon);
			answers.add(answer);

			answer = new Answer();
			answer.setAid("14");
			answer.setRoutingId("5");
			answer.setName("c9");
			answer.setContent("This is a answer 9");
			answer.setPerson("john");
			answer.setDatatype(1);//数据类型为：1 答案类型
			answer.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("answer");
			joinSon.setParent("5");
			answer.setQuestionJoin(joinSon);
			answers.add(answer);

			answer = new Answer();
			answer.setAid("15");
			answer.setRoutingId("5");
			answer.setName("c10");
			answer.setContent("This is a answer 10");
			answer.setPerson("john");
			answer.setDatatype(1);//数据类型为：1 答案类型
			answer.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("answer");
			joinSon.setParent("5");
			answer.setQuestionJoin(joinSon);
			answers.add(answer);

			answer = new Answer();
			answer.setAid("16");
			answer.setRoutingId("5");
			answer.setName("c11");
			answer.setContent("This is a answer 11");
			answer.setPerson("john");
			answer.setDatatype(1);//数据类型为：1 答案类型
			answer.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("answer");
			joinSon.setParent("5");
			answer.setQuestionJoin(joinSon);
			answers.add(answer);

			answer = new Answer();
			answer.setAid("17");
			answer.setRoutingId("5");
			answer.setName("c12");
			answer.setContent("This is a answer 12");
			answer.setPerson("john");
			answer.setDatatype(1);//数据类型为：1 答案类型
			answer.setCreatedDate(new Date());
			joinSon = new JoinSon();
			joinSon.setName("answer");
			joinSon.setParent("5");
			answer.setQuestionJoin(joinSon);
			answers.add(answer);

			return answers;
		}
		catch (Exception e){
			return null;
		}
	}

	/**
	 * 通过List集合导入雇员和公司数据
	 */
	public void importDataFromBeans()  {
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();

		//导入公司数据,并且实时刷新，测试需要，实际环境不要带refresh
		List<Question> questions = buildQuestions();
		clientUtil.addDocuments("pager","pagertype",questions,"refresh");

		//导入雇员数据,并且实时刷新，测试需要，实际环境不要带refresh
		List<Comment> comments = buildComments();
		clientUtil.addDocuments("pager","pagertype",comments,"refresh");
		List<Answer> answers = buildAnswers();
		clientUtil.addDocuments("pager","pagertype",answers,"refresh");

	}
	/**
	 * 导入测试数据
	 * 6.x对bulk的处理更加严格，所以从配置文件中获取到要导入的数据，trim掉空格，补上换行符
	 */
	private void importDatas() {


		ClientInterface restClient = ElasticSearchHelper.getRestClientUtil();
		//导入数据,并且实时刷新，测试需要，实际环境不要带refresh
		ESInfo esInfo = clientInterface.getESInfo("bulkImportQuestionData");
		StringBuilder data = new StringBuilder();
		data.append(esInfo.getTemplate().trim());
		data.append("\n");
		restClient.executeHttp("pager/pagertype/_bulk?refresh", data.toString(), ClientUtil.HTTP_POST);
		//导入数据,并且实时刷新，测试需要，实际环境不要带refresh
		data.setLength(0);
		esInfo = clientInterface.getESInfo("bulkImportAnswerData");
		data = new StringBuilder();
		data.append(esInfo.getTemplate().trim());
		data.append("\n");
		restClient.executeHttp("pager/pagertype/_bulk?refresh", data.toString(), ClientUtil.HTTP_POST);
		//导入数据,并且实时刷新，测试需要，实际环境不要带refresh
		data.setLength(0);
		esInfo = clientInterface.getESInfo("bulkImportCommentData");
		data = new StringBuilder();
		data.append(esInfo.getTemplate().trim());
		data.append("\n");
		restClient.executeHttp("pager/pagertype/_bulk?refresh", data.toString(), ClientUtil.HTTP_POST);
		long companycount = clientInterface.countAll("pager/pagertype");
		System.out.println(companycount);

	}

	/**
	 * 根据问题名称检索答案和评论信息，同时返回问题及答案和评论信息
	 */
	public void hasParentSearchByCountryReturnParent2ndMultiChildren() {
		Map<String, Object> params = new HashMap<String, Object>();//没有检索条件，构造一个空的参数对象
//		params.put("name","Alice Smith");

		try {
			//设置子文档的类型和对象映射关系
			ESInnerHitSerialThreadLocal.setESInnerTypeReferences("answer", Answer.class);//指定inner查询结果对于answer类型和对应的对象类型Answer
			ESInnerHitSerialThreadLocal.setESInnerTypeReferences("comment", Comment.class);//指定inner查询结果对于comment类型和对应的对象类型Comment
//			String response = clientInterface.executeRequest("pager/pagertype/_search", "hasParentSearchByNameReturnAnswerAndComment", params);
			ESDatas<Question> escompanys = clientInterface.searchList("pager/pagertype/_search",
					"hasParentSearchByNameReturnAnswerAndComment", params, Question.class);
//			escompanys = clientUtil.searchAll("client_info",Basic.class);
			long totalSize = escompanys.getTotalSize();
			List<Question> clientInfos = escompanys.getDatas();//获取符合条件的数据
			//查看问题信息以及对应的答案和评论信息
			for (int i = 0; clientInfos != null && i < clientInfos.size(); i++) {
				Question question = clientInfos.get(i);
				List<Answer> answers = ResultUtil.getInnerHits(question.getInnerHits(), "answer");
				if (answers != null)
					System.out.println(answers.size());
				List<Comment> comments = ResultUtil.getInnerHits(question.getInnerHits(), "comment");
				if (comments != null)
					System.out.println(comments.size());


			}
		} finally {
			ESInnerHitSerialThreadLocal.clean();//清空inner查询结果对于雇员类型
		}
	}

	/**
	 * 根据问题名称，检索答案和评论信息以及对应的问题信息
	 */
	public void hasParentSearchByCountryReturnParent2ndChildren() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "q4");

		try {
			ESInnerHitSerialThreadLocal.setESInnerTypeReferences(Question.class);//指定inner查询结果对于问题类型,问题只有一个文档类型，索引不需要显示指定Question类型信息
			ESDatas<Answer> escompanys = clientInterface.searchList("pager/pagertype/_search",
					"hasParentSearchByQuestionNameReturnParent2ndChildren", params, Answer.class);
			List<Answer> employeeList = escompanys.getDatas();
			long totalSize = escompanys.getTotalSize();
			//查看答案和评论信息以及对应的问题信息
			for (int i = 0; employeeList != null && i < employeeList.size(); i++) {
				Answer employee = employeeList.get(i);
				List<Question> companies = ResultUtil.getInnerHits(employee.getInnerHits(), "question");
				System.out.println(companies.size());
			}
		} finally {
			ESInnerHitSerialThreadLocal.clean();//清空inner查询结果对于公司类型
		}
	}

	/**
	 * 根据问题名称，检索答案信息
	 */

	public void hasParentSearchByName() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "q4");


		ESDatas<Answer> escompanys = clientInterface.searchList("pager/pagertype/_search",
				"hasParentSearchByName", params, Answer.class);
		List<Answer> employeeList = escompanys.getDatas();
		long totalSize = escompanys.getTotalSize();
		//查看符合条件的问题对应的答案信息
		for (int i = 0; i < employeeList.size(); i++) {
			Answer employee = employeeList.get(i);
			System.out.println(employee.getDatatype());
		}


	}

	/**
	 * 根据问题id，检索答案信息
	 */
	public void hasParentIdSearch() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", "1");


		ESDatas<Answer> escompanys = clientInterface.searchList("pager/pagertype/_search",
				"hasParentIdSearch", params, Answer.class);
		List<Answer> employeeList = escompanys.getDatas();//根据问题id，检索答案信息
		long totalSize = escompanys.getTotalSize();
		//查看答案信息
		for (int i = 0; i < employeeList.size(); i++) {
			Answer employee = employeeList.get(i);
			System.out.println(employee.getDatatype());
		}


	}

	/**
	 * 运行demo的junit测试方法
	 */
	@Test
	public void testJoin() {
		createPagerIndice();
		importDatas();
		hasParentIdSearch();
		hasParentSearchByName();
		hasParentSearchByCountryReturnParent2ndChildren();
		hasParentSearchByCountryReturnParent2ndMultiChildren();


	}

	/**
	 * 运行demo的junit测试方法
	 */
	@Test
	public void testJoinBean() {
		createPagerIndice();
		importDataFromBeans();
		hasParentIdSearch();
		hasParentSearchByName();
		hasParentSearchByCountryReturnParent2ndChildren();
		hasParentSearchByCountryReturnParent2ndMultiChildren();


	}

}
