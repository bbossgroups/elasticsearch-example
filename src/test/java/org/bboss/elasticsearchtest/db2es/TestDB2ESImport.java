package org.bboss.elasticsearchtest.db2es;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.StatementInfo;
import com.frameworkset.common.poolman.handle.ResultSetHandler;
import com.frameworkset.common.poolman.util.SQLUtil;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.*;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 * 数据导入文档：https://my.oschina.net/bboss/blog/1832212
 * mysql数据导入es测试用例
 */
public class TestDB2ESImport {

	private void initDBSource(){
		SQLUtil.startNoPool("test",//数据源名称
				"com.mysql.jdbc.Driver",//mysql驱动
				"jdbc:mysql://localhost:3306/bboss",//mysql链接串
				"root","123456",//数据库账号和口令
				"select 1 " //数据库连接校验sql
		);
	}
	@Test
	public void testDB2ES() throws SQLException {
		initDBSource();
		try {
			ElasticSearchHelper.getRestClientUtil().dropIndice("dbdemo");
		}
		catch (Exception e){

		}
		SQLExecutor.queryByNullRowHandler(new ResultSetHandler() {
			@Override
			public void handleResult(ResultSet resultSet, StatementInfo statementInfo) throws Exception {
				ESJDBC esjdbcResultSet = new ESJDBC();
				esjdbcResultSet.setResultSet(resultSet);
				esjdbcResultSet.setMetaData(statementInfo.getMeta());
				JDBCRestClientUtil jdbcRestClientUtil = new JDBCRestClientUtil();
				jdbcRestClientUtil.addDocuments("dbdemo","dbdemo",esjdbcResultSet,"refresh",98);
			}
		},"select * from td_sm_log");

		long dbcount = SQLExecutor.queryObject(long.class,"select count(*) from td_sm_log");
		long escount = ElasticSearchHelper.getRestClientUtil().countAll("dbdemo");
		System.out.println("dbcount:"+dbcount+","+"escount:"+escount);

	}

