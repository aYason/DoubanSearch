#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import hashlib
import re


def get_md5(url):
    if isinstance(url, str):
        url = url.encode("utf-8")
    m = hashlib.md5()
    m.update(url)
    return m.hexdigest()


def extract_num(text):
    #从字符串中提取出数字
    match_re = re.match(".*?(\d+).*", text)
    if match_re:
        nums = match_re.group(1)
    else:
        nums = '0'
    return nums


def remove_seq(text):
    clear_text = "".join(text.split())
    return clear_text
