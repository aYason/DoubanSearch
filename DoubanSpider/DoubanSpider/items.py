# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://doc.scrapy.org/en/latest/topics/items.html
import re

import scrapy
from scrapy.loader import ItemLoader
from scrapy.loader.processors import TakeFirst, Join, MapCompose

from DoubanSpider.models.es_type import DoubanMovieType

from DoubanSpider import settings

from elasticsearch_dsl.connections import connections
es = connections.create_connection(hosts=settings.ELASTICSEARCH_HOST)

from DoubanSpider.utils.common import extract_num, remove_seq


class DoubanspiderItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    pass


def gen_suggests(index, info_tuple):
    # 根据字符串生成搜索建议数组
    used_words = set()
    suggests = []
    for text, weight in info_tuple:
        if text:
            words = es.indices.analyze(index=index, analyzer="ik_max_word", params={'filter': ["lowercase"]}, body=text)
            analyzed_words = set([r["token"] for r in words["tokens"] if len(r["token"]) > 1])
            new_words = analyzed_words - used_words
        else:
            new_words = set()

        if new_words:
            suggests.append({"input": list(new_words), "weight": weight})

    return suggests


class DoubanMovieItemLoader(ItemLoader):
    # 自定义itemloader
    default_output_processor = Join("/")


def return_value(value):
    return value


def get_chinese_name(value):
    match_re = re.match("^(\S+)\s.*", value)
    if match_re:
        chinese_name = match_re.group(1)
    else:
        chinese_name = value
    return chinese_name


class DoubanMovieItem(scrapy.Item):
    # 唯一标志
    md5_url_id = scrapy.Field()
    # 排名
    serial_number = scrapy.Field(
        input_processor=MapCompose(extract_num)
    )
    # 名称
    name = scrapy.Field(
        input_processor=MapCompose(get_chinese_name)
    )
    # 封面
    front_image_url = scrapy.Field(
        output_processor=MapCompose(return_value)
    )
    front_image_path = scrapy.Field()
    # 导演
    director = scrapy.Field()
    # 主演
    actor = scrapy.Field()
    # 类型
    type = scrapy.Field()
    # 上映日期
    release_date = scrapy.Field()
    # 片长
    runtime = scrapy.Field()
    # 剧情简介
    description = scrapy.Field(
        input_processor=MapCompose(remove_seq),
        output_processor=Join("")
    )
    # 豆瓣评分
    star = scrapy.Field()
    # 评价数
    evaluation = scrapy.Field()

    def get_insert_sql(self):
        insert_sql = """
                    insert into douban_movie(md5_url_id, serial_number, name, front_image_url, front_image_path, director, actor, type, release_date, runtime, description, star, evaluation)
                    values(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);
                """
        params = (
            self['md5_url_id'],
            self['serial_number'],
            self['name'],
            self['front_image_url'],
            self['front_image_path'],
            self['director'],
            self['actor'],
            self['type'],
            self['release_date'],
            self['runtime'],
            self['description'],
            self['star'],
            self['evaluation'],
        )
        return insert_sql, params

    def save_to_es(self):
        movie = DoubanMovieType()
        movie.meta.id = self['md5_url_id']
        movie.serial_number = self['serial_number']
        movie.name = self['name']
        movie.front_image_url = self['front_image_url']
        if "front_image_path" in self:
            movie.front_image_path = self["front_image_path"]
        movie.director = self['director']
        movie.actor = self['actor']
        movie.type = self['type']
        movie.release_date = self['release_date']
        movie.runtime = self['runtime']
        movie.description = self['description']
        movie.star = self['star']
        movie.evaluation = self['evaluation']

        movie.suggest = gen_suggests(DoubanMovieType._doc_type.index,
                                     ((movie.name, 10),
                                      (movie.description, 7),
                                      (movie.type, 3),
                                      (movie.director, 3),
                                      (movie.actor, 3)))
        movie.save()
        return
