# -*- coding: utf-8 -*-
from urllib import parse

import scrapy
from scrapy.loader import ItemLoader

from DoubanSpider.items import DoubanMovieItem, DoubanMovieItemLoader
from DoubanSpider.utils.common import get_md5


class DoubanSpiderSpider(scrapy.Spider):
    # 爬虫名
    name = 'douban_spider'
    # 允许爬取的域名
    allowed_domains = ['movie.douban.com']
    # 入口url
    start_urls = ['https://movie.douban.com/top250']

    def parse(self, response):
        movie_list = response.xpath("//div[@class='article']//ol[@class='grid_view']/li")
        for movie_item in movie_list:
            post_url = movie_item.xpath(".//div[@class='hd']/a/@href").extract_first()
            yield scrapy.Request(url=parse.urljoin(response.url, post_url), callback=self.parse_detail)

        next_link = response.xpath("//span[@class='next']/a/@href").extract_first()
        if next_link:
            yield scrapy.Request(url=parse.urljoin(response.url, next_link), callback=self.parse)

    def parse_detail(self, response):
        item_loader = DoubanMovieItemLoader(item=DoubanMovieItem(), response=response)

        item_loader.add_value("md5_url_id", get_md5(response.url))
        item_loader.add_xpath("serial_number", "//div[@class='top250']/span[1]/text()")
        item_loader.add_xpath("name", "//div[@id='content']/h1/span[1]/text()")
        item_loader.add_xpath("front_image_url", "//div[@id='mainpic']/a/img/@src")
        item_loader.add_xpath("director", "//a[@rel='v:directedBy']/text()")
        item_loader.add_xpath("actor", "//a[@rel='v:starring']/text()")
        item_loader.add_xpath("type", "//span[@property='v:genre']/text()")
        item_loader.add_xpath("release_date", "//span[@property='v:initialReleaseDate']/text()")
        item_loader.add_xpath("runtime", "//span[@property='v:runtime']/text()")
        item_loader.add_xpath("description", "//div[@class='indent']//span[@property='v:summary']/text()")
        item_loader.add_xpath("star", "//div[@id='interest_sectl']//strong/text()")
        item_loader.add_xpath("evaluation", "//div[@class='rating_sum']/a/span/text()")

        movie_item = item_loader.load_item()
        yield movie_item