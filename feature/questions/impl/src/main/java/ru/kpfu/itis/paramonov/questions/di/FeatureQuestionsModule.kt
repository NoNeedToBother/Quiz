package ru.kpfu.itis.paramonov.questions.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindProvider
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.questions.api.usecase.GetMaxScoreUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.GetQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.GetQuestionsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.GetSavedQuestionsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.GetTrainingQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveQuestionsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveResultsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveTrainingQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionDataApiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionDataUiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionSettingsApiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionSettingsUiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionUiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetMaxScoreUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetQuestionSettingsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetQuestionsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetSavedQuestionsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetTrainingQuestionSettingsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.SaveQuestionSettingsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.SaveQuestionsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.SaveResultsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.SaveTrainingQuestionSettingsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel.QuestionsViewModel
import ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel.TrainingQuestionsViewModel
import ru.kpfu.itis.paramonov.questions.presentation.settings.viewmodel.QuestionSettingsViewModel

val featureQuestionsModule = DI {
    bind<QuestionDataApiModelMapper>() with provider { QuestionDataApiModelMapper() }
    bind<QuestionDataUiModelMapper>() with provider { QuestionDataUiModelMapper() }
    bind<QuestionSettingsUiModelMapper>() with provider { QuestionSettingsUiModelMapper() }
    bind<QuestionSettingsApiModelMapper>() with provider { QuestionSettingsApiModelMapper() }
    bind<QuestionUiModelMapper>() with provider { QuestionUiModelMapper() }

    bind<GetMaxScoreUseCase>() with provider {
        GetMaxScoreUseCaseImpl(instance(), instance())
    }
    bind<GetQuestionSettingsUseCase>() with provider {
        GetQuestionSettingsUseCaseImpl(instance(), instance())
    }
    bind<GetQuestionsUseCase>() with provider {
        GetQuestionsUseCaseImpl(instance(), instance(), instance())
    }
    bind<GetSavedQuestionsUseCase>() with provider {
        GetSavedQuestionsUseCaseImpl(instance(), instance(), instance())
    }
    bind<GetTrainingQuestionSettingsUseCase>() with provider {
        GetTrainingQuestionSettingsUseCaseImpl(instance(), instance())
    }
    bind<SaveQuestionSettingsUseCase>() with provider {
        SaveQuestionSettingsUseCaseImpl(instance(), instance())
    }
    bind<SaveQuestionsUseCase>() with provider {
        SaveQuestionsUseCaseImpl(instance(), instance(), instance())
    }
    bind<SaveResultsUseCase>() with provider {
        SaveResultsUseCaseImpl(instance(), instance(), instance(), instance())
    }
    bind<SaveTrainingQuestionSettingsUseCase>() with provider {
        SaveTrainingQuestionSettingsUseCaseImpl(instance(), instance())
    }

    bindProvider {
        QuestionsViewModel(
            getQuestionsUseCase = instance(),
            saveQuestionsUseCase = instance(),
            saveResultsUseCase = instance(),
            getQuestionSettingsUseCase = instance(),
            getMaxScoreUseCase = instance(),
            questionDataUiModelMapper = instance(),
            questionDataApiModelMapper = instance(),
            resourceManager = instance()
        )
    }
    bindProvider {
        TrainingQuestionsViewModel(
            getSavedQuestionsUseCase = instance(),
            questionDataUiModelMapper = instance(),
            resourceManager = instance()
        )
    }

    bindProvider {
        QuestionSettingsViewModel(
            getQuestionSettingsUseCase = instance(),
            saveQuestionSettingsUseCase = instance(),
            questionSettingsUiModelMapper = instance(),
            questionSettingsApiModelMapper = instance(),
            saveTrainingQuestionSettingsUseCase = instance(),
            getTrainingQuestionSettingsUseCase = instance(),
            resourceManager = instance()
        )
    }
}
