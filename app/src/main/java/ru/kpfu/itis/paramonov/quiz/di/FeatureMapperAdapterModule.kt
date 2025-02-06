package ru.kpfu.itis.paramonov.quiz.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.quiz.mapper.feature_authentication.FirebaseUserToFeatureAuthenticationUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_leaderboards.FirebaseUserToFeatureLeaderboardsUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_leaderboards.ResultToFeatureLeaderboardsResultMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_profiles.FirebaseUserToFeatureProfilesUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_profiles.ResultToFeatureProfilesResultMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.DatabaseQuestionToFeatureQuestionsDatabaseQuestionMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.FeatureQuestionsDatabaseQuestionToDatabaseQuestionMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.FeatureQuestionsResultToResultMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.FeatureQuestionsUserToFirebaseUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.FirebaseUserToFeatureQuestionsUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.QuestionToFeatureQuestionsQuestionMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_users.FirebaseUserToFeatureUsersUserMapper

val featureMapperAdapterModule = DI.Module("FeatureMapperAdapterModule") {
    bind<FirebaseUserToFeatureAuthenticationUserMapper>() with provider {
        FirebaseUserToFeatureAuthenticationUserMapper()
    }
    bind<FirebaseUserToFeatureQuestionsUserMapper>() with provider {
        FirebaseUserToFeatureQuestionsUserMapper()
    }
    bind<QuestionToFeatureQuestionsQuestionMapper>() with provider {
        QuestionToFeatureQuestionsQuestionMapper()
    }
    bind<FeatureQuestionsUserToFirebaseUserMapper>() with provider {
        FeatureQuestionsUserToFirebaseUserMapper()
    }
    bind<FeatureQuestionsResultToResultMapper>() with provider {
        val featureQuestionsUserToFirebaseUserMapper: FeatureQuestionsUserToFirebaseUserMapper = instance()
        FeatureQuestionsResultToResultMapper(
            featureQuestionsUserToFirebaseUserMapper = featureQuestionsUserToFirebaseUserMapper
        )
    }
    bind<DatabaseQuestionToFeatureQuestionsDatabaseQuestionMapper>() with provider {
        DatabaseQuestionToFeatureQuestionsDatabaseQuestionMapper()
    }
    bind<FeatureQuestionsDatabaseQuestionToDatabaseQuestionMapper>() with provider {
        FeatureQuestionsDatabaseQuestionToDatabaseQuestionMapper()
    }
    bind<ResultToFeatureProfilesResultMapper>() with provider {
        val firebaseUserToFeatureProfilesUserMapper: FirebaseUserToFeatureProfilesUserMapper = instance()
        ResultToFeatureProfilesResultMapper(
            firebaseUserToFeatureProfilesUserMapper = firebaseUserToFeatureProfilesUserMapper
        )
    }
    bind<FirebaseUserToFeatureLeaderboardsUserMapper>() with provider {
        FirebaseUserToFeatureLeaderboardsUserMapper()
    }
    bind<ResultToFeatureLeaderboardsResultMapper>() with provider {
        val firebaseUserToFeatureLeaderboardsUserMapper: FirebaseUserToFeatureLeaderboardsUserMapper = instance()
        ResultToFeatureLeaderboardsResultMapper(
            firebaseUserToFeatureLeaderboardsUserMapper = firebaseUserToFeatureLeaderboardsUserMapper
        )
    }
    bind<FirebaseUserToFeatureUsersUserMapper>() with provider {
        FirebaseUserToFeatureUsersUserMapper()
    }
    bind<FirebaseUserToFeatureProfilesUserMapper>() with provider {
        FirebaseUserToFeatureProfilesUserMapper()
    }
}