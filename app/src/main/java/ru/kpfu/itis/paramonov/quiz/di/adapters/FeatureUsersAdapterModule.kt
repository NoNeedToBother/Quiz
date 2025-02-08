package ru.kpfu.itis.paramonov.quiz.di.adapters

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.quiz.mapper.users.FirebaseUserToFeatureUsersUserMapper
import ru.kpfu.itis.paramonov.users.api.model.User
import ru.kpfu.itis.paramonov.users.api.repository.UserRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository as FirebaseUserRepository

val featureUsersAdapterModule = DI.Module("FeatureUsersAdapterModule") {
    bind<UserRepository>() with provider {
        val userRepository: FirebaseUserRepository
        = instance()
        val firebaseUserToFeatureUsersUserMapper: FirebaseUserToFeatureUsersUserMapper = instance()
        object : UserRepository {
            override suspend fun findByUsername(
                username: String,
                max: Int,
                lastId: String?
            ): List<User> =
                userRepository.findByUsername(username, max, lastId).map {
                    firebaseUserToFeatureUsersUserMapper.map(it)
                }

            override suspend fun getFriends(
                offset: Int,
                max: Int
            ): List<User> =
                userRepository.getFriends(offset, max).map {
                    firebaseUserToFeatureUsersUserMapper.map(it)
                }
        }
    }
}
