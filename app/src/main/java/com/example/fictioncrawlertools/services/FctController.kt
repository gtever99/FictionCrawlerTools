package com.example.fictioncrawlertools.services

import com.example.fictioncrawlertools.enums.FictionWebType
import com.example.fictioncrawlertools.services.factorys.FctLJSB
import com.example.fictioncrawlertools.services.models.SearchModel

class FctController {
    private val controllerMap = mapOf<FictionWebType, FctBase>(
        FictionWebType.LJSB to FctLJSB()
    )

    /**
     * 获取对应的处理类
     * @param webType FictionWebType
     * @return FctBase?
     */
    private fun getFactorys(webType: FictionWebType) : FctBase? {
        return controllerMap[webType]
    }

    suspend fun searchFiction(keyword: String): MutableList<SearchModel>? {
        return getFactorys(FictionWebType.LJSB)?.searchFiction(keyword)
    }
}