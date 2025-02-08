package ru.kpfu.itis.paramonov.firebase.internal.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.R
import ru.kpfu.itis.paramonov.firebase.external.domain.exceptions.FriendRequestException
import ru.kpfu.itis.paramonov.firebase.internal.data.utils.waitResult
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.FriendRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository

internal class FriendRepositoryImpl(
    private val database: FirebaseFirestore,
    private val dispatcher: CoroutineDispatcher,
    private val resourceManager: ResourceManager,
    private val userRepository: UserRepository
): FriendRepository {

    override suspend fun sendFriendRequest(id: String) {
        withContext(dispatcher) {
            userRepository.getUser(id)?.let { to ->
                val requestsFrom = to.friendRequestFromList.toMutableList()
                userRepository.getCurrentUser()?.let {from ->
                    if (!from.friendIdList.contains(to.id) &&
                        !to.friendRequestFromList.contains(to.id)) {
                        requestsFrom.add(from.id)
                        val toDoc = database.collection(USERS_COLLECTION_NAME).document(to.id)
                        val updates = mapOf(DB_REQUESTS_FIELD to requestsFrom)
                        toDoc.set(updates, SetOptions.mergeFields(DB_REQUESTS_FIELD))
                            .waitResult()
                    }
                } ?: throw FriendRequestException(
                    resourceManager.getString(R.string.friend_req_fail)
                )
            } ?: throw FriendRequestException(
                resourceManager.getString(R.string.friend_req_fail)
            )
        }
    }

    override suspend fun acceptFriendRequest(id: String) {
        withContext(dispatcher) {
            userRepository.getCurrentUser()?.let { to ->
                userRepository.getUser(id)?.let { from ->
                    if (to.friendRequestFromList.contains(id) && !to.friendIdList.contains(id)) {
                        val requestsFrom = to.friendRequestFromList.toMutableList().apply { remove(from.id) }
                        val toFriends = to.friendIdList.toMutableList().apply { add(from.id) }
                        val fromFriends = to.friendIdList.toMutableList().apply { add(to.id) }
                        val fromDoc = database.collection(USERS_COLLECTION_NAME).document(from.id)
                        val toDoc = database.collection(USERS_COLLECTION_NAME).document(to.id)

                        val fromUpdates = mapOf(DB_FRIENDS_FIELD to fromFriends)
                        val toUpdates = mapOf(DB_REQUESTS_FIELD to requestsFrom, DB_FRIENDS_FIELD to toFriends)
                        val taskTo = toDoc.set(toUpdates, SetOptions.mergeFields(DB_REQUESTS_FIELD, DB_FRIENDS_FIELD))
                        val taskFrom = fromDoc.set(fromUpdates, SetOptions.mergeFields(
                            DB_FRIENDS_FIELD
                        ))
                        taskTo.waitResult()
                        taskFrom.waitResult()
                    }
                }
            }
        }
    }

    override suspend fun denyFriendRequest(id: String) {
        withContext(dispatcher) {
            userRepository.getCurrentUser()?.let { to ->
                userRepository.getUser(id)?.let { from ->
                    if (to.friendRequestFromList.contains(id) && !to.friendIdList.contains(id)) {
                        val requestsFrom = to.friendRequestFromList.toMutableList().apply { remove(from.id) }
                        val toDoc = database.collection(USERS_COLLECTION_NAME).document(to.id)
                        val toUpdates = mapOf(DB_REQUESTS_FIELD to requestsFrom)
                        toDoc.set(toUpdates, SetOptions.mergeFields(DB_REQUESTS_FIELD)).waitResult()
                    }
                }
            }
        }
    }

    companion object {
        private const val USERS_COLLECTION_NAME = "users"
        private const val DB_FRIENDS_FIELD = "friends"
        private const val DB_REQUESTS_FIELD = "requestsFrom"
    }
}
