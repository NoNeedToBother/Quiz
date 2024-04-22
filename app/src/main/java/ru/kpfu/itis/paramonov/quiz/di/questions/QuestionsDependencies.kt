package ru.kpfu.itis.paramonov.quiz.di.questions

import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.HtmlDecoder

interface QuestionsDependencies {

    fun resourceManager(): ResourceManager

    fun htmlDecoder(): HtmlDecoder

}