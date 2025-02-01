package ru.kpfu.itis.paramonov.quiz.di.dependencies

import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.core.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.core.di.FeatureContainer
import ru.kpfu.itis.paramonov.core.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.authentication.di.FeatureAuthenticationDependencies
import ru.kpfu.itis.paramonov.authentication.di.FeatureAuthenticationHolder
import ru.kpfu.itis.paramonov.authentication.di.FeatureAuthenticationDependenciesContainer
import ru.kpfu.itis.paramonov.leaderboards.di.FeatureLeaderboardsDependencies
import ru.kpfu.itis.paramonov.leaderboards.di.FeatureLeaderboardsDependenciesContainer
import ru.kpfu.itis.paramonov.leaderboards.di.FeatureLeaderboardsHolder
import ru.kpfu.itis.paramonov.questions.di.FeatureQuestionsDependencies
import ru.kpfu.itis.paramonov.questions.di.FeatureQuestionsDependenciesContainer
import ru.kpfu.itis.paramonov.questions.di.FeatureQuestionsHolder
import ru.kpfu.itis.paramonov.profiles.di.FeatureProfilesDependencies
import ru.kpfu.itis.paramonov.profiles.di.FeatureProfilesDependenciesContainer
import ru.kpfu.itis.paramonov.profiles.di.FeatureProfilesHolder
import ru.kpfu.itis.paramonov.users.di.FeatureUsersDependencies
import ru.kpfu.itis.paramonov.users.di.FeatureUsersDependenciesContainer
import ru.kpfu.itis.paramonov.users.di.FeatureUsersHolder
import ru.kpfu.itis.paramonov.quiz.App

@Module
interface ComponentHolderModule {

    @ApplicationScope
    @Binds
    fun featureContainer(application: App): FeatureContainer

    @ApplicationScope
    @Binds
    fun featureAuthenticationDependenciesContainer(application: App): FeatureAuthenticationDependenciesContainer

    @ApplicationScope
    @Binds
    fun featureQuestionsDependenciesContainer(application: App): FeatureQuestionsDependenciesContainer

    @ApplicationScope
    @Binds
    fun featureProfilesDependenciesContainer(application: App): FeatureProfilesDependenciesContainer

    @ApplicationScope
    @Binds
    fun featureLeaderboardsDependenciesContainer(application: App): FeatureLeaderboardsDependenciesContainer

    @ApplicationScope
    @Binds
    fun featureUsersDependenciesContainer(application: App): FeatureUsersDependenciesContainer

    @ApplicationScope
    @Binds
    @ClassKey(FeatureAuthenticationDependencies::class)
    @IntoMap
    fun authenticationFeatureHolder(authenticationFeatureHolder: FeatureAuthenticationHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(FeatureQuestionsDependencies::class)
    @IntoMap
    fun questionsFeatureHolder(questionsFeatureHolder: FeatureQuestionsHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(FeatureProfilesDependencies::class)
    @IntoMap
    fun profilesFeatureHolder(profilesFeatureHolder: FeatureProfilesHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(FeatureLeaderboardsDependencies::class)
    @IntoMap
    fun leaderboardsFeatureHolder(leaderboardsFeatureHolder: FeatureLeaderboardsHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(FeatureUsersDependencies::class)
    @IntoMap
    fun usersFeatureHolder(usersFeatureHolder: FeatureUsersHolder): FeatureApiHolder
}