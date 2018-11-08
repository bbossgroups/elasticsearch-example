package org.bboss.elasticsearchtest.parentchild;/*
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

import org.frameworkset.elasticsearch.ElasticSearchException;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.client.ClientUtil;
import org.frameworkset.elasticsearch.client.ResultUtil;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.serial.ESInnerHitSerialThreadLocal;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentChildTest {

	public void createIndice(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/indexparentchild.xml");
		try {
			//删除mapping
			clientUtil.dropIndice("company");
		} catch (ElasticSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//创建mapping
		clientUtil.createIndiceMapping("company","createCompanyEmployeeIndice");
	}

	/**
	 * 通过读取配置文件中的dsl json数据导入雇员和公司数据
	 */
	public void importFromJsonData(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/indexparentchild.xml");

		//导入公司数据,并且实时刷新，测试需要，实际环境不要带refresh
		clientUtil.executeHttp("company/company/_bulk?refresh","bulkImportCompanyData",ClientUtil.HTTP_POST);
		//导入雇员数据,并且实时刷新，测试需要，实际环境不要带refresh
		clientUtil.executeHttp("company/employee/_bulk?refresh","bulkImportEmployeeData",ClientUtil.HTTP_POST);
		long companycount = clientUtil.countAll("company/company");
		System.out.println(companycount);
		long employeecount = clientUtil.countAll("company/employee");
		System.out.println(employeecount);
	}

	private List<Company> buildCompanies(){
		List<Company> companies = new ArrayList<Company>();
		Company company = new Company();
		company.setName("London Westminster");
		company.setCity("London");
		company.setCountry("UK");
		company.setCompanyId("london");//指定公司_id
		companies.add(company);

		company = new Company();
		company.setName("Liverpool Central");
		company.setCity("Liverpool");
		company.setCompanyId("liverpool");//指定公司_id
		company.setCountry("UK");
		companies.add(company);

		company = new Company();
		company.setName("Champs Élysées");
		company.setCity("Paris");
		company.setCompanyId("paris");//指定公司_id
		company.setCountry("France");
		companies.add(company);
		return companies;
	}

	private List<Employee> buildEmployees()  {
		try {
			List<Employee> employees = new ArrayList<Employee>();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Employee employee = new Employee();
			employee.setCompanyId("london");//指定parent：公司_id
			employee.setEmployeeId(1);//指定雇员_id
			employee.setName("Alice Smith");
			employee.setHobby("hiking");
			employee.setBirthday(dateFormat.parse("1970-10-24"));
			employees.add(employee);

			employee = new Employee();
			employee.setCompanyId("london");//指定parent：公司_id
			employee.setEmployeeId(2);//指定雇员_id
			employee.setName("Mark Thomas");
			employee.setHobby("diving");
			employee.setBirthday(dateFormat.parse("1982-05-16"));
			employees.add(employee);

			employee = new Employee();
			employee.setCompanyId("liverpool");//指定parent：公司_id
			employee.setEmployeeId(3);//指定雇员_id
			employee.setName("Barry Smith");
			employee.setHobby("hiking");
			employee.setBirthday(dateFormat.parse("1979-04-01"));
			employees.add(employee);

			employee = new Employee();
			employee.setCompanyId("paris");//指定parent：公司_id
			employee.setEmployeeId(4);//指定雇员_id
			employee.setName("Adrien Grand");
			employee.setHobby("horses");
			employee.setBirthday(dateFormat.parse("1987-05-11"));
			employees.add(employee);

			employee = new Employee();
			employee.setCompanyId("paris");//指定parent：公司_id
			employee.setEmployeeId(5);//指定雇员_id
			employee.setName("Adrien Green");
			employee.setHobby("dancing");
			employee.setBirthday(dateFormat.parse("1977-05-12"));
			employees.add(employee);
			return employees;
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
		List<Company> companies = buildCompanies();
		clientUtil.addDocuments("company","company",companies,"refresh");

		//导入雇员数据,并且实时刷新，测试需要，实际环境不要带refresh
		List<Employee> employees = buildEmployees();
		clientUtil.addDocuments("company","employee",employees,"refresh");

	}

	/**
	 * 通过雇员生日检索公司信息
	 */
	public void hasChildSearchByBirthday(){

		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/indexparentchild.xml");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("birthday","1980-01-01");
		ESDatas<Company> escompanys = clientUtil.searchList("company/company/_search","hasChildSearchByBirthday",params,Company.class);
		List<Company> companyList = escompanys.getDatas();//获取符合条件的公司
		long totalSize = escompanys.getTotalSize();
	}

	/**
	 * 通过雇员姓名检索公司信息
	 */
	public void hasChildSearchByName(){

		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/indexparentchild.xml");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name","Alice Smith");
		ESDatas<Company> escompanys = clientUtil.searchList("company/company/_search","hasChildSearchByName",params,Company.class);
		List<Company> companyList = escompanys.getDatas();//获取符合条件的公司
		long totalSize = escompanys.getTotalSize();

	}
	/**
	 * 通过雇员数量检索公司信息
	 */
	public void hasChildSearchByMinChild(){

		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/indexparentchild.xml");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("min_children",2);
		ESDatas<Company> escompanys = clientUtil.searchList("company/company/_search","hasChildSearchByMinChild",params,Company.class);
		List<Company> companyList = escompanys.getDatas();//获取符合条件的公司
		long totalSize = escompanys.getTotalSize();

	}
	/**
	 * 通过公司所在国家检索雇员信息
	 */
	public void hasParentSearchByCountry(){

		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/indexparentchild.xml");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("country","UK");
		ESDatas<Employee> escompanys = clientUtil.searchList("company/employee/_search","hasParentSearchByCountry",params,Employee.class);
		List<Employee> companyList = escompanys.getDatas();//获取符合条件的公司
		long totalSize = escompanys.getTotalSize();

	}
	/**
	 * 检索公司信息，并返回公司对应的雇员信息（符合检索条件的雇员信息）
	 */
	public void hasChildSearchReturnParent2ndChildren(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/indexparentchild.xml");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name","Alice Smith");

		try {
			ESInnerHitSerialThreadLocal.setESInnerTypeReferences("employee",Employee.class);//指定inner查询结果对于雇员类型
//			ESInnerHitSerialThreadLocal.setESInnerTypeReferences("othersontype",OtherSon.class);//指定inner查询结果对于雇员类型
			ESDatas<Company> escompanys = clientUtil.searchList("company/company/_search",
													"hasChildSearchReturnParent2ndChildren",params,Company.class);
			long totalSize = escompanys.getTotalSize();
			List<Company> companyList = escompanys.getDatas();//获取符合条件的公司
			//查看公司下面的雇员信息（符合检索条件的雇员信息）
			for (int i = 0; i < companyList.size(); i++) {
				Company company = companyList.get(i);
				List<Employee> employees = ResultUtil.getInnerHits(company.getInnerHits(), "employee");
				System.out.println(employees.size());
//				List<OtherSon> otherSons = ResultUtil.getInnerHits(company.getInnerHits(), "othersontype");
//				System.out.println(otherSons.size());
			}
		}
		finally{
			ESInnerHitSerialThreadLocal.clean();//清空inner查询结果对于雇员类型
		}
	}
	public void createClientIndice(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/Client_Info.xml");
		try {
			//删除mapping
			clientUtil.dropIndice("client_info");
		} catch (ElasticSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//创建mapping
		clientUtil.createIndiceMapping("client_info","createClientIndice");
	}

	/**
	 * 通过读取配置文件中的dsl json数据导入雇员和公司数据
	 */
	public void importClientInfoFromJsonData(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/Client_Info.xml");


		clientUtil.executeHttp("client_info/basic/_bulk?refresh","bulkImportBasicData",ClientUtil.HTTP_POST);
		clientUtil.executeHttp("client_info/diagnosis/_bulk?refresh","bulkImportDiagnosisData",ClientUtil.HTTP_POST);
		clientUtil.executeHttp("client_info/medical/_bulk?refresh","bulkImportMedicalData",ClientUtil.HTTP_POST);
		clientUtil.executeHttp("client_info/exam/_bulk?refresh","bulkImportExamData",ClientUtil.HTTP_POST);
		long companycount = clientUtil.countAll("client_info/basic");
		System.out.println(companycount);
		long basiccount = clientUtil.countAll("client_info/basic");
		System.out.println(basiccount);
		long medicalcount = clientUtil.countAll("client_info/medical");
		System.out.println(medicalcount);
		long examcount = clientUtil.countAll("client_info/exam");
		System.out.println(examcount);
		long diagnosiscount = clientUtil.countAll("client_info/diagnosis");
		System.out.println(diagnosiscount);
	}
	/**
	 * 查找顾客信息，演示多子文档父子数据查询功能
	 */
	public void hasParentSearchByCountryReturnParent2ndMultiChildren(){
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/Client_Info.xml");
		Map<String,Object> params = new HashMap<String,Object>();//没有检索条件，构造一个空的参数对象
//		params.put("name","Alice Smith");

		try {
			//设置子文档的类型和对象映射关系
			ESInnerHitSerialThreadLocal.setESInnerTypeReferences("exam",Exam.class);//指定inner查询结果对于exam类型和对应的对象类型Exam
			ESInnerHitSerialThreadLocal.setESInnerTypeReferences("diagnosis",Diagnosis.class);//指定inner查询结果对于diagnosis类型和对应的对象类型Diagnosis
			ESInnerHitSerialThreadLocal.setESInnerTypeReferences("medical",Medical.class);//指定inner查询结果对于medical类型和对应的对象类型Medical
			ESDatas<Basic> escompanys = clientUtil.searchList("client_info/basic/_search",
					"hasParentSearchByCountryReturnParent2ndMultiChildren",params,Basic.class);
//			escompanys = clientUtil.searchAll("client_info",Basic.class);
			long totalSize = escompanys.getTotalSize();
			List<Basic> clientInfos = escompanys.getDatas();//获取符合条件的数据
			//查看公司下面的雇员信息（符合检索条件的雇员信息）
			for (int i = 0; clientInfos != null && i < clientInfos.size(); i++) {
				Basic clientInfo = clientInfos.get(i);
				List<Exam> exams = ResultUtil.getInnerHits(clientInfo.getInnerHits(), "exam");
				if(exams != null)
					System.out.println(exams.size());
				List<Diagnosis> diagnosiss = ResultUtil.getInnerHits(clientInfo.getInnerHits(), "diagnosis");
				if(diagnosiss != null)
					System.out.println(diagnosiss.size());
				List<Medical> medicals = ResultUtil.getInnerHits(clientInfo.getInnerHits(), "medical");
				if(medicals != null)
					System.out.println(medicals.size());

			}
		}
		finally{
			ESInnerHitSerialThreadLocal.clean();//清空inner查询结果对于雇员类型
		}
	}
	/**
	 * 通过公司所在国家检索雇员信息,并返回雇员对应的公司信息
	 */
	public void hasParentSearchByCountryReturnParent2ndChildren(){

		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/indexparentchild.xml");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("country","UK");

		try {
			ESInnerHitSerialThreadLocal.setESInnerTypeReferences(Company.class);//指定inner查询结果对于公司类型,公司只有一个文档类型，索引不需要显示指定company类型信息
			ESDatas<Employee> escompanys = clientUtil.searchList("company/employee/_search",
													"hasParentSearchByCountryReturnParent2ndChildren",params,Employee.class);
			List<Employee> employeeList = escompanys.getDatas();//获取符合条件的雇员数据
			long totalSize = escompanys.getTotalSize();
			//查看每个雇员对应的公司信息
			for(int i = 0;  i < employeeList.size(); i ++) {
				Employee employee = employeeList.get(i);
				List<Company> companies = ResultUtil.getInnerHits(employee.getInnerHits(), "company");
				System.out.println(companies.size());
			}
		}
		finally{
			ESInnerHitSerialThreadLocal.clean();//清空inner查询结果对于公司类型
		}
	}

	@Test
	public void testMutil(){
		this.createClientIndice();
		this.importClientInfoFromJsonData();
		this.hasParentSearchByCountryReturnParent2ndMultiChildren();
	}
	@Test
	public void testFromBeans(){
		createIndice();
		this.importDataFromBeans();
		hasChildSearchByBirthday();
		this.hasChildSearchByName();
		this.hasChildSearchByMinChild();
		this.hasParentSearchByCountry();
	}
	@Test
	public void testFromJson(){
		createIndice();
		importFromJsonData();
		hasChildSearchByBirthday();
		this.hasChildSearchByName();
		this.hasChildSearchByMinChild();
		this.hasParentSearchByCountry();

		this.hasChildSearchReturnParent2ndChildren();
		this.hasParentSearchByCountryReturnParent2ndChildren();
	}

}
