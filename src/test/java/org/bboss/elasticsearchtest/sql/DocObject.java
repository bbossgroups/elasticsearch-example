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

import com.frameworkset.orm.annotation.Column;

import java.util.Date;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/9/17 10:49
 * @author biaoping.yin
 * @version 1.0
 */
public class DocObject {
	private int isnew;
	private Date createtime;
	private String content;
	private int documentId;
	private int channelId;

	/**
	 * 通过column指定索引文档和对象属性的映射关系
	 * 通过column注解还可以指定日期格式和时区信息
	 * @Column(name="docInfo.author",dataformat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",timezone = "Etc/UTC",locale = "zh")
	 *
	 */
	@Column(name="docInfo.author")
	private String docInfoAuthor;

	public int getIsnew() {
		return isnew;
	}

	public void setIsnew(int isnew) {
		this.isnew = isnew;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getDocInfoAuthor() {
		return docInfoAuthor;
	}

	public void setDocInfoAuthor(String docInfoAuthor) {
		this.docInfoAuthor = docInfoAuthor;
	}
}
