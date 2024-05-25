package ru.kpfu.itis.paramonov.navigation

import android.view.View

interface UserRouter {

    fun goToUser(id: String)

    fun withSharedView(sharedView: View, block: UserRouter.() -> Unit)
}