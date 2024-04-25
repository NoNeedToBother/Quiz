package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model

class QuestionData {

    private var _answers: MutableList<AnswerData> = mutableListOf()

    val text: String

    constructor(question: QuestionDataUiModel) {
        val correct = question.answer
        val incorrect = question.incorrectAnswers
        _answers.add(AnswerData(correct, chosen = false, correct = true))
        for (incorrectAnswer in incorrect) {
            _answers.add(AnswerData(incorrectAnswer, chosen = false, correct = false))
        }
        _answers = ArrayList(_answers.shuffled())
        text = question.text
    }

    constructor(text: String, answers: List<AnswerData>) {
        this.text = text
        _answers.addAll(answers)
    }

    val answers: List<AnswerData> get() = _answers
}