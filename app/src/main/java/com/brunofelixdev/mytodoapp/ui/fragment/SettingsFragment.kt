package com.brunofelixdev.mytodoapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.data.pref.getCurrentThemeMode
import com.brunofelixdev.mytodoapp.data.pref.setCurrentThemeMode
import com.brunofelixdev.mytodoapp.databinding.FragmentSettingsBinding
import com.brunofelixdev.mytodoapp.util.Constants

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initViews() {
        when (getCurrentThemeMode(requireContext())) {
            Constants.PREF_SYSTEM -> {
                binding.radioSystem.isChecked = true
            }
            Constants.PREF_LIGHT -> {
                binding.radioLight.isChecked = true
            }
            Constants.PREF_NIGHT -> {
                binding.radioNight.isChecked = true
            }
        }

        binding.rgParent.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radio_system -> {
                    setCurrentThemeMode(requireContext(), Constants.PREF_SYSTEM)
                    activity?.recreate()
                    activity?.setTheme(R.style.Theme_MyToDoApp)
                }
                R.id.radio_light -> {
                    setCurrentThemeMode(requireContext(), Constants.PREF_LIGHT)
                    activity?.recreate()
                    activity?.setTheme(R.style.Theme_MyToDoApp)
                }
                R.id.radio_night -> {
                    setCurrentThemeMode(requireContext(), Constants.PREF_NIGHT)
                    activity?.recreate()
                    activity?.setTheme(R.style.Theme_MyToDoApp)
                }
            }
        }
    }
}