package ru.kpfu.itis.paramonov.network.internal.data.model

import com.google.gson.annotations.SerializedName

internal data class CategoryResponse(
    @SerializedName("trivia_categories") val info: List<CategoryInfoResponse>
)