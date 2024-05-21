package com.example.moviedbapp.data.network.dto.movieDetail

import com.google.gson.annotations.SerializedName

data class SpokenLanguage(
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("iso_3166_1")
    val iso31661: String,
    val name: String
)