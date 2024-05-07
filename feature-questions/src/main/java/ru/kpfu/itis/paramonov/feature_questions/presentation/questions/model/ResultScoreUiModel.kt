package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model

enum class ResultScoreUiModel(
    var gradation: GradationUiModel
) {

    A(GradationUiModel.DEFAULT),
    B(GradationUiModel.DEFAULT),
    C(GradationUiModel.DEFAULT),
    D(GradationUiModel.DEFAULT),
    E(GradationUiModel.DEFAULT),
    F(GradationUiModel.DEFAULT);



    enum class GradationUiModel {
        PLUS, MINUS, DEFAULT
    }
}