package ru.kpfu.itis.paramonov.question_api.internal.data.model

import com.google.gson.annotations.SerializedName

internal data class CategoryResponse(
    @SerializedName("trivia_categories") val info: List<CategoryInfoResponse>
)