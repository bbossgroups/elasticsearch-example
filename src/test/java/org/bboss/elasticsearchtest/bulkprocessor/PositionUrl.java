package org.bboss.elasticsearchtest.bulkprocessor;
/**
 *  Copyright 2008-2010 biaoping.yin
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


import com.frameworkset.orm.annotation.PrimaryKey;

import java.sql.Timestamp;

/**
 * <p>Description: </p>
 *
 * @author jun.xie
 * @version 1.0
 * @Date 2022/1/12 16:21
 */
public class PositionUrl implements java.io.Serializable {
	/**
	 * 主键
	 */
	@PrimaryKey
	private String id;
	/**
	 * 触点URL
	 */
	private String positionUrl;
	/**
	 * 触点名称
	 */
	private String positionName;
	/**
	 * 创建时间
	 */
	private Timestamp createtime;
	public PositionUrl() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPositionUrl() {
		return positionUrl;
	}

	public void setPositionUrl(String positionUrl) {
		this.positionUrl = positionUrl;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
}