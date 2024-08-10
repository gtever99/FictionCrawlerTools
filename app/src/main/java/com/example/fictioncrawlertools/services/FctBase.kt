package com.example.fictioncrawlertools.services

import android.util.Log
import com.example.fictioncrawlertools.enums.FictionWebType
import com.example.fictioncrawlertools.services.models.SearchModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

abstract class FctBase constructor(
    webType: FictionWebType
) {
    var baseUrl: String = ""

    init {
        baseUrl = webType.url;
    }

    /**
     * 搜索小说
     * @param keyword 搜索关键词
     * @return SearchModel
     */
    abstract suspend fun searchFiction(keyword: String) : MutableList<SearchModel>

    /**
     * 获取页面内容 get
     * @param path String
     */
    suspend fun getWebHtml(path: String): Document {
        val getUrl = "${baseUrl}${path}"
        Log.v("获取页面内容", "开始获取，url：${getUrl}")

        val document = Jsoup.connect("https://69shuba.cx/book/58953/").headers(
            mapOf(
                "User-Agent" to "Mozilla / 5.0(Windows NT 10.0; Win64; x64) AppleWebKit / 537.36(KHTML, like Gecko) Chrome / 80.0.3987.122  Safari / 537.36"
            )
        ).get()

        Log.v("获取页面内容", "请求结束！")
        return document;
    }

    /**
     * 获取页面内容 post 带参数
     * @param path String
     */
    suspend fun getWebHtml(path: String, formData: Map<String, String>) : Document? {
        val docRes : Document?
        var retryCount = 0
        while (true) {
            val res = getWebHtmlCore(path, formData)
            if (res !== null || retryCount >= 10) {
                docRes = res
                break
            }
            retryCount ++
            Log.w("获取页面内容", "请求错误，正在重试，重试次数：${retryCount}")
        }
        return docRes;
    }
    private suspend fun getWebHtmlCore(path: String, formData: Map<String, String>) : Document? {
        Log.v("获取页面内容", "开始获取，url：${path}，请求参数：${formData}")
        try {
            val document = Jsoup.connect(path).headers(
                mapOf(
                    "User-Agent" to "Mozilla / 5.0(Windows NT 10.0; Win64; x64) AppleWebKit / 537.36(KHTML, like Gecko) Chrome / 80.0.3987.122  Safari / 537.36"
                )
            ).data(formData).post()
            Log.v("获取页面内容", "请求成功，url：${path}")
            return document
        } catch (e : Exception)  {
            println("错误消息：${e}")
        }
        return null;
    }
}