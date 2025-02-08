package ru.kpfu.itis.paramonov.questions.api.usecase

interface GetMaxScoreUseCase {

    suspend operator fun invoke(): Double

}
