package ru.kpfu.itis.paramonov.quiz.di.adapters

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.profiles.api.model.Result
import ru.kpfu.itis.paramonov.profiles.api.model.User
import ru.kpfu.itis.paramonov.profiles.api.repository.FriendRepository
import ru.kpfu.itis.paramonov.profiles.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.quiz.mapper.profiles.FirebaseUserToFeatureProfilesUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.profiles.ResultToFeatureProfilesResultMapper

val featureProfilesAdapterModule = DI.Module("") {
    bind<FriendRepository>() with provider {
        val friendRepository: ru.kpfu.itis.paramonov.firebase.external.repository.FriendRepository
                = instance()
        object : FriendRepository {
            override suspend fun sendFriendRequest(id: String) {
                friendRepository.sendFriendRequest(id)
            }
            override suspend fun acceptFriendRequest(id: String) {
                friendRepository.acceptFriendRequest(id)
            }
            override suspend fun denyFriendRequest(id: String) {
                friendRepository.denyFriendRequest(id)
            }
        }
    }

    bind<ResultRepository>() with provider {
        val resultRepository: ru.kpfu.itis.paramonov.firebase.external.repository.ResultRepository
            = instance()
        val resultToFeatureProfilesResultMapper: ResultToFeatureProfilesResultMapper = instance()

        object : ResultRepository {
            override suspend fun getLastResults(max: Int): List<Result> =
                resultRepository.getLastResults(max).map {
                    resultToFeatureProfilesResultMapper.map(it)
                }

            override suspend fun getLastResults(
                max: Int,
                id: String
            ): List<Result> =
                resultRepository.getLastResults(max, id).map {
                    resultToFeatureProfilesResultMapper.map(it)
                }
        }
    }

    bind<UserRepository>() with provider {
        val userRepository: ru.kpfu.itis.paramonov.firebase.external.repository.UserRepository
            = instance()
        val firebaseUserToFeatureProfilesUserMapper: FirebaseUserToFeatureProfilesUserMapper = instance()

        object : UserRepository {
            override suspend fun getCurrentUser(): User? =
                userRepository.getCurrentUser()?.let { firebaseUserToFeatureProfilesUserMapper.map(it) }

            override suspend fun getUser(id: String): User? =
                userRepository.getUser(id)?.let { firebaseUserToFeatureProfilesUserMapper.map(it) }

            override suspend fun logoutUser(onLogoutSuccess: suspend () -> Unit) {
                userRepository.logoutUser(onLogoutSuccess)
            }

            override suspend fun reauthenticate(email: String, password: String) {
                userRepository.reauthenticate(email, password)
            }

            override suspend fun subscribeToProfileUpdates(): Flow<User> =
                userRepository.subscribeToProfileUpdates().map {
                    firebaseUserToFeatureProfilesUserMapper.map(it)
                }

            override suspend fun updateUser(vararg pairs: Pair<String, Any>): User =
                userRepository.updateUser(*pairs).let { firebaseUserToFeatureProfilesUserMapper.map(it) }

            override suspend fun updateCredentials(email: String?, password: String?) {
                userRepository.updateCredentials(email, password)
            }
        }
    }
}
