package org.bboss.elasticsearchtest.db2es;
/*
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

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.util.SQLUtil;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * ES-JDBC测试用例
 */
public class ESJdbcTest {

	public void initDBSource() {
		SQLUtil.startPool("es",//ES数据源名称
				"org.elasticsearch.xpack.sql.jdbc.jdbc.JdbcDriver",//ES jdbc驱动
				"jdbc:es://http://127.0.0.1:9200/timezone=UTC&page.size=250",//es链接串
				"elastic", "changeme",//es x-pack账号和口令
				"SHOW tables 'db%'" //数据源连接校验sql
		);
	}

	/**
	 * 执行一个查询
	 *
	 * @throws SQLException
	 */
	@Test
	public void testSelect() throws SQLException {
		initDBSource();//启动数据源
		//执行查询，将结果映射为HashMap集合
		List<HashMap> data = SQLExecutor.queryListWithDBName(HashMap.class, "es",
				"SELECT SCORE() as score,* FROM dbclobdemo ");
		System.out.println(data);

		data = SQLExecutor.queryListWithDBName(HashMap.class, "es",
				"SELECT SCORE() as score,* FROM dbclobdemo where detailtemplateId=?", 1);
		System.out.println(data);
	}

	/**
	 * 执行一个查询
	 *
	 * @throws SQLException
	 */
	@Test
	public void testDBLogSelect() throws SQLException {
		initDBSource();//启动数据源
		//执行查询，将结果映射为HashMap集合
		List<HashMap> data = SQLExecutor.queryListWithDBName(HashMap.class, "es", "SELECT SCORE() as score,* FROM dbdemo ");
		System.out.println(data);

		data = SQLExecutor.queryListWithDBName(HashMap.class, "es", "SELECT SCORE() as score,* FROM dbdemo where logId=?", 204);
		System.out.println(data);
	}

	@Test
	public void testQuery() throws SQLException {
		initDBSource();//启动数据源
		//执行查询，将结果映射为HashMap集合,全文检索查询
		List<HashMap> data = SQLExecutor.queryListWithDBName(HashMap.class,
				"es", "SELECT * FROM hawkeye-auth-service-web-api-index-2018-06-30 where match(url_group,'synchronize_info')");
		System.out.println(data);
		//关键词精确查找
		data = SQLExecutor.queryListWithDBName(HashMap.class,
				"es", "SELECT * FROM hawkeye-auth-service-web-api-index-2018-06-30 where url_group.keyword = ?", "synchronize_info");
		System.out.println(data);
	}

	/**
	 * 进行模糊搜索，Elasticsearch 的搜索能力大家都知道，强！在 SQL 里面，可以用 match 关键字来写，如下：
	 *
	 * @throws SQLException
	 */
	@Test
	public void testMatchQuery() throws SQLException {
		initDBSource();
		List<HashMap> data = SQLExecutor.queryListWithDBName(HashMap.class, "es", "SELECT SCORE(), * FROM dbclobdemo WHERE match(content, '_ewebeditor_pa_src') ORDER BY documentId DESC");
		System.out.println(data);

		/**
		 *还能试试 SELECT 里面的一些其他操作，如过滤，别名，如下：
		 */
		data = SQLExecutor.queryListWithDBName(HashMap.class, "es", "SELECT SCORE() as score,title as myname FROM dbclobdemo  as mytable WHERE match(content, '_ewebeditor_pa_src') and (title.keyword = 'adsf' OR title.keyword ='elastic') limit 5 ");
		System.out.println(data);
	}

	/**
	 * 分组和函数计算
	 */
	@Test
	public void testGroupQuery() throws SQLException {
		initDBSource();
		List<HashMap> data = SQLExecutor.queryListWithDBName(HashMap.class, "es", "SELECT title.keyword,max(documentId) as max_id FROM dbclobdemo as mytable group by title.keyword limit 5");
		System.out.println(data);


	}


	/**
	 * 查看所有的索引表
	 *
	 * @throws SQLException
	 */
	@Test
	public void testShowTable() throws SQLException {
		initDBSource();
		List<HashMap> data = SQLExecutor.queryListWithDBName(HashMap.class, "es", "SHOW tables");
		System.out.println(data);
	}

	/**
	 * 如 dbclob 开头的索引，注意通配符只支持 %和 _，分别表示多个和单个字符（什么，不记得了，回去翻数据库的书去！）
	 *
	 * @throws SQLException
	 */
	@Test
	public void testShowTablePattern() throws SQLException {
		initDBSource();
		List<HashMap> data = SQLExecutor.queryListWithDBName(HashMap.class, "es", "SHOW tables 'dbclob_'");
		System.out.println(data);
		data = SQLExecutor.queryListWithDBName(HashMap.class, "es", "SHOW tables 'dbclob%'");
		System.out.println(data);
	}

	/**
	 * 查看索引的字段和元数据
	 *
	 * @throws SQLException
	 */
	@Test
	public void testDescTable() throws SQLException {
		initDBSource();
		List<HashMap> data = SQLExecutor.queryListWithDBName(HashMap.class, "es", "DESC dbclobdemo");
		System.out.println(data);
		data = SQLExecutor.queryListWithDBName(HashMap.class, "es", "SHOW COLUMNS IN dbclobdemo");
		System.out.println(data);
	}

	/**
	 * 不记得 ES 支持哪些函数，只需要执行下面的命令，即可得到完整列表
	 *
	 * @throws SQLException
	 */
	@Test
	public void testShowFunctin() throws SQLException {
		initDBSource();
		List<HashMap> data = SQLExecutor.queryListWithDBName(HashMap.class, "es", "SHOW FUNCTIONS");
		System.out.println(data);
		//同样支持通配符进行过滤：
		data = SQLExecutor.queryListWithDBName(HashMap.class, "es", "SHOW FUNCTIONS 'S__'");
		System.out.println(data);

	}
}
