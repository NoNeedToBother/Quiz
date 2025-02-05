package ru.kpfu.itis.paramonov.quiz

import android.app.Application
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kpfu.itis.paramonov.authentication.api.api.FeatureAuthenticationApi
import ru.kpfu.itis.paramonov.authentication.api.model.User
import ru.kpfu.itis.paramonov.authentication.api.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.quiz.di.dependencies.FeatureHolderManager
import ru.kpfu.itis.paramonov.core.di.FeatureContainer
import ru.kpfu.itis.paramonov.core.api.CommonApi
import ru.kpfu.itis.paramonov.core.api.ContextApi
import ru.kpfu.itis.paramonov.authentication.di.FeatureAuthenticationDependenciesContainer
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.leaderboards.di.FeatureLeaderboardsDependenciesContainer
import ru.kpfu.itis.paramonov.questions.di.FeatureQuestionsDependenciesContainer
import ru.kpfu.itis.paramonov.profiles.di.FeatureProfilesDependenciesContainer
import ru.kpfu.itis.paramonov.users.di.FeatureUsersDependenciesContainer
import ru.kpfu.itis.paramonov.firebase.external.di.DaggerFirebaseComponent
import ru.kpfu.itis.paramonov.firebase.external.di.DaggerFirebaseComponent_FirebaseDependenciesComponent
import ru.kpfu.itis.paramonov.database.external.di.DaggerLocalDatabaseComponent
import ru.kpfu.itis.paramonov.database.external.di.DaggerLocalDatabaseComponent_LocalDatabaseDependenciesComponent
import ru.kpfu.itis.paramonov.quiz.di.AppComponent
import ru.kpfu.itis.paramonov.quiz.di.dependencies.ComponentDependenciesProvider
import ru.kpfu.itis.paramonov.firebase.external.di.FirebaseComponent
import ru.kpfu.itis.paramonov.database.external.di.LocalDatabaseComponent
import ru.kpfu.itis.paramonov.leaderboards.api.api.FeatureLeaderboardsApi
import ru.kpfu.itis.paramonov.network.external.di.DaggerQuestionsComponent
import ru.kpfu.itis.paramonov.network.external.di.DaggerQuestionsComponent_QuestionDependenciesComponent
import ru.kpfu.itis.paramonov.network.external.di.QuestionsComponent
import ru.kpfu.itis.paramonov.profiles.api.api.FeatureProfilesApi
import ru.kpfu.itis.paramonov.profiles.api.repository.FriendRepository
import ru.kpfu.itis.paramonov.questions.api.api.FeatureQuestionsApi
import ru.kpfu.itis.paramonov.questions.api.model.Question
import ru.kpfu.itis.paramonov.questions.api.model.Result
import ru.kpfu.itis.paramonov.questions.api.model.database.DatabaseQuestion
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.questions.api.repository.SavedQuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.UserRepository
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.DatabaseQuestionToFeatureQuestionsDatabaseQuestionMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.FeatureQuestionsDatabaseQuestionToDatabaseQuestionMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.FeatureQuestionsResultToResultMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_authentication.FirebaseUserToFeatureAuthenticationUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_leaderboards.ResultToFeatureLeaderboardsResultMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_profiles.FirebaseUserToFeatureProfilesUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_profiles.ResultToFeatureProfilesResultMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.FirebaseUserToFeatureQuestionsUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.QuestionToFeatureQuestionsQuestionMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_users.FirebaseUserToFeatureUsersUserMapper
import ru.kpfu.itis.paramonov.users.api.api.FeatureUsersApi
import javax.inject.Inject

