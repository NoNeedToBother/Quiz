package ru.kpfu.itis.paramonov.navigation

sealed class Routes(val route: String) {
    data object RegisterScreen: Routes("registerScreen")
    data object SignInScreen: Routes("signInScreen")
    data object MainMenuScreen: Routes("mainMenuScreen")
    data object QuestionsScreen: Routes("questionsScreen")
    data object TrainingScreen: Routes("trainingScreen")
    data object QuestionSettingsScreen: Routes("questionSettingsScreen")
    data object UserScreen: Routes("userScreen")

    data object SearchUsersScreen: Routes("searchUsersScreen")
    data object LeaderboardsScreen: Routes("leaderboardsScreen")
    data object FriendsScreen: Routes("friendsScreen")
    data object ProfileScreen: Routes("profileScreen")
}
