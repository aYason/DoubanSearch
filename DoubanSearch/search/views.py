import json

import MySQLdb
from django.http import HttpResponse
from django.views import View
from search.models import DoubanMovieType

from elasticsearch import Elasticsearch

client = Elasticsearch()


# 搜索建议
class Suggest(View):
    def get(self, request):
        key_words = request.GET.get('k', '')
        re_datas = []
        if key_words:
            s = DoubanMovieType.search()
            s = s.suggest('movie_suggestions',
                          key_words,
                          completion={
                              "field": "suggest",
                              "fuzzy": {
                                  "fuzziness": "AUTO"
                              },
                              "size": 5
                          })
            suggestions = s.execute_suggest()
            for match in suggestions.movie_suggestions[0].options:
                source = match._source
                re_datas.append(source['name'])
        return HttpResponse(json.dumps(re_datas), content_type="application/json;charset=utf-8")


# 搜索功能
class Search(View):
    def get(self, request):
        key_words = request.GET.get("k", "")
        page = request.GET.get("p", "1")
        try:
            page = int(page)
        except:
            page = 1

        hit_list = []
        if key_words:
            response = client.search(
                index="douban",
                body={
                    "query": {
                        "multi_match": {
                            "query": key_words,
                            "fields": ["name", "description", "type", "director", "actor"]
                        }
                    },
                    "from": (page - 1) * 10,
                    "size": 10,
                }
            )
            for hit in response['hits']['hits']:
                item_dict = {}
                item_dict["md5_url_id"] = hit['_id']
                item_dict["name"] = hit['_source']['name']
                item_dict["front_image_url"] = hit['_source']['front_image_url']
                item_dict["star"] = hit['_source']['star']
                item_dict["type"] = hit['_source']['type']
                item_dict["release_date"] = hit['_source']['release_date']
                hit_list.append(item_dict)

        return HttpResponse(json.dumps(hit_list), content_type="application/json;charset=utf-8")


# 电影查询
class Content(View):
    def __init__(self):
        self.conn = MySQLdb.connect(host='127.0.0.1',
                                    user='root',
                                    password='',
                                    database='douban_spider',
                                    charset="utf8",
                                    use_unicode=True)
        self.cursor = self.conn.cursor()

    def get(self, request):
        content_id = request.GET.get("id", "")
        item_dict = {}

        if content_id:
            select_sql = """
                        select * 
                        from douban_movie
                        where md5_url_id='%s';
                    """ % (content_id)
            self.cursor.execute(select_sql)
            results = self.cursor.fetchall()

            if len(results) != 0:
                results = results[0]
                item_dict['md5_url_id'] = results[0]
                item_dict['serial_number'] = results[1]
                item_dict['name'] = results[2]
                item_dict['front_image_url'] = results[3]
                item_dict['front_image_path'] = results[4]
                item_dict['director'] = results[5]
                item_dict['actor'] = results[6]
                item_dict['type'] = results[7]
                item_dict['release_date'] = results[8]
                item_dict['runtime'] = results[9]
                item_dict['description'] = results[10]
                item_dict['star'] = results[11]
                item_dict['evaluation'] = results[12]

        return HttpResponse(json.dumps(item_dict), content_type="application/json;charset=utf-8")

# 热门搜索 popular
# 搜索记录 history
