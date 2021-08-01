package org.bboss.elasticsearchtest.crud;
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

import com.frameworkset.util.SimpleStringUtil;
import org.frameworkset.elasticsearch.client.LogDslCallback;
import org.frameworkset.elasticsearch.entity.LogDsl;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/11/13 14:07
 * @author biaoping.yin
 * @version 1.0
 */
public class TestSlowDslCallback implements LogDslCallback {
	public void logDsl(LogDsl slowDsl) {
		System.out.println(SimpleStringUtil.object2json(slowDsl));
	}
}
