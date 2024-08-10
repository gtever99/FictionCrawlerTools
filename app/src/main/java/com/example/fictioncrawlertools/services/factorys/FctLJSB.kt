package com.example.fictioncrawlertools.services.factorys

import com.example.fictioncrawlertools.enums.FictionWebType
import com.example.fictioncrawlertools.services.FctBase
import com.example.fictioncrawlertools.services.models.SearchModel
import java.net.URLEncoder


/**
 * 六九书吧小说搜索
 */
class FctLJSB : FctBase(FictionWebType.LJSB) {

    override suspend fun searchFiction(keyword: String): MutableList<SearchModel> {
        val gwh = getWebHtml("${baseUrl}/modules/article/search.php", mapOf(
            "submit" to "Search",
            "searchkey" to URLEncoder.encode(keyword, "GBK")
        ))
        val searchList  = mutableListOf<SearchModel>()
        if (gwh == null) return searchList

        val paragraphs = gwh.select(".newbox ul li")
        for (paragraph in paragraphs) {
            searchList.add(SearchModel(
                title = paragraph.select("h3 a")[1].text(),
                introduction = paragraph.select(".ellipsis_2").text(),
                link = paragraph.getElementsByTag("a").attr("href"),
                label = paragraph.select(".labelbox label").text(),
                cover = baseUrl + paragraph.select(".imgbox img").attr("src")
            ))
        }
        return searchList
    }
}
