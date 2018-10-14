#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from datetime import datetime
from elasticsearch_dsl import DocType, Date, Nested, Boolean, \
    analyzer, InnerObjectWrapper, Completion, Keyword, Text, Float, Integer

from DoubanSpider import settings

from elasticsearch_dsl.connections import connections
connections.create_connection(hosts=settings.ELASTICSEARCH_HOST)

from elasticsearch_dsl.analysis import CustomAnalyzer

class CustomAnalyzer(CustomAnalyzer):
    def get_analysis_definition(self):
        return {}

ik_analyzer = CustomAnalyzer("ik_max_word", filter=["lowercase"])

class DoubanMovieType(DocType):
    suggest = Completion(analyzer=ik_analyzer)

    md5_url_id = Keyword()
    serial_number = Integer()
    name = Text(analyzer="ik_max_word")
    font_image_url = Keyword()
    font_image_path = Keyword()
    director = Text(analyzer="ik_max_word")
    actor = Text(analyzer="ik_max_word")
    type = Text(analyzer="ik_max_word")
    release_date = Keyword()
    runtime = Keyword()
    description = Text(analyzer="ik_max_word")
    star = Float()
    evaluation = Integer()

    class Meta:
        index = "douban"
        doc_type = "movie"

if __name__ == "__main__":
    DoubanMovieType.init()

