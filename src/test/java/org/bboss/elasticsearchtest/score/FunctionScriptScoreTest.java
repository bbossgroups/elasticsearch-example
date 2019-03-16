package org.bboss.elasticsearchtest.score;
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
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/3/16 14:03
 * @author biaoping.yin
 * @version 1.0
 */
public class FunctionScriptScoreTest {
	private ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/score.xml");
	private void createUserInfoIndice(){
		if(clientInterface.existIndice("user-info")){
			clientInterface.dropIndice("user-info");
		}
		clientInterface.createIndiceMapping("user-info","createUserInfoIndice");
	}

	private void createUserInfoScoreScript(){
		try {
			clientInterface.executeHttp("_scripts/user_info_score", ClientInterface.HTTP_DELETE);//删除user_info_score
		}
		catch(Exception e){
			e.printStackTrace();

		}
		clientInterface.executeHttp("_scripts/user_info_score", "userInfoScore",
									ClientInterface.HTTP_POST);//创建评分脚本函数user_info_score

		String user_info_score = clientInterface.executeHttp("_scripts/user_info_score",
				ClientInterface.HTTP_GET);//获取刚才创建评分脚本函数user_info_score
		System.out.println(user_info_score);

	}
	private void importUserInfoData(){
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		UserInfo userInfo = new UserInfo();
		userInfo.setName("高 X");
		userInfo.setUserId("1");
		userInfoList.add(userInfo);
		userInfo = new UserInfo();
		userInfo.setName("高 XX");
		userInfo.setUserId("2");
		userInfoList.add(userInfo);
		userInfo = new UserInfo();
		userInfo.setName("X 高 X");
		userInfo.setUserId("3");
		userInfoList.add(userInfo);
		userInfo = new UserInfo();
		userInfo.setName("X 高 X");
		userInfo.setUserId("4");
		userInfoList.add(userInfo);
		userInfo = new UserInfo();
		userInfo.setName("XXX 高");
		userInfo.setUserId("5");
		userInfoList.add(userInfo);
		userInfo = new UserInfo();
		userInfo.setName("高 XXX");
		userInfo.setUserId("6");
		userInfoList.add(userInfo);
		userInfo = new UserInfo();
		userInfo.setName("XXX 高 X");
		userInfo.setUserId("7");
		userInfoList.add(userInfo);
		clientInterface.addDocuments("user-info","user",userInfoList,"refresh=true");
	}
	private void queryUserInfo(){
		//普通检索
		Map<String,String> params = new HashMap<String, String>();
		params.put("name","高");
		ESDatas<UserInfo> datas = clientInterface.searchList("user-info/_search","nameQuery",params,UserInfo.class);
		List<UserInfo> userInfos = datas.getDatas();
		System.out.println("普通检索结果:");
		System.out.println(userInfos);
		System.out.println(datas.getTotalSize());

		System.out.println("普通检索结果______________________________________结束");
		System.out.println("普通检索结果______________________________________结束");
		//自定义评分函数检索
		datas = clientInterface.searchList("user-info/_search","nameScriptScoreQuery",params,UserInfo.class);
		userInfos = datas.getDatas();
		System.out.println("自定义评分函数检索:");
		System.out.println(userInfos);
		System.out.println(datas.getTotalSize());

		System.out.println("自定义评分函数检索______________________________________结束");
		System.out.println("自定义评分函数检索______________________________________结束");

	}
	@Test
	public void testFunctionScriptScore(){
		this.createUserInfoIndice();//创建通讯录索引
		this.createUserInfoScoreScript();//创建自定义评分脚本
		this.importUserInfoData();//导入测试数据
		this.queryUserInfo(); //执行普票查询和自定义评分查询，并打印查询结果
	}
}