class App: Application(), FeatureContainer, FeatureAuthenticationDependenciesContainer,
    FeatureQuestionsDependenciesContainer, FeatureProfilesDependenciesContainer,
    FeatureLeaderboardsDependenciesContainer, FeatureUsersDependenciesContainer {
    @Inject
    lateinit var featureHolderManager: FeatureHolderManager

    @Inject
    lateinit var dependencies: ComponentDependenciesProvider

    @Inject
    lateinit var firebaseUserToFeatureAuthenticationUserMapper: FirebaseUserToFeatureAuthenticationUserMapper

    @Inject
    lateinit var firebaseUserToFeatureQuestionsUserMapper: FirebaseUserToFeatureQuestionsUserMapper

    @Inject
    lateinit var questionToFeatureQuestionsQuestionMapper: QuestionToFeatureQuestionsQuestionMapper

    @Inject
    lateinit var featureQuestionsResultToResultMapper: FeatureQuestionsResultToResultMapper

    @Inject
    lateinit var databaseQuestionToFeatureQuestionsDatabaseQuestionMapper: DatabaseQuestionToFeatureQuestionsDatabaseQuestionMapper

    @Inject
    lateinit var featureQuestionsDatabaseQuestionToDatabaseQuestionMapper: FeatureQuestionsDatabaseQuestionToDatabaseQuestionMapper

    @Inject
    lateinit var resultToFeatureProfilesResultMapper: ResultToFeatureProfilesResultMapper

    @Inject
    lateinit var firebaseUserToFeatureProfilesUserMapper: FirebaseUserToFeatureProfilesUserMapper

    @Inject
    lateinit var resultToFeatureLeaderboardsResultMapper: ResultToFeatureLeaderboardsResultMapper

    @Inject
    lateinit var firebaseUserToFeatureUsersUserMapper: FirebaseUserToFeatureUsersUserMapper

    private lateinit var appComponent: AppComponent

    private lateinit var firebaseComponent: FirebaseComponent

    private lateinit var localDatabaseComponent: LocalDatabaseComponent

    private lateinit var questionsComponent: QuestionsComponent

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        initAppComponent()
        initFirebaseComponent()
        initLocalDatabaseComponent()
        initQuestionsComponent()
    }

    private fun initAppComponent() {
        appComponent = AppComponent.init(this)
        appComponent.inject(this)
    }

    private fun initFirebaseComponent() {
        val firebaseDependencies = DaggerFirebaseComponent_FirebaseDependenciesComponent.builder()
            .commonApi(appComponent)
            .build()

        firebaseComponent = DaggerFirebaseComponent.builder()
            .firebaseDependencies(firebaseDependencies)
            .build()
    }

    private fun initLocalDatabaseComponent() {
        val localDatabaseDependencies = DaggerLocalDatabaseComponent_LocalDatabaseDependenciesComponent.builder()
            .commonApi(appComponent)
            .contextApi(appComponent)
            .build()

        localDatabaseComponent = DaggerLocalDatabaseComponent.builder()
            .localDatabaseDependencies(localDatabaseDependencies)
            .build()
    }

    private fun initQuestionsComponent() {
        val questionsDependencies = DaggerQuestionsComponent_QuestionDependenciesComponent.builder()
            .commonApi(appComponent)
            .build()

        questionsComponent = DaggerQuestionsComponent.builder()
            .questionsDependencies(questionsDependencies)
            .build()
    }

    override fun featureAuthenticationApi(): FeatureAuthenticationApi {
        return object : FeatureAuthenticationApi {
            override fun authenticationRepository(): AuthenticationRepository {
                return object : AuthenticationRepository {
                    override suspend fun registerUser(
                        username: String,
                        email: String,
                        password: String,
                        confirmPassword: String
                    ): User = firebaseUserToFeatureAuthenticationUserMapper.map(
                        firebaseComponent.authenticationRepository().registerUser(
                            username = username, email = email, password = password, confirmPassword = confirmPassword
                        )
                    )

                    override suspend fun authenticateUser(email: String, password: String): User =
                        firebaseUserToFeatureAuthenticationUserMapper.map(
                            firebaseComponent.authenticationRepository().authenticateUser(email, password)
                        )

                    override suspend fun checkUserIsAuthenticated(): User? =
                        firebaseComponent.authenticationRepository().checkUserIsAuthenticated()?.let {
                            firebaseUserToFeatureAuthenticationUserMapper.map(it)
                        }
                }
            }
        }
    }

    override fun featureQuestionsApi(): FeatureQuestionsApi {
        return object : FeatureQuestionsApi {
            override fun userRepository(): UserRepository {
                return object : UserRepository {
                    override suspend fun getCurrentUser(): ru.kpfu.itis.paramonov.questions.api.model.User? =
                        firebaseComponent.userRepository().getCurrentUser()?.let {
                            firebaseUserToFeatureQuestionsUserMapper.map(it)
                        }
                }
            }

            override fun questionRepository(): QuestionRepository {
                return object : QuestionRepository {
                    override suspend fun getCategoryCode(category: Category): Int {
                        return questionsComponent.questionRepository().getCategoryCode(category)
                    }

                    override suspend fun getQuestions(
                        amount: Int,
                        difficulty: Difficulty,
                        category: Int
                    ): Question {
                        return questionToFeatureQuestionsQuestionMapper.map(
                            questionsComponent.questionRepository().getQuestions(
                                amount, difficulty, category
                            )
                        )
                    }
                }
            }

            override fun questionSettingsRepository(): QuestionSettingsRepository {
                return object : QuestionSettingsRepository {
                    override fun getDifficulty(): Difficulty = localDatabaseComponent.sharedPreferencesRepository().getDifficulty()

                    override fun saveDifficulty(difficulty: Difficulty) {
                        localDatabaseComponent.sharedPreferencesRepository().saveDifficulty(difficulty)
                    }

                    override fun getCategory(): Category = localDatabaseComponent.sharedPreferencesRepository().getCategory()

                    override fun saveCategory(category: Category) {
                        localDatabaseComponent.sharedPreferencesRepository().saveCategory(category)
                    }

                    override fun getGameMode(): GameMode = localDatabaseComponent.sharedPreferencesRepository().getGameMode()

                    override fun saveGameMode(gameMode: GameMode) {
                        localDatabaseComponent.sharedPreferencesRepository().saveGameMode(gameMode)
                    }

                    override fun getLimit(): Int = localDatabaseComponent.sharedPreferencesRepository().getLimit()

                    override fun saveLimit(limit: Int) {
                        localDatabaseComponent.sharedPreferencesRepository().saveLimit(limit)
                    }
                }
            }

            override fun resultRepository(): ResultRepository {
                return object : ResultRepository {
                    override suspend fun getMaxScore(): Double = firebaseComponent.resultRepository().getMaxScore()

                    override suspend fun save(result: Result): Double {
                        return firebaseComponent.resultRepository().save(
                            featureQuestionsResultToResultMapper.map(result)
                        )
                    }
                }
            }

            override fun savedQuestionRepository(): SavedQuestionRepository {
                return object : SavedQuestionRepository {
                    override suspend fun getQuestions(limit: Int): List<DatabaseQuestion> =
                        localDatabaseComponent.savedQuestionsRepository().getQuestions(limit)
                            .map { databaseQuestionToFeatureQuestionsDatabaseQuestionMapper.map(it) }

                    override suspend fun saveQuestions(questions: List<DatabaseQuestion>) {
                        localDatabaseComponent.savedQuestionsRepository().saveQuestions(
                            questions.map { featureQuestionsDatabaseQuestionToDatabaseQuestionMapper.map(it) }
                        )
                    }
                }
            }
        }
    }

    override fun featureProfilesApi(): FeatureProfilesApi {
        return object : FeatureProfilesApi {
            override fun friendRepository(): FriendRepository {
                return object : FriendRepository {
                    override suspend fun sendFriendRequest(id: String) {
                        firebaseComponent.friendRepository().sendFriendRequest(id)
                    }

                    override suspend fun acceptFriendRequest(id: String) {
                        firebaseComponent.friendRepository().acceptFriendRequest(id)
                    }

                    override suspend fun denyFriendRequest(id: String) {
                        firebaseComponent.friendRepository().denyFriendRequest(id)
                    }
                }
            }

            override fun resultRepository(): ru.kpfu.itis.paramonov.profiles.api.repository.ResultRepository {
                return object : ru.kpfu.itis.paramonov.profiles.api.repository.ResultRepository {
                    override suspend fun getLastResults(max: Int): List<ru.kpfu.itis.paramonov.profiles.api.model.Result> =
                        firebaseComponent.resultRepository().getLastResults(max).map {
                            resultToFeatureProfilesResultMapper.map(it)
                        }

                    override suspend fun getLastResults(
                        max: Int,
                        id: String
                    ): List<ru.kpfu.itis.paramonov.profiles.api.model.Result> =
                        firebaseComponent.resultRepository().getLastResults(max, id).map {
                            resultToFeatureProfilesResultMapper.map(it)
                        }
                }
            }

            override fun userRepository(): ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository {
                return object : ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository {
                    override suspend fun getCurrentUser(): ru.kpfu.itis.paramonov.profiles.api.model.User? =
                        firebaseComponent.userRepository().getCurrentUser()?.let {
                            firebaseUserToFeatureProfilesUserMapper.map(it)
                        }

                    override suspend fun getUser(id: String): ru.kpfu.itis.paramonov.profiles.api.model.User? =
                        firebaseComponent.userRepository().getUser(id)?.let {
                            firebaseUserToFeatureProfilesUserMapper.map(it)
                        }

                    override suspend fun logoutUser(onLogoutSuccess: suspend () -> Unit) {
                        firebaseComponent.userRepository().logoutUser(onLogoutSuccess)
                    }

                    override suspend fun reauthenticate(email: String, password: String) {
                        firebaseComponent.userRepository().reauthenticate(email, password)
                    }

                    override suspend fun subscribeToProfileUpdates(): Flow<ru.kpfu.itis.paramonov.profiles.api.model.User> =
                        firebaseComponent.userRepository().subscribeToProfileUpdates().map {
                            firebaseUserToFeatureProfilesUserMapper.map(it)
                        }

                    override suspend fun updateUser(vararg pairs: Pair<String, Any>): ru.kpfu.itis.paramonov.profiles.api.model.User =
                        firebaseComponent.userRepository().updateUser(*pairs).let {
                            firebaseUserToFeatureProfilesUserMapper.map(it)
                        }

                    override suspend fun updateCredentials(email: String?, password: String?) {
                        firebaseComponent.userRepository().updateCredentials(email, password)
                    }
                }
            }
        }
    }

    override fun featureLeaderboardsApi(): FeatureLeaderboardsApi {
        return object : FeatureLeaderboardsApi {
            override fun resultRepository(): ru.kpfu.itis.paramonov.leaderboards.api.repository.ResultRepository {
                return object : ru.kpfu.itis.paramonov.leaderboards.api.repository.ResultRepository {
                    override suspend fun getGlobalResults(
                        gameMode: GameMode,
                        difficulty: Difficulty?,
                        category: Category?,
                        max: Int,
                        afterScore: Double?
                    ): List<ru.kpfu.itis.paramonov.leaderboards.api.model.Result> =
                        firebaseComponent.resultRepository().getGlobalResults(
                            gameMode, difficulty, category, max, afterScore
                        ).map { resultToFeatureLeaderboardsResultMapper.map(it) }

                    override suspend fun getFriendsResults(
                        gameMode: GameMode,
                        difficulty: Difficulty?,
                        category: Category?,
                        max: Int,
                        afterScore: Double?
                    ): List<ru.kpfu.itis.paramonov.leaderboards.api.model.Result> =
                        firebaseComponent.resultRepository().getFriendsResults(
                            gameMode, difficulty, category, max, afterScore
                        ).map { resultToFeatureLeaderboardsResultMapper.map(it) }
                }
            }

            override fun questionSettingsRepository(): ru.kpfu.itis.paramonov.leaderboards.api.repository.QuestionSettingsRepository {
                return object : ru.kpfu.itis.paramonov.leaderboards.api.repository.QuestionSettingsRepository {
                    override fun getDifficulty(): Difficulty =
                        localDatabaseComponent.sharedPreferencesRepository().getDifficulty()

                    override fun getGameMode(): GameMode =
                        localDatabaseComponent.sharedPreferencesRepository().getGameMode()
                }
            }
        }
    }

    override fun featureUsersApi(): FeatureUsersApi {
        return object : FeatureUsersApi {
            override fun userRepository(): ru.kpfu.itis.paramonov.users.api.repository.UserRepository {
                return object : ru.kpfu.itis.paramonov.users.api.repository.UserRepository {
                    override suspend fun findByUsername(
                        username: String,
                        max: Int,
                        lastId: String?
                    ): List<ru.kpfu.itis.paramonov.users.api.model.User> =
                        firebaseComponent.userRepository().findByUsername(
                            username, max, lastId
                        ).map { firebaseUserToFeatureUsersUserMapper.map(it) }

                    override suspend fun getFriends(
                        offset: Int,
                        max: Int
                    ): List<ru.kpfu.itis.paramonov.users.api.model.User> =
                        firebaseComponent.userRepository().getFriends(
                            offset, max
                        ).map { firebaseUserToFeatureUsersUserMapper.map(it) }
                }
            }
        }
    }

    override fun <T> getFeature(key: Class<*>): T {
        return featureHolderManager.getFeature<T>(key)!!
    }

    override fun releaseFeature(key: Class<*>) {
        featureHolderManager.releaseFeature(key)
    }

    override fun commonApi(): CommonApi {
        return appComponent
    }

    override fun contextApi(): ContextApi {
        return appComponent
    }


}