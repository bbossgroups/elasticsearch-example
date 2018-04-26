#基于bboss es booter demo 的maven工程
本实例是一个bboss es booter的demo maven工程，可供spring boot项目集成参考

bboss elasticsearch开发视频教程 ,百度网盘免密下载： 

https://pan.baidu.com/s/1kXjAOKn 
# spring boot工程集成说明
## 在spring boot工程中导入以下坐标：

maven坐标
```
<dependency>
    <groupId>com.bbossgroups.plugins</groupId>
    <artifactId>bboss-elasticsearch-rest-booter</artifactId>
    <version>5.0.6.1</version>
</dependency>
```
gradle坐标
```
compile "com.bbossgroups.plugins:bboss-elasticsearch-rest-booter:5.0.6.1"
```
## 在spring booter中配置es参数
修改spring booter项目的application.properties文件，加入以下内容：
```
elasticUser=elastic
elasticPassword=changeme

#elasticsearch.rest.hostNames=10.1.236.88:9200
elasticsearch.rest.hostNames=127.0.0.1:9200
#elasticsearch.rest.hostNames=10.21.20.168:9200
#elasticsearch.rest.hostNames=10.180.211.27:9280,10.180.211.27:9281,10.180.211.27:9282
elasticsearch.dateFormat=yyyy.MM.dd
elasticsearch.timeZone=Asia/Shanghai
elasticsearch.ttl=2d
#在控制台输出脚本调试开关showTemplate,false关闭，true打开，同时log4j至少是info级别
elasticsearch.showTemplate=true
elasticsearch.discoverHost=true

http.timeoutConnection = 400000
http.timeoutSocket = 400000
http.connectionRequestTimeout=400000
http.retryTime = 1
http.maxLineLength = -1
http.maxHeaderCount = 200
http.maxTotal = 400
http.defaultMaxPerRoute = 200
```
配置项中，只有elasticsearch.rest.hostNames是必选项，其他可选，详细的配置说明，参考文档：
https://my.oschina.net/bboss/blog/1556866

## demo功能说明：

## 2.1 定义实体对象Demo

## 2.2 编写测试功能对象-DocumentCRUD.java
   创建dsl配置文件-esmapper/demo.xml
   
   删除和创建mapping
   
   单文档添加/修改/删除
   
   文档批量添加(elasticsearch不是实时flush记录，所以批量添加后如果立即查询可能查询不到刚添加的记录)
   
   文档批量修改(elasticsearch不是实时flush记录，所以批量修改后如果立即查询可能查询不到刚修改的效果)
   
   文档检索
   
## 2.3 参考文档
https://my.oschina.net/bboss/blog/1556866

## 开发交流群
166471282
  