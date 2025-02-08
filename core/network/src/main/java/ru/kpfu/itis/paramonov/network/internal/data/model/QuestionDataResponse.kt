package ru.kpfu.itis.paramonov.network.internal.data.model

import com.google.gson.annotations.SerializedName

internal data class QuestionDataResponse(
    @SerializedName("question") val text: String?,
    @SerializedName("correct_answer") val answer: String?,
    @SerializedName("incorrect_answers") val incorrectAnswers: List<String>?
)
