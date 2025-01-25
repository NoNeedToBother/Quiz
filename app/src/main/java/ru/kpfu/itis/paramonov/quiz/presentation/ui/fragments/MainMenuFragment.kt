package ru.kpfu.itis.paramonov.quiz.presentation.ui.fragments

import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.core.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.quiz.R
import ru.kpfu.itis.paramonov.quiz.databinding.FragmentMainMenuBinding
import ru.kpfu.itis.paramonov.quiz.di.dependencies.findComponentDependencies
import ru.kpfu.itis.paramonov.quiz.di.main.MainComponent
import ru.kpfu.itis.paramonov.quiz.di.main.MainDependencies
import ru.kpfu.itis.paramonov.quiz.presentation.viewmodel.MainMenuViewModel
import javax.inject.Inject

class MainMenuFragment: BaseFragment(R.layout.fragment_main_menu) {

    private val binding: FragmentMainMenuBinding by viewBinding(FragmentMainMenuBinding::bind)

    @Inject
    lateinit var viewModel: MainMenuViewModel

    override fun inject() {
        with(requireActivity() as AppCompatActivity) {
            MainComponent.init(this,
                findComponentDependencies<MainDependencies>())
                .mainMenuComponentFactory()
                .create(this@MainMenuFragment)
                .inject(this@MainMenuFragment)
        }
    }

    override fun initView() {
        setMainMenu()
        setOnClickListeners()
    }

    private fun setMainMenu() {
        with(binding) {
            val beginBtnTxt = getString(R.string.begin)
            val questionSettingsBtnText = getString(R.string.question_settings)
            val trainingBtnText = getString(R.string.training)

            tvQuestion.text = SpannableString(beginBtnTxt).apply {
                setSpan(UnderlineSpan(), 0, beginBtnTxt.length, 0)
            }
            tvQuestionSettings.text = SpannableString(questionSettingsBtnText).apply {
                setSpan(UnderlineSpan(), 0, questionSettingsBtnText.length, 0)
            }
            tvTraining.text = SpannableString(trainingBtnText).apply {
                setSpan(UnderlineSpan(), 0, trainingBtnText.length, 0)
            }
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            tvQuestion.setOnClickListener {
                viewModel.goToQuestions()
            }
            tvQuestionSettings.setOnClickListener {
                viewModel.goToQuestionSettings()
            }
            tvTraining.setOnClickListener {
                viewModel.goToTrainingMode()
            }
        }
    }

    override fun observeData() {
    }
}