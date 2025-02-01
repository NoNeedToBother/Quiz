package ru.kpfu.itis.paramonov.questions.presentation.settings.fragments

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.questions.R
import ru.kpfu.itis.paramonov.questions.databinding.FragmentTrainingQuestionSettingsBinding
import ru.kpfu.itis.paramonov.questions.di.FeatureQuestionsComponent
import ru.kpfu.itis.paramonov.questions.di.FeatureQuestionsDependencies
import ru.kpfu.itis.paramonov.questions.presentation.settings.viewmodel.TrainingQuestionSettingsViewModel
import java.lang.NumberFormatException
import javax.inject.Inject

class TrainingQuestionSettingsFragment: BaseFragment(R.layout.fragment_training_question_settings) {
    private val binding: FragmentTrainingQuestionSettingsBinding by viewBinding(FragmentTrainingQuestionSettingsBinding::bind)

    @Inject
    lateinit var viewModel: TrainingQuestionSettingsViewModel

    override fun inject() {
        FeatureUtils.getFeature<FeatureQuestionsComponent>(this, FeatureQuestionsDependencies::class.java)
            .trainingQuestionSettingsComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView() {
        with(binding) {
            etLimit.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let {
                        val len = it.toString().length
                        val default = getString(R.string.enter_limit_default)
                        if (len == default.length - 1) {
                            etLimit.setText(default)
                            etLimit.setSelection(default.length)
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val regex = """\d+""".toRegex()
                        regex.find(s)?.let {
                            try {
                                val number = it.value.toInt()
                                if (number !in LIMIT_LOWER_BOUND..LIMIT_UPPER_BOUND) {
                                    etLimit.error = getString(
                                        R.string.limit_should_be, LIMIT_LOWER_BOUND, LIMIT_UPPER_BOUND)
                                }
                            } catch (ex: NumberFormatException) {
                                etLimit.error = getString(
                                    R.string.limit_should_be, LIMIT_LOWER_BOUND, LIMIT_UPPER_BOUND)
                            }
                        }
                    }
                }
            })

            btnSave.setOnClickListener {
                val limitTxt = etLimit.text.toString()
                val regex = """\d+""".toRegex()
                regex.find(limitTxt)?.let {
                    try {
                        viewModel.saveTrainingQuestionSettings(it.value.toInt())
                    } catch (_: NumberFormatException) {
                        showErrorBottomSheetDialog(
                            getString(ru.kpfu.itis.paramonov.ui.R.string.empty),
                            getString(
                                R.string.limit_not_correct, LIMIT_LOWER_BOUND, LIMIT_UPPER_BOUND)
                            )
                    }
                }
            }
        }
    }

    override fun observeData() {
        viewModel.getTrainingQuestionSettings()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectSettingsData()
                }
            }
        }
    }

    private suspend fun collectSettingsData() {
        viewModel.settingsDataFlow.collect {
            it?.let { result ->
                when(result) {
                    is TrainingQuestionSettingsViewModel.TrainingQuestionSettingsResult.Success -> {
                        with(binding) {
                            etLimit.setText(getString(R.string.enter_limit, result.getValue().limit))
                            btnSave.isEnabled = true
                        }
                    }
                    is TrainingQuestionSettingsViewModel.TrainingQuestionSettingsResult.Failure -> {
                        showErrorBottomSheetDialog(
                            getString(ru.kpfu.itis.paramonov.ui.R.string.empty),
                            result.getException().message ?:
                            getString(ru.kpfu.itis.paramonov.ui.R.string.default_error_msg)
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val LIMIT_LOWER_BOUND = 1
        private const val LIMIT_UPPER_BOUND = 100
    }
}