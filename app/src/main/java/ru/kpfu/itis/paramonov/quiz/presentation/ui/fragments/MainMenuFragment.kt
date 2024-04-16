package ru.kpfu.itis.paramonov.quiz.presentation.ui.fragments

import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.quiz.R
import ru.kpfu.itis.paramonov.quiz.databinding.FragmentMainMenuBinding
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator
import ru.kpfu.itis.paramonov.quiz.presentation.ui.MainActivity
import javax.inject.Inject

class MainMenuFragment: BaseFragment(R.layout.fragment_main_menu) {

    private val binding: FragmentMainMenuBinding by viewBinding(FragmentMainMenuBinding::bind)

    @Inject
    lateinit var navigator: Navigator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    override fun initView() {
        setMainMenu()
        setOnClickListeners()
    }

    private fun setMainMenu() {
        with(binding) {
            val beginBtnTxt = getString(R.string.begin)
            val questionSettingsBtnText = getString(R.string.question_settings)

            tvQuestion.text = SpannableString(beginBtnTxt).apply {
                setSpan(UnderlineSpan(), 0, beginBtnTxt.length, 0)
            }
            tvQuestionSettings.text = SpannableString(questionSettingsBtnText).apply {
                setSpan(UnderlineSpan(), 0, questionSettingsBtnText.length, 0)
            }
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            tvQuestion.setOnClickListener {
                navigator.goToQuestion()
            }
            tvQuestionSettings.setOnClickListener {
                navigator.goToQuestionSettings()
            }
        }
    }

    override fun observeData() {
    }
}