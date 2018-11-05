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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.annotation.ESId;
import com.frameworkset.orm.annotation.ESParentId;
import org.frameworkset.elasticsearch.entity.ESBaseData;

import java.util.Date;

public class Employee extends ESBaseData {
	/**
	 * 通过ESId注解将employeeId指定为雇员的文档_id
	 */
	@ESId
	private int employeeId;
	/**
	 * 通过ESParentId注解将companyId指定为雇员的parent属性，对应Company中的文档_id的值
	 */
	@ESParentId
	private String companyId;
	private String name;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(dataformat = "yyyy-MM-dd")
	private Date birthday;
	private String hobby;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}



	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
}
