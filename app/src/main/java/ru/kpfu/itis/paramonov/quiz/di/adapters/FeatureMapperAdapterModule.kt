package ru.kpfu.itis.paramonov.quiz.di.adapters

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.quiz.mapper.authentication.FirebaseUserToFeatureAuthenticationUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.leaderboards.FirebaseUserToFeatureLeaderboardsUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.leaderboards.ResultToFeatureLeaderboardsResultMapper
import ru.kpfu.itis.paramonov.quiz.mapper.profiles.FirebaseUserToFeatureProfilesUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.profiles.ResultToFeatureProfilesResultMapper
import ru.kpfu.itis.paramonov.quiz.mapper.questions.DatabaseQuestionToFeatureQuestionsDatabaseQuestionMapper
import ru.kpfu.itis.paramonov.quiz.mapper.questions.FeatureQuestionsDatabaseQuestionToDatabaseQuestionMapper
import ru.kpfu.itis.paramonov.quiz.mapper.questions.FeatureQuestionsResultToResultMapper
import ru.kpfu.itis.paramonov.quiz.mapper.questions.FeatureQuestionsUserToFirebaseUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.questions.FirebaseUserToFeatureQuestionsUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.questions.QuestionToFeatureQuestionsQuestionMapper
import ru.kpfu.itis.paramonov.quiz.mapper.users.FirebaseUserToFeatureUsersUserMapper

typealias QuestionsAdapterUserMapper = FeatureQuestionsUserToFirebaseUserMapper

typealias ProfilesAdapterUserMapper = FirebaseUserToFeatureProfilesUserMapper

typealias LeaderboardsAdapterUserMapper = FirebaseUserToFeatureLeaderboardsUserMapper

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
        val featureQuestionsUserToFirebaseUserMapper: QuestionsAdapterUserMapper = instance()
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
        val firebaseUserToFeatureProfilesUserMapper: ProfilesAdapterUserMapper = instance()
        ResultToFeatureProfilesResultMapper(
            firebaseUserToFeatureProfilesUserMapper = firebaseUserToFeatureProfilesUserMapper
        )
    }
    bind<FirebaseUserToFeatureLeaderboardsUserMapper>() with provider {
        FirebaseUserToFeatureLeaderboardsUserMapper()
    }
    bind<ResultToFeatureLeaderboardsResultMapper>() with provider {
        val firebaseUserToFeatureLeaderboardsUserMapper: LeaderboardsAdapterUserMapper = instance()
        ResultToFeatureLeaderboardsResultMapper(
            firebaseUserToFeatureLeaderboardsUserMapper
                = firebaseUserToFeatureLeaderboardsUserMapper
        )
    }
    bind<FirebaseUserToFeatureUsersUserMapper>() with provider {
        FirebaseUserToFeatureUsersUserMapper()
    }
    bind<FirebaseUserToFeatureProfilesUserMapper>() with provider {
        FirebaseUserToFeatureProfilesUserMapper()
    }
}
