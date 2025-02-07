package ru.kpfu.itis.paramonov.database.external.di

import android.content.Context
import android.content.SharedPreferences
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import ru.kpfu.itis.paramonov.database.external.domain.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.database.external.domain.repository.SavedQuestionRepository
import ru.kpfu.itis.paramonov.database.internal.data.room.database.QuestionDatabase
import ru.kpfu.itis.paramonov.database.internal.data.room.repository.SavedQuestionRepositoryImpl
import ru.kpfu.itis.paramonov.database.internal.data.shared_pref.repository.QuestionSettingsRepositoryImpl

val localDatabaseModule = DI.Module("LocalDatabase") {
    bind<String>(tag = "quiz_shared_pref_tag") with provider { "quiz_database" }
    bind<SharedPreferences>() with singleton {
        val context: Context = instance<Context>()
        val sharedPreferencesKey: String = instance(tag = "quiz_shared_pref_tag")
        context.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
    }
    bind<QuestionSettingsRepository>() with provider {
        QuestionSettingsRepositoryImpl(instance(), instance())
    }

    bind<String>(tag = "quiz_database_tag") with provider { "quiz" }
    bind<QuestionDatabase>() with singleton {
        val context: Context = instance()
        val name: String = instance(tag = "quiz_database_tag")
        QuestionDatabase.init(context, name)
    }
    bind<SavedQuestionRepository>() with provider {
        SavedQuestionRepositoryImpl(instance(), instance())
    }
}
