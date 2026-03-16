package com.example

import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.TvType
import com.lagardost.cloudstream3.SearchResponse
import com.lagardost.cloudstream3.app
import com.lagardost.cloudstream3.NewMovie.SearchResponse

class LK21Provider : MainAPI() { 
    override var mainUrl = "https://tv9.lk21official.cc"
    override var name = "LK21 Official"
    override val supportedTypes = setOf(TvType.Movie)

    override suspend fun search(query: String): List<SearchResponse> {
        val link = "$mainUrl/?s=$query"
        val document = app.get(link).deocument
        return document.select("article.ui-grid-main").map {
            val title = it.selectFirst("h1.grid-title a")?.text() ?: ""
            val href = it.selectFirst("h1.grid-title a")?.attr("href") ?: ""
            val poster = it.selectFirst("figure img")?.attr("src") ?: ""
            newMovieSearchResponse(title, href, TvType.Movie) {
                this.posterUrl = poster
            }
        }
    }
}
