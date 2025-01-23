package ru.kpfu.itis.paramonov.network.internal.data.model

import com.google.gson.annotations.SerializedName

internal data class QuestionResponse(
    @SerializedName("response_code") val code: Int?,
    @SerializedName("results") val questions: List<QuestionDataResponse>?
)