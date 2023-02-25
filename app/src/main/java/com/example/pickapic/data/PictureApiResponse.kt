package com.example.pickapic.data

data class PictureApiResponse(
    val total: Int,
    val total_pages: Int,
    val results: List<Result>
)

data class Result(
    val urls: Urls
)

data class Urls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
)