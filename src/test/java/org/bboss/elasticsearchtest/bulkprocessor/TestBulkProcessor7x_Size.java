package org.bboss.elasticsearchtest.bulkprocessor;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: for elasticsearch 7x</p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/12/8 9:57
 * @author biaoping.yin
 * @version 1.0
 */
public class TestBulkProcessor7x_Size {
	private static Logger logger = LoggerFactory.getLogger(TestBulkProcessor7x_Size.class);

	public static void main(String[] args){
		TestBulkProcessor7x testBulkProcessor = new TestBulkProcessor7x();
        //设置批量记录占用内存最大值，以字节为单位，达到最大值时，执行一次bulk操作
        // 可以根据实际情况调整maxMemSize参数，如果不设置maxMemSize，则按照按批处理数据记录数BulkSizes来判别是否执行执行一次bulk操作
        //maxMemSize参数默认值为0，不起作用，只有>0才起作用

        testBulkProcessor.buildBulkProcessor(0);//禁用maxMemSize参数
//		testBulkProcessor.buildLogBulkProcessor();
		testBulkProcessor.testBulkDatas();
		
//		testBulkProcessor.shutdown(false);//调用shutDown停止方法后，BulkProcessor不会接收新的请求，但是会处理完所有已经进入bulk队列的数据


	}

}
