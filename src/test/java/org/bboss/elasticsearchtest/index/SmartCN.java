package org.bboss.elasticsearchtest.index;
/**
 * Copyright 2020 bboss
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

import com.frameworkset.util.SimpleStringUtil;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description:
 * 安装smartcn 插件
 * elasticsearch-plugin install analysis-smartcn
 * </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/2/20 16:49
 * @author biaoping.yin
 * @version 1.0
 */
public class SmartCN {
	public static void main(String[] args){

//		createIndiceMapping();
		testmy_shingle_analyzer_idxDatas();
//		testDatas();
		createUserInfoScoreScript();
		searchmy_shingle_analyzer_idxDatas();
		searchmy_shingle_analyzer_idxDatasfunctionscore();

	}
	private static void createUserInfoScoreScript(){
		ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/analyzer.xml");
		try {

			clientInterface.executeHttp("_scripts/title_info_score", ClientInterface.HTTP_DELETE);//删除user_info_score
		}
		catch(Exception e){
			e.printStackTrace();

		}
			clientInterface.executeHttp("_scripts/title_info_score", "titleInfoScore",
					ClientInterface.HTTP_POST);//创建评分脚本函数user_info_score

			String user_info_score = clientInterface.executeHttp("_scripts/title_info_score",
					ClientInterface.HTTP_GET);//获取刚才创建评分脚本函数user_info_score
			System.out.println(user_info_score);



	}
	public static void createIndiceMapping(){
		ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/analyzer.xml");

		try {
			clientInterface.dropIndice("my_shingle_analyzer_idx");
			clientInterface.createIndiceMapping("my_shingle_analyzer_idx","createcnmy_shingle_analyzer");
		}catch (Exception e){
			e.printStackTrace();
		}
		try {
			clientInterface.dropIndice("smartcn_idx");
			clientInterface.createIndiceMapping("smartcn_idx", "createsmartcn");
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void testmy_shingle_analyzer_idxDatas(){
		List<Map> datas = new ArrayList<Map>();
		Map data = new HashMap();
		data.put("title","学校年终工作总结");
		datas.add(data);

		data = new HashMap();
		data.put("title","终工作总结");
		datas.add(data);
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		clientInterface.addDocuments("my_shingle_analyzer_idx",datas,"refresh=true");
	}

	public static void searchmy_shingle_analyzer_idxDatas(){

		Map data = new HashMap();
		data.put("title","学校终工作总结");

		ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/analyzer.xml");
		ESDatas<Map> datas = clientInterface.searchList("my_shingle_analyzer_idx/_search","searchcontentbody",data,Map.class);
		System.out.println(SimpleStringUtil.object2json(datas.getDatas()));
	}


	public static void searchmy_shingle_analyzer_idxDatasfunctionscore(){

		Map data = new HashMap();
		data.put("title","学校终工作总结");

		ClientInterface clientInterface = ElasticSearchHelper.getConfigRestClientUtil("esmapper/analyzer.xml");
		ESDatas<Map> datas = clientInterface.searchList("my_shingle_analyzer_idx/_search","titleScriptScoreQuery",data,Map.class);
		System.out.println(SimpleStringUtil.object2json(datas.getDatas()));
	}

	public static void testDatas(){
		List<Map> datas = new ArrayList<Map>();
		Map data = new HashMap();
		data.put("title","学校年终工作总结");
		datas.add(data);

		data = new HashMap();
		data.put("title","终工作总结");
		datas.add(data);
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		clientInterface.addDocuments("smartcn_idx",datas,"refresh=true");
	}


	public static void testanalyzer(){
		List<Map> datas = new ArrayList<Map>();
		Map data = new HashMap();
		data.put("title","学校年终工作总结");
		datas.add(data);

		data = new HashMap();
		data.put("title","终工作总结");
		datas.add(data);
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		clientInterface.addDocuments("smartcn_idx",datas,"refresh=true");
	}
}
