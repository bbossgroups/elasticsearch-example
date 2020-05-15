package org.bboss.elasticsearchtest.aws;
/**
 * Copyright 2020 bboss
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

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2020/5/13 17:35
 * @author biaoping.yin
 * @version 1.0
 */
public class AWSClient {
	public static void main(String[] args){
//		final CredentialsProvider credentialsProvider =
//				new BasicCredentialsProvider();
//		credentialsProvider.setCredentials(AuthScope.ANY,
//				new UsernamePasswordCredentials("elastic", "changeme"));
//
//		AWSCredentials credentials = new BasicAWSCredentials("", "");
//		AWS4Signer signer = new AWS4Signer();
//		AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(credentials);
//		signer.setServiceName("es");
//		signer.setRegionName("us-east-1");
//
////		final HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(
////				"es", signer, awsCredentialsProvider);
//		HttpHost[] httpHosts = new HttpHost[0];
////		for (int i = 0; i<httpHosts.length;i++){
////			Hosts host = elasticSearchProperty.getHosts().get(i);
////			httpHosts[i] = new HttpHost(host.getIp(), host.getPort());
////		}
//		final HttpRequestInterceptor interceptor = null;
//		new RestHighLevelClient(RestClient.builder(httpHosts).
//				setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
//					public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder hacb) {
//						return hacb.addInterceptorLast(interceptor).setDefaultCredentialsProvider(credentialsProvider);
//					}
//				}));
	}
}
