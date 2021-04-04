package com.brunofelixdev.mytodoapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.data.pref.isNightModeEnabled
import com.brunofelixdev.mytodoapp.data.pref.setIsNightModeEnabled
import com.brunofelixdev.mytodoapp.databinding.FragmentSettingsBinding

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
        if (isNightModeEnabled(requireContext())) {
            binding.radioDark.isChecked = true
        } else {
            binding.radioLight.isChecked = true
        }

        binding.rgParent.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radio_light -> {
                    setIsNightModeEnabled(requireContext(), false)
                    activity?.recreate()
                    activity?.setTheme(R.style.Theme_MyToDoApp)
                }
                R.id.radio_dark -> {
                    setIsNightModeEnabled(requireContext(), true)
                    activity?.recreate()
                    activity?.setTheme(R.style.Theme_MyToDoApp)
                }
            }
        }
    }
}