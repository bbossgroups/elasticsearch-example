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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.annotation.ESRouting;
import org.frameworkset.elasticsearch.entity.ESBaseData;
import org.frameworkset.elasticsearch.entity.JoinSon;

import java.util.Date;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/11/22 16:10
 * @author biaoping.yin
 * @version 1.0
 */
public class Question extends ESBaseData {
	private String name;
	private String content;
	private String person;

	private int datatype;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(dataformat = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("created_date")
	private Date createdDate;
	@JsonProperty("question_join")
	private JoinSon questionJoin;
	@ESRouting
	private String routingId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public int getDatatype() {
		return datatype;
	}

	public void setDatatype(int datatype) {
		this.datatype = datatype;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public JoinSon getQuestionJoin() {
		return questionJoin;
	}

	public void setQuestionJoin(JoinSon questionJoin) {
		this.questionJoin = questionJoin;
	}
}
