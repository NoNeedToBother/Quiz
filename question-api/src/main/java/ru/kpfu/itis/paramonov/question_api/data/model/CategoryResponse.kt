package ru.kpfu.itis.paramonov.question_api.data.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("trivia_categories") val info: List<CategoryInfoResponse>
)