	/**
	 * 从外部application.properties文件中加载数据源配置和es配置
	 */
	@Test
	public void testSimpleLogImportBuilderFromExternalDBConfig(){
		ImportBuilder importBuilder = ImportBuilder.newInstance();
		try {
			//清除测试表,导入的时候回重建表，测试的时候加上为了看测试效果，实际线上环境不要删表
			ElasticSearchHelper.getRestClientUtil().dropIndice("dbdemo");
		}
		catch (Exception e){

		}


		//指定导入数据的sql语句，必填项，可以设置自己的提取逻辑
		importBuilder.setSql("select * from td_sm_log");
		/**
		 * es相关配置
		 */
		importBuilder
				.setIndex("dbdemo") //必填项
				.setIndexType("dbdemo") //必填项
				.setRefreshOption("refresh")//可选项，null表示不实时刷新，importBuilder.setRefreshOption("refresh");表示实时刷新
				.setUseJavaName(true) //可选项,将数据库字段名称转换为java驼峰规范的名称，例如:doc_id -> docId
				.setBatchSize(50);  //可选项,批量导入es的记录数，默认为-1，逐条处理，> 0时批量处理


		/**
		 * 一次、作业创建一个内置的线程池，实现多线程并行数据导入elasticsearch功能，作业完毕后关闭线程池
		 */
		importBuilder.setParallel(true);//设置为多线程并行批量导入
		importBuilder.setQueue(10);//设置批量导入线程池等待队列长度
		importBuilder.setThreadCount(50);//设置批量导入线程池工作线程数量
		importBuilder.setContinueOnError(true);//任务出现异常，是否继续执行作业：true（默认值）继续执行 false 中断作业执行 
		importBuilder.setAsyn(false);//true 异步方式执行，不等待所有导入作业任务结束，方法快速返回；false（默认值） 同步方式执行，等待所有导入作业任务结束，所有作业结束后方法才返回
		importBuilder.setEsIdField("log_id");
		importBuilder.setDebugResponse(false);//设置是否将每次处理的reponse打印到日志文件中，默认false，不打印响应报文将大大提升性能，只有在需要的时候才，log日志级别同时要设置为INFO
//		importBuilder.setDiscardBulkResponse(true);//设置是否需要批量处理的响应报文，不需要设置为false，true为需要，默认true，如果不需要响应报文将大大提升处理速度
		/**
		 * 执行数据库表数据导入es操作
		 */
		DataStream dataStream = importBuilder.builder();
		dataStream.db2es();
		
		long count = ElasticSearchHelper.getRestClientUtil().countAll("dbdemo");
		try {
			Long dbcount = SQLExecutor.queryObject(Long.class, "select count(1) from (" + importBuilder.getSql()+ ") b");
			System.out.println("数据库记录数dbcount:" + dbcount);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("数据导入完毕后索引表dbdemo中的文档数量:"+count);
	}

	 

	@Test
	public void testDB2ESClob() throws SQLException {
		initDBSource();
		try {
			ElasticSearchHelper.getRestClientUtil().dropIndice("dbclobdemo");
		}
		catch (Exception e){

		}

		SQLExecutor.queryByNullRowHandler(new ResultSetHandler() {
			@Override
			public void handleResult(ResultSet resultSet, StatementInfo statementInfo) throws Exception {
				ESJDBC esjdbcResultSet = new ESJDBC();
				esjdbcResultSet.setResultSet(resultSet);
				esjdbcResultSet.setMetaData(statementInfo.getMeta());
				JDBCRestClientUtil jdbcRestClientUtil = new JDBCRestClientUtil();
				jdbcRestClientUtil.addDocuments("dbclobdemo","dbclobdemo",esjdbcResultSet,"refresh",1000);
			}
		},"select * from td_cms_document");

		long dbcount = SQLExecutor.queryObject(long.class,"select count(*) from td_cms_document");
		long escount = ElasticSearchHelper.getRestClientUtil().countAll("dbclobdemo");
		System.out.println("dbcount:"+dbcount+","+"escount:"+escount);

	}
	@Test
	public void testImportBuilder() throws SQLException {
		ImportBuilder importBuilder = ImportBuilder.newInstance();
		try {
			//清除测试表
			ElasticSearchHelper.getRestClientUtil().dropIndice("dbclobdemo");
		}
		catch (Exception e){

		}
		//数据源相关配置，可选项，可以在外部启动数据源
		importBuilder.setDbName("test")
				.setDbDriver("com.mysql.jdbc.Driver") //数据库驱动程序，必须导入相关数据库的驱动jar包
				.setDbUrl("jdbc:mysql://localhost:3306/bboss")
				.setDbUser("root")
				.setDbPassword("123456")
				.setValidateSQL("select 1")
				.setUsePool(true);//是否使用连接池


		//指定导入数据的sql语句，必填项，可以设置自己的提取逻辑
		importBuilder.setSql("select * from td_cms_document");
		/**
		 * es相关配置
		 */
		importBuilder
				.setIndex("dbclobdemo") //必填项,索引表名称
				.setIndexType("dbclobdemo") //必填项，索引类型
				.setRefreshOption("refresh")//可选项，null表示不实时刷新，importBuilder.setRefreshOption("refresh");
				.setUseJavaName(true) //可选项,将数据库字段名称转换为java驼峰规范的名称，例如:doc_id -> docId
				.setEsIdField("document_id")//可选项,设置文档id对应的数据库字段
				.setEsParentIdField(null) //可选项,如果不指定，es自动为文档产生id
				.setRoutingValue(null) //可选项		importBuilder.setRoutingField(null);
				.setEsDocAsUpsert(true)//可选项 新增/更新标识，具体看es官方文档
				.setEsRetryOnConflict(3)//可选项
				.setEsReturnSource(false)//可选项
				.setEsVersionField(null)//可选项 乐观锁版本号对应的数据库字段
				.setEsVersionType(null)//可选项 乐观锁版本类型，具体值看es官方文档
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") //可选项,默认日期格式
				.setLocale("zh_CN")  //可选项,默认locale
				.setTimeZone("Etc/UTC")  //可选项,默认时区
				.setBatchSize(1000);  //可选项,批量导入es的记录数，默认为-1，逐条处理，> 0时批量处理

		/**
		 * db-es mapping 表字段名称到es 文档字段的映射：比如document_id -> docId
		 * 可以配置mapping，也可以不配置，默认基于java 驼峰规则进行db field-es field的映射和转换
		 */
		importBuilder.addFieldMapping("document_id","docId")
					 .addFieldMapping("docwtime","docwTime")
					 .addIgnoreFieldMapping("channel_id");//添加忽略字段

		importBuilder.setDebugResponse(false);//设置是否将每次处理的reponse打印到日志文件中，默认false
		importBuilder.setDiscardBulkResponse(true);//设置是否需要批量处理的响应报文，不需要设置为false，true为需要，默认false

		/**
		 * 为每条记录添加额外的字段和值
		 * 可以为基本数据类型，也可以是复杂的对象
		 */
		importBuilder.addFieldValue("testF1","f1value");
		importBuilder.addFieldValue("testInt",0);
		importBuilder.addFieldValue("testDate",new Date());
		importBuilder.addFieldValue("testFormateDate","yyyy-MM-dd HH",new Date());
		TestObject testObject = new TestObject();
		testObject.setId("testid");
		testObject.setName("jackson");
		importBuilder.addFieldValue("testObject",testObject);

		/**
		 * 重新设置es数据结构
		 */
		importBuilder.setDataRefactor(new DataRefactor() {
			public void refactor(Context context) throws Exception  {
				CustomObject customObject = new CustomObject();
				customObject.setAuthor((String)context.getValue("author"));
				customObject.setTitle((String)context.getValue("title"));
				customObject.setSubtitle((String)context.getValue("subtitle"));
				context.addFieldValue("docInfo",customObject);//如果还需要构建更多的内部对象，可以继续构建

				//上述三个属性已经放置到docInfo中，如果无需再放置到索引文档中，可以忽略掉这些属性
				context.addIgnoreFieldMapping("author");
				context.addIgnoreFieldMapping("title");
				context.addIgnoreFieldMapping("subtitle");
			}
		});

		/**
		 * 执行数据库表数据导入es操作
		 */
		DataStream dataStream = importBuilder.builder();
		dataStream.db2es();
		long dbcount = SQLExecutor.queryObject(long.class,"select count(*) from td_cms_document");
		ESDatas<Map> datas = ElasticSearchHelper.getRestClientUtil().searchAll("dbclobdemo",Map.class);

		long escount = ElasticSearchHelper.getRestClientUtil().countAll("dbclobdemo");
		System.out.println("dbclobdemo:"+dbcount+","+"dbclobdemo:"+escount);
	}

	@Test
	public void testSimpleImportBuilder(){
		ImportBuilder importBuilder = ImportBuilder.newInstance();
		try {
			//清除测试表
			ElasticSearchHelper.getRestClientUtil().dropIndice("dbclobdemo");
		}
		catch (Exception e){

		}
		//数据源相关配置，可选项，可以在外部启动数据源
		importBuilder.setDbName("test")
				.setDbDriver("com.mysql.jdbc.Driver") //数据库驱动程序，必须导入相关数据库的驱动jar包
				.setDbUrl("jdbc:mysql://localhost:3306/bboss")
				.setDbUser("root")
				.setDbPassword("123456")
				.setValidateSQL("select 1")
				.setUsePool(false);//是否使用连接池


		//指定导入数据的sql语句，必填项，可以设置自己的提取逻辑
		importBuilder.setSql("select * from td_cms_document");
		/**
		 * es相关配置
		 */
		importBuilder
				.setIndex("dbclobdemo") //必填项
				.setIndexType("dbclobdemo") //必填项
				.setRefreshOption(null)//可选项，null表示不实时刷新，importBuilder.setRefreshOption("refresh");表示实时刷新
				.setUseJavaName(true) //可选项,将数据库字段名称转换为java驼峰规范的名称，例如:doc_id -> docId
				.setBatchSize(1000);  //可选项,批量导入es的记录数，默认为-1，逐条处理，> 0时批量处理


		/**
		 * 执行数据库表数据导入es操作
		 */
		DataStream dataStream = importBuilder.builder();
		dataStream.db2es();
	}

	/**
	 * 从外部application.properties文件中加载数据源配置和es配置
	 */
	@Test
	public void testSimpleImportBuilderFromExternalDBConfig(){
		ImportBuilder importBuilder = ImportBuilder.newInstance();
		try {
			//清除测试表
			ElasticSearchHelper.getRestClientUtil().dropIndice("dbclobdemo");
		}
		catch (Exception e){

		}


		//指定导入数据的sql语句，必填项，可以设置自己的提取逻辑
		importBuilder.setSql("select * from td_cms_document");
		/**
		 * es相关配置
		 */
		importBuilder
				.setIndex("dbclobdemo") //必填项
				.setIndexType("dbclobdemo") //必填项
				.setRefreshOption(null)//可选项，null表示不实时刷新，importBuilder.setRefreshOption("refresh");表示实时刷新
				.setUseJavaName(true) //可选项,将数据库字段名称转换为java驼峰规范的名称，例如:doc_id -> docId
				.setBatchSize(1);  //可选项,批量导入es的记录数，默认为-1，逐条处理，> 0时批量处理

		importBuilder.setParallel(true);
		importBuilder.setThreadCount(10);
		/**
		 * 执行数据库表数据导入es操作
		 */
		DataStream dataStream = importBuilder.builder();
		dataStream.db2es();
	}
}
