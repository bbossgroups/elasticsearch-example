package org.bboss.elasticsearchtest.textembedding;
/**
 * Copyright 2025 bboss
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
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.entity.MetaMap;
import org.frameworkset.spi.remote.http.HttpRequestProxy;
import org.frameworkset.tran.DataImportException;
import org.frameworkset.tran.textembedding.XinferenceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author biaoping.yin
 * @Date 2025/5/11
 */
public class TextEmbedding {
    private static Logger logger = LoggerFactory.getLogger(TextEmbedding.class);
    public void testCreateTextEmbeddingIndex(){
        //创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/textembedding.xml");
        clientUtil.dropIndice("collection-with-embeddings");
        clientUtil.createIndiceMapping("collection-with-embeddings","createTextEmbeddingIndex");
    }
    
     
    public void init(){
        Map properties = new HashMap();

        //embedding_model为的向量模型服务数据源名称
        properties.put("http.poolNames","embedding_model");

        properties.put("embedding_model.http.hosts","172.24.176.18:9997");///设置向量模型服务地址(这里调用的xinference发布的模型服务),多个地址逗号分隔，可以实现点到点负载和容灾

        properties.put("embedding_model.http.timeoutSocket","60000");
        properties.put("embedding_model.http.timeoutConnection","40000");
        properties.put("embedding_model.http.connectionRequestTimeout","70000");
        properties.put("embedding_model.http.maxTotal","100");
        properties.put("embedding_model.http.defaultMaxPerRoute","100");
        HttpRequestProxy.startHttpPools(properties);
    }
    
    public void bulkdata(){
        List<Map> datas = new ArrayList<>();
        for(int i =0 ; i < 100; i ++) {
            
            Map data = new LinkedHashMap();
            String content = null;
            if(i % 5 == 0){
                content = "bboss是什么呢";
                data.put("key","bboss");
            }
            else if(i % 5 == 1){
                content = "中国五月名山是什么呢";
                data.put("key","china");
            }
            else if(i % 5 == 2){
                content = "能不能写首诗呢";
                data.put("key","document");
            }
            else if(i % 5 == 3){
                content = "bboss基于Apache License开源协议，由开源社区bboss发起和维护，主要由以下四部分构成：\n" +
                        "\n" +
                        "Elasticsearch Highlevel Java Restclient ， 一个高性能高兼容性的Elasticsearch/Opensearch java客户端框架\n" +
                        "数据采集同步ETL ，一个基于java语言实现数据采集作业的强大ETL工具，提供丰富的输入插件和输出插件，可以基于插件规范轻松扩展新的输入插件和输出插件；支持数据向量化处理\n" +
                        "流批一体化计算框架，提供灵活的数据指标统计计算流批一体化处理功能的简易框架，可以结合数据采集同步ETL工具，实现数据流处理和批处理计算，亦可以独立使用；计算结果可以保存到各种关系数据库、分布式数据仓库Elasticsearch、Clickhouse等，特别适用于数据体量和规模不大的企业级数据分析计算场景，具有成本低、见效快、易运维等特点，助力企业降本增效。\n" +
                        "通用分布式作业调度工作流，提供通用流程编排模型，可以将各种需要按照先后顺序执行的任务编排成工作流，进行统一调度执行，譬如数据采集作业任务、流批处理作业任务、业务办理任务、充值缴费任务以及大模型推理任务等，按照顺序编排成工作流程；默认提供了数据交换和流批处理作业节点、通用函数节点以及复合类型节点（串/并行执行），可以按需自定义扩展新的流程节点、远程服务执行节点；可以为流程节点设置条件触发器，控制流程节点是否执行；通过流程上下文和节点上下文在节点间传递和共享参数；通过设置流程执行和节点执行监听器，维护流程和节点执行参数，采集和获取流程、节点执行监控指标以及执行异常信息。";
                data.put("key","flower");
            }
            else if(i % 5 == 4){
                content = "数据交换开源项目bboss";
                data.put("key","etl");
            }
            else{
                content = "elasticsearch knn searcher";
                data.put("key","knn");
            }
            data.put("text",content);
          
            //数据向量化处理
            float[] embedding = text2embedding(  content);
            if (embedding != null){
                data.put("text_embedding", embedding);//设置向量数据
            }
           
            datas.add(data);
            
        }
        //将向量数据批量写入向量表
        ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
        clientUtil.addDocuments("collection-with-embeddings",datas);
        
    }

