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


import com.frameworkset.orm.annotation.*;
import org.frameworkset.elasticsearch.entity.Explanation;
import org.frameworkset.elasticsearch.entity.InnerSearchHits;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 测试实体，可以从ESBaseData对象继承meta属性，检索时会将文档的一下meta属性设置到对象实例中
 */
public class MetaDemo  {
	private Object dynamicPriceTemplate;
	//设定文档标识字段
	@ESMetaId
	private Long demoId;
	private String contentbody;
	/**  当在mapping定义中指定了日期格式时，则需要指定以下两个注解,例如
	 *
	 "agentStarttime": {
	 "type": "date",###指定多个日期格式
	 "format":"yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd'T'HH:mm:ss.SSS||yyyy-MM-dd HH:mm:ss||epoch_millis"
	 }
	 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	 @Column(dataformat = "yyyy-MM-dd HH:mm:ss.SSS")
	 */

	protected Date agentStarttime;
	private String applicationName;
	private String orderId;
	private int contrastStatus;
	@ESMetaType
	private String  type;
	@ESMetaFields
	private Map<String, List<Object>> fields;
	@ESMetaVersion
	private long version;
	@ESMetaIndex
	private String index;//"_index": "trace-2017.09.01",
	@ESMetaHighlight
	private Map<String,List<Object>> highlight;
	@ESMetaSort
	private Object[] sort;
	@ESMetaScore
	private Double  score;
	@ESMetaParentId
	private Object parent;
	@ESRouting
	private Object routing;
	@ESMetaFound
	private boolean found;
	@ESMetaNested
	private Map<String,Object> nested;
	@ESMetaInnerHits
	private Map<String,Map<String, InnerSearchHits>> innerHits;
	@ESMetaShard
	private String shard;//"_index": "trace-2017.09.01",
	@ESMetaNode
	private String node;//"_index": "trace-2017.09.01",
	@ESMetaExplanation
	private Explanation explanation;//"_index": "trace-2017.09.01",
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	public String getContentbody() {
		return contentbody;
	}

	public void setContentbody(String contentbody) {
		this.contentbody = contentbody;
	}

	public Date getAgentStarttime() {
		return agentStarttime;
	}

	public void setAgentStarttime(Date agentStarttime) {
		this.agentStarttime = agentStarttime;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public Long getDemoId() {
		return demoId;
	}

	public void setDemoId(Long demoId) {
		this.demoId = demoId;
	}

	public Object getDynamicPriceTemplate() {
		return dynamicPriceTemplate;
	}

	public void setDynamicPriceTemplate(Object dynamicPriceTemplate) {
		this.dynamicPriceTemplate = dynamicPriceTemplate;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getContrastStatus() {
		return contrastStatus;
	}

	public void setContrastStatus(int contrastStatus) {
		this.contrastStatus = contrastStatus;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, List<Object>> getFields() {
		return fields;
	}

	public void setFields(Map<String, List<Object>> fields) {
		this.fields = fields;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public Map<String, List<Object>> getHighlight() {
		return highlight;
	}

	public void setHighlight(Map<String, List<Object>> highlight) {
		this.highlight = highlight;
	}

	public Object[] getSort() {
		return sort;
	}

	public void setSort(Object[] sort) {
		this.sort = sort;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Object getParent() {
		return parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public Object getRouting() {
		return routing;
	}

	public void setRouting(Object routing) {
		this.routing = routing;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public Map<String, Object> getNested() {
		return nested;
	}

	public void setNested(Map<String, Object> nested) {
		this.nested = nested;
	}

	public Map<String, Map<String, InnerSearchHits>> getInnerHits() {
		return innerHits;
	}

	public void setInnerHits(Map<String, Map<String, InnerSearchHits>> innerHits) {
		this.innerHits = innerHits;
	}

	public String getShard() {
		return shard;
	}

	public void setShard(String shard) {
		this.shard = shard;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public Explanation getExplanation() {
		return explanation;
	}

	public void setExplanation(Explanation explanation) {
		this.explanation = explanation;
	}
}
