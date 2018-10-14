# DoubanSearch
基于 scrapy 和 elasticsearch 的电影类垂直搜索引擎 demo，数据源来自 doubanTop250

# Usage
启动 mysql and elasticsearch 服务
```
net start mysql

cd \elasticsearch-rtf-master\bin
elasticsearch.bat
```

在插件上查看 elasticsearch 集群信息
```
cd \tools\elasticsearch-head
npm run start

浏览器上访问 http://localhost:9100
```