    /**
     * 数据向量化处理方法
     * @param text
     * @return
     */
    private float[] text2embedding(String text){
        Map params = new HashMap();
        params.put("input", text);//设置将要向量化的数据
        params.put("model", "custom-bge-large-zh-v1.5");//指定Xinference向量模型id
        //                   {"input": ["\\u5411\\u91cf\\u8f6c\\u6362"], "model": "custom-bge-large-zh-v1.5", "encoding_format": "base64"}
//                   String params = "{\"input\": [\"\\u5411\\u91cf\\u8f6c\\u6362\"], \"model\": \"custom-bge-large-zh-v1.5\", \"encoding_format\": \"base64\"}";
//                   Headers({'host': '172.24.176.18:9997', 'accept-encoding': 'gzip, deflate, br', 'connection': 'keep-alive', 
//                   'accept': 'application/json', 'content-type': 'application/json', 'user-agent': 'OpenAI/Python 1.35.13', 
//                   'x-stainless-lang': 'python', 'x-stainless-package-version': '1.35.13', 'x-stainless-os': 'Windows', 
//                   'x-stainless-arch': 'other:amd64', 'x-stainless-runtime': 'CPython', 'x-stainless-runtime-version': '3.10.14', 
//                   'authorization': '[secure]', 'x-stainless-async': 'false', 
//                   'openai-organization': '', 'content-length': '105'})

        //调用向量服务，对数据进行向量化
        XinferenceResponse result = HttpRequestProxy.sendJsonBody("embedding_model", params, "/v1/embeddings", XinferenceResponse.class);
        if (result != null) {
            float[] embedding = result.embedding();
            return embedding;
        } else {
            throw new DataImportException("change LOG_CONTENT to vector failed:XinferenceResponse is null");
        }
    }
    
    public void search(){
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/textembedding.xml");
        Map params = new LinkedHashMap();
        //设置向量查询条件，调用数据向量化方法，将检索文本bboss转化为向量数据，默认最多返回10条数据
        params.put("condition",text2embedding("bboss"));       
        params.put("size",100);
        params.put("k",50);
        //返回MetaMap类型，为LinkHashMap的子类，但是包含索引记录元数据，元数据参考文档:https://esdoc.bbossgroups.com/#/document-crud?id=_62-%e5%b8%a6%e5%85%83%e6%95%b0%e6%8d%ae%e7%9a%84map%e5%af%b9%e8%b1%a1metamap%e4%bd%bf%e7%94%a8
        ESDatas<MetaMap> datas = clientUtil.searchList("/collection-with-embeddings/_search","search1",params, MetaMap.class);
        logger.info("datas.getTotalSize():"+datas.getTotalSize());//匹配条件的总记录数
        List<MetaMap> metaMaps = datas.getDatas();//返回的结果数据
        for(int i = 0; i < metaMaps.size(); i ++){
            MetaMap metaMap = metaMaps.get(i);
            logger.info("score: {}",metaMap.getScore());//相似度分数
            logger.info("text: {}",metaMap.get("text"));//检索的原始文本
            logger.info("key: {}",metaMap.get("key"));//检索的key字段值

        }
    }

    public void search1(){
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/textembedding.xml");
        Map params = new LinkedHashMap();
        //设置向量查询条件，调用数据向量化方法，将检索文本bboss转化为向量数据
        params.put("condition",text2embedding("bboss"));
        //设置返回top k条数据
        params.put("k",50);
        //设置Elasticsearch query size：最多返回记录数，需大于k参数值
        params.put("size",100);
        //返回MetaMap类型，为LinkHashMap的子类，但是包含索引记录元数据，元数据参考文档:https://esdoc.bbossgroups.com/#/document-crud?id=_62-%e5%b8%a6%e5%85%83%e6%95%b0%e6%8d%ae%e7%9a%84map%e5%af%b9%e8%b1%a1metamap%e4%bd%bf%e7%94%a8
        ESDatas<MetaMap> datas = clientUtil.searchList("/collection-with-embeddings/_search","search1",params, MetaMap.class);
        logger.info("datas.getTotalSize():"+datas.getTotalSize());//匹配条件的总记录数
        List<MetaMap> metaMaps = datas.getDatas();//返回的结果数据
        for(int i = 0; i < metaMaps.size(); i ++){
            MetaMap metaMap = metaMaps.get(i);
            logger.info("score: {}",metaMap.getScore());//相似度分数
            logger.info("text: {}",metaMap.get("text"));//检索的原始文本
            logger.info("key: {}",metaMap.get("key"));//检索的key字段值

        }
    }

