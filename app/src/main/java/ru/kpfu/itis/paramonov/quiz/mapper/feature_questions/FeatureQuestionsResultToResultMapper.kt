package ru.kpfu.itis.paramonov.quiz.mapper.feature_questions

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.questions.api.model.Result
import javax.inject.Inject

class FeatureQuestionsResultToResultMapper @Inject constructor(
    private val featureQuestionsUserToFirebaseUserMapper: FeatureQuestionsUserToFirebaseUserMapper
): ModelMapper<Result, ru.kpfu.itis.paramonov.firebase.external.domain.model.Result> {
    override fun map(model: Result): ru.kpfu.itis.paramonov.firebase.external.domain.model.Result {
        return ru.kpfu.itis.paramonov.firebase.external.domain.model.Result(
            user = featureQuestionsUserToFirebaseUserMapper.map(model.user),
            time = model.time,
            correct = model.correct,
            total = model.total,
            difficulty = model.difficulty,
            category = model.category,
            gameMode = model.gameMode
        )
    }
}