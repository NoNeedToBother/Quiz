package ru.kpfu.itis.paramonov.question_api.internal.data.model

import com.google.gson.annotations.SerializedName

internal data class QuestionResponse(
    @SerializedName("response_code") val code: Int?,
    @SerializedName("results") val questions: List<QuestionDataResponse>?
)