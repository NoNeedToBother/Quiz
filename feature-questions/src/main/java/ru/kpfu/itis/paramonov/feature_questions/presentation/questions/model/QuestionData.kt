package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model

import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.DifficultyUiModel

class QuestionData {

    private var _answers: MutableList<AnswerData> = mutableListOf()

    val text: String

    val difficulty: DifficultyUiModel?
    val category: CategoryUiModel?

    constructor(question: QuestionDataUiModel) {
        val correct = question.answer
        val incorrect = question.incorrectAnswers
        _answers.add(AnswerData(correct, chosen = false, correct = true))
        for (incorrectAnswer in incorrect) {
            _answers.add(AnswerData(incorrectAnswer, chosen = false, correct = false))
        }
        _answers = ArrayList(_answers.shuffled())
        text = question.text
        difficulty = question.difficulty
        category = question.category
    }

    constructor(text: String, answers: List<AnswerData>, difficulty: DifficultyUiModel?, category: CategoryUiModel?) {
        this.text = text
        _answers.addAll(answers)
        this.difficulty = difficulty
        this.category = category
    }

    val answers: List<AnswerData> get() = _answers
}