    public void searchWithFilter(){
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/textembedding.xml");
        Map params = new LinkedHashMap();
        //设置向量查询条件，调用数据向量化方法，将检索文本bboss转化为向量数据
        params.put("condition",text2embedding("bboss"));
        //设置返回top k条数据
        params.put("k",50);
        //设置Elasticsearch query size：最多返回记录数，需大于k参数值
        params.put("size",100);
        //指定向量相似度阈值，不会返回向量检索相似度低于similarity值的记录
        params.put("similarity",0.5);
        //设置混合检索条件：指定key字段值条件
        params.put("key","bboss");
        ESDatas<MetaMap> datas = clientUtil.searchList("/collection-with-embeddings/_search","searchWithFilter",params, MetaMap.class);
        logger.info("datas.getTotalSize():"+datas.getTotalSize());
        List<MetaMap> metaMaps = datas.getDatas();
        for(int i = 0; i < metaMaps.size(); i ++){
            MetaMap metaMap = metaMaps.get(i);
            logger.info("score: {}",metaMap.getScore());//相似度分数
            logger.info("text: {}",metaMap.get("text"));//检索的原始文本
            logger.info("key: {}",metaMap.get("key"));//检索的key字段值

        }
    }
    
    public void searchWithScore(){
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/textembedding.xml");
        Map params = new LinkedHashMap();
        //设置向量查询条件，调用数据向量化方法，将检索文本bboss转化为向量数据
        params.put("condition",text2embedding("bboss"));
        //设置返回top k条数据
        params.put("k",50);
        //设置Elasticsearch query size：最多返回记录数，需大于k参数值
        params.put("size",100);
        //指定向量相似度阈值，不会返回向量检索相似度低于similarity值的记录
        params.put("similarity",0.5);
        ESDatas<MetaMap> datas = clientUtil.searchList("/collection-with-embeddings/_search","searchWithScore",params, MetaMap.class);
        logger.info("datas.getTotalSize():"+datas.getTotalSize());
//        logger.info("datas.getDatas():"+ SimpleStringUtil.object2json(datas.getDatas()));
        List<MetaMap> metaMaps = datas.getDatas();
        for(int i = 0; i < metaMaps.size(); i ++){
            MetaMap metaMap = metaMaps.get(i);
            logger.info("score: {}",metaMap.getScore());//相似度分数
            logger.info("text: {}",metaMap.get("text"));//检索的原始文本
            logger.info("key: {}",metaMap.get("key"));//检索的key字段值

        }
    }


    public void searchVectorAndRerank(){
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/textembedding.xml");
        Map params = new LinkedHashMap();
        //设置向量查询条件，调用数据向量化方法，将检索文本bboss转化为向量数据
        params.put("condition",text2embedding("bboss介绍"));
        //设置返回top k条数据
        params.put("k",50);
        //设置Elasticsearch query size：最多返回记录数，需大于k参数值
        params.put("size",100);
        //指定向量相似度阈值，不会返回向量检索相似度低于similarity值的记录
        params.put("similarity",0.5);
        ESDatas<MetaMap> datas = clientUtil.searchList("/collection-with-embeddings/_search","searchWithScore",params, MetaMap.class);
        logger.info("datas.getTotalSize():"+datas.getTotalSize());
//        logger.info("datas.getDatas():"+ SimpleStringUtil.object2json(datas.getDatas()));
        List<MetaMap> metaMaps = datas.getDatas();
        List rerankDatas = new ArrayList();
        for(int i = 0; i < metaMaps.size(); i ++){
            MetaMap metaMap = metaMaps.get(i);
            logger.info("score: {}",metaMap.getScore());//相似度分数
            logger.info("text: {}",metaMap.get("text"));//检索的原始文本
            rerankDatas.add(metaMap.get("text"));
            logger.info("key: {}",metaMap.get("key"));//检索的key字段值

        }
        
        //对检索结果进行Rerank处理
        Map rerankParams = new LinkedHashMap();
        rerankParams.put("model","bge-reranker-base");//指定基于Xinference部署的Rerank模型
        rerankParams.put("documents",rerankDatas);//根据问题进行向量检索返回的数据
        rerankParams.put("query","bboss介绍");//问题

        //调用Xinference Rerank服务，对问题的各个答案进行语义相关度排序
        Map reponse = HttpRequestProxy.sendJsonBody("embedding_model",rerankParams,"/v1/rerank",Map.class);
        logger.info(SimpleStringUtil.object2json(reponse));
    }


//        String requestBody =
//                "{\"model\": \"bge-reranker-base\", " +
//                        "\"documents\": [\"A man is eating food.\",  " +
//                        "\"A man is eating a piece of bread.\",  " +
//                        "\"The girl is carrying a baby.\", " +
//                        "\"A man is riding a horse.\",  " +
//                        "\"A woman is playing violin.\"],  " +
//                        "\"query\": \"A man is eating pasta.\"}";
}
