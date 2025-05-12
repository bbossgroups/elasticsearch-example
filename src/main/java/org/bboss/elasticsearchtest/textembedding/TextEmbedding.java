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
                content = "梅花几月份开呢";
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
          
            float[] embedding = text2embedding(  content);
            if (embedding != null){
                data.put("text_embedding", embedding);//设置向量数据
            }
           
            datas.add(data);
            
        }

        ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
        clientUtil.addDocuments("collection-with-embeddings",datas);
        
    }
    
    private float[] text2embedding(String text){
        Map params = new HashMap();
        params.put("input", text);
        params.put("model", "custom-bge-large-zh-v1.5");
        //                   {"input": ["\\u5411\\u91cf\\u8f6c\\u6362"], "model": "custom-bge-large-zh-v1.5", "encoding_format": "base64"}
//                   String params = "{\"input\": [\"\\u5411\\u91cf\\u8f6c\\u6362\"], \"model\": \"custom-bge-large-zh-v1.5\", \"encoding_format\": \"base64\"}";
//                   Headers({'host': '172.24.176.18:9997', 'accept-encoding': 'gzip, deflate, br', 'connection': 'keep-alive', 
//                   'accept': 'application/json', 'content-type': 'application/json', 'user-agent': 'OpenAI/Python 1.35.13', 
//                   'x-stainless-lang': 'python', 'x-stainless-package-version': '1.35.13', 'x-stainless-os': 'Windows', 
//                   'x-stainless-arch': 'other:amd64', 'x-stainless-runtime': 'CPython', 'x-stainless-runtime-version': '3.10.14', 
//                   'authorization': '[secure]', 'x-stainless-async': 'false', 
//                   'openai-organization': '', 'content-length': '105'})

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
        params.put("condition",text2embedding("bboss"));
        ESDatas<Map> datas = clientUtil.searchList("/collection-with-embeddings/_search","search",params,Map.class);
        logger.info("datas.getTotalSize():"+datas.getTotalSize());
        logger.info("datas.getDatas():"+ SimpleStringUtil.object2json(datas.getDatas()));
    }

    public void search1(){
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/textembedding.xml");
        Map params = new LinkedHashMap();
        params.put("condition",text2embedding("bboss"));
        params.put("k",50);
        params.put("size",100);
        ESDatas<MetaMap> datas = clientUtil.searchList("/collection-with-embeddings/_search","search1",params, MetaMap.class);
        logger.info("datas.getTotalSize():"+datas.getTotalSize());
        logger.info("datas.getDatas():"+ SimpleStringUtil.object2json(datas.getDatas()));
    }

    public void searchWithFilter(){
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/textembedding.xml");
        Map params = new LinkedHashMap();
        params.put("condition",text2embedding("bboss"));
        params.put("k",50);
        params.put("size",100);
        params.put("key","bboss");
        ESDatas<MetaMap> datas = clientUtil.searchList("/collection-with-embeddings/_search","searchWithFilter",params, MetaMap.class);
        logger.info("datas.getTotalSize():"+datas.getTotalSize());
//        logger.info("datas.getDatas():"+ SimpleStringUtil.object2json(datas.getDatas()));
        List<MetaMap> metaMaps = datas.getDatas();
        for(int i = 0; i < metaMaps.size(); i ++){
            MetaMap metaMap = metaMaps.get(i);
            logger.info("score: {}",metaMap.getScore());
            logger.info("text: {}",metaMap.get("text"));

        }
    }
    
    public void searchWithScore(){
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/textembedding.xml");
        Map params = new LinkedHashMap();
        params.put("condition",text2embedding("bboss"));
        params.put("k",50);
        params.put("size",100);

        params.put("similarity",0.5);
        ESDatas<MetaMap> datas = clientUtil.searchList("/collection-with-embeddings/_search","searchWithScore",params, MetaMap.class);
        logger.info("datas.getTotalSize():"+datas.getTotalSize());
//        logger.info("datas.getDatas():"+ SimpleStringUtil.object2json(datas.getDatas()));
        List<MetaMap> metaMaps = datas.getDatas();
        for(int i = 0; i < metaMaps.size(); i ++){
            MetaMap metaMap = metaMaps.get(i);
            logger.info("score: {}",metaMap.getScore());
            logger.info("text: {}",metaMap.get("text"));

        }
    }


    
    

}
