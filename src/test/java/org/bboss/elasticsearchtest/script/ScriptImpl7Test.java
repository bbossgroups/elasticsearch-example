package org.bboss.elasticsearchtest.script;/*
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

import com.frameworkset.util.SimpleStringUtil;
import org.frameworkset.elasticsearch.serial.SerialUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ScriptImpl7Test {
	@Test
	public void test(){
		ScriptImpl7 script = new ScriptImpl7();
		script.updateDocumentByScriptPath();
	}

	@Test
	public void test1(){
		ScriptImpl7 script = new ScriptImpl7();
		script.updateDocumentByScriptQueryPath();
	}

	public static void main(String[] args){
		Map value = new HashMap();
		value.put("aaa","\r\n\"");
		String _value = SerialUtil.object2json(value);
		value.put("aaa",_value);
		_value = SerialUtil.object2json(value);
		System.out.println(_value);
		value = SimpleStringUtil.json2Object(_value,HashMap.class);
		System.out.println(_value);
	}
}
