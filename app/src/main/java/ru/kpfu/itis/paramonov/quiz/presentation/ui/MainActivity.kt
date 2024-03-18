package ru.kpfu.itis.paramonov.quiz.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.paramonov.quiz.R
import ru.kpfu.itis.paramonov.quiz.presentation.ui.fragment.RegisterFragment
import ru.kpfu.itis.paramonov.quiz.presentation.ui.fragment.SignInFragment

@AndroidEntryPoint
class MainActivity: AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .add(R.id.main_activity_container, RegisterFragment())
            .commit()
    }
}