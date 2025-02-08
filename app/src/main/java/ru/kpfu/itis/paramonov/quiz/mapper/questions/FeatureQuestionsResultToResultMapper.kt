package ru.kpfu.itis.paramonov.quiz.mapper.questions

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.questions.api.model.Result

class FeatureQuestionsResultToResultMapper(
    private val featureQuestionsUserToFirebaseUserMapper: FeatureQuestionsUserToFirebaseUserMapper
): ModelMapper<Result, ru.kpfu.itis.paramonov.firebase.external.model.Result> {
    override fun map(model: Result): ru.kpfu.itis.paramonov.firebase.external.model.Result {
        return ru.kpfu.itis.paramonov.firebase.external.model.Result(
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
