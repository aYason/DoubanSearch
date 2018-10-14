# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html
from scrapy.pipelines.images import ImagesPipeline
import MySQLdb
from twisted.enterprise import adbapi

from DoubanSpider import settings


class DoubanspiderPipeline(object):
    def process_item(self, item, spider):
        return item


class ElasticsearchPipeline(object):
    def process_item(self, item, spider):
        item.save_to_es()
        return item


class MovieImagePipeline(ImagesPipeline):
    def item_completed(self, results, item, info):
        if "front_image_url" in item:
            for ok, value in results:
                image_file_path = value["path"]
                item["front_image_path"] = image_file_path
                item["front_image_url"] = item["front_image_url"][0]
        return item


class MysqlTwistedPipline(object):
     def __init__(self):
         dbparms = dict(
             host=settings.MYSQL_HOST,
             db=settings.MYSQL_DBNAME,
             user=settings.MYSQL_USER,
             passwd=settings.MYSQL_PASSWORD,
             charset='utf8',
             use_unicode=True,
         )
         dbpool = adbapi.ConnectionPool("MySQLdb", **dbparms)
         self.dbpool = dbpool

     def process_item(self, item, spider):
         # 使用twisted将mysql插入变成异步执行
         query = self.dbpool.runInteraction(self.do_insert, item)
         query.addErrback(self.handle_error, item, spider)  # 处理异常
         return item

     def handle_error(self, failure, item, spider):
         # 处理异步插入的异常
         print(failure)

     def do_insert(self, cursor, item):
         # 执行具体的插入
         # 根据不同的item 构建不同的sql语句并插入到mysql中
         insert_sql, params = item.get_insert_sql()
         cursor.execute(insert_sql, params)


# class MysqlPipeline(object):
#     def __init__(self):
#         self.conn = MySQLdb.connect(host='127.0.0.1',
#                                     user='root',
#                                     password='',
#                                     database='douban_spider',
#                                     charset="utf8",
#                                     use_unicode=True)
#         self.cursor = self.conn.cursor()
#
#     def process_item(self, item, spider):
#         insert_sql = """
#             insert into douban_movie(md5_url_id, serial_number, name, front_image_url, front_image_path, director, actor, type, release_date, runtime, description, star, evaluation)
#             values(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);
#         """
#         self.cursor.execute(insert_sql, (
#                             item['md5_url_id'],
#                             item['serial_number'],
#                             item['name'],
#                             item['front_image_url'],
#                             item['front_image_path'],
#                             item['director'],
#                             item['actor'],
#                             item['type'],
#                             item['release_date'],
#                             item['runtime'],
#                             item['description'],
#                             item['star'],
#                             item['evaluation'],
#         ))
#         self.conn.commit()
#         return item