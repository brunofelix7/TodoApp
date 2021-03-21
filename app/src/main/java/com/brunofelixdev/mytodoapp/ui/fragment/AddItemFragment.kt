package com.brunofelixdev.mytodoapp.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.databinding.FragmentAddItemBinding
import com.brunofelixdev.mytodoapp.extension.hideKeyboard
import com.brunofelixdev.mytodoapp.extension.myCustomMask
import com.brunofelixdev.mytodoapp.extension.toast
import com.brunofelixdev.mytodoapp.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AddItemFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentAddItemBinding? = null
    private val binding: FragmentAddItemBinding get() = _binding!!

    private val viewModel: ItemViewModel by viewModels()

    private var uiStateJob: Job? = null

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    companion object {
        private val TAG = AddItemFragment::class.java.simpleName
        private const val CALENDAR_MASK = "##/##/####"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        initViews()
        collectData()
        return binding.root
    }

    override fun onDestroyView() {
        uiStateJob?.cancel()
        activity?.hideKeyboard()
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_add_menu, menu)
        ItemFragment.optionsMenu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                submitForm()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.day = dayOfMonth
        this.month = month
        this.year = year

        val pickedDay = if (dayOfMonth < 10) "0${this.day}" else dayOfMonth.toString()
        val pickedMonth = if (month < 10) "0${this.month.plus(1)}" else month.plus(1).toString()
        val pickedYear = year.toString()

        getDateTimeCalendar()

        binding.tilDueDate.error = null

        binding.etDueDate.setText("${pickedDay}/${pickedMonth}/${pickedYear}")
    }

    private fun initViews() {
        binding.etDueDate.myCustomMask(CALENDAR_MASK)
        binding.btnDatePicker.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
    }

    private fun getDateTimeCalendar() {
        val now = Calendar.getInstance()

        this.day = now.get(Calendar.DAY_OF_MONTH)
        this.month = now.get(Calendar.MONTH)
        this.year = now.get(Calendar.YEAR)
    }

    private fun collectData() {
        uiStateJob = lifecycleScope.launch {
            viewModel.uiStateFlow.collect { uiState ->
                when(uiState) {
                    is ItemViewModel.UiState.Loading -> {
                        clearFormErrors()
                        binding.progressBar.isVisible = true
                    }
                    is ItemViewModel.UiState.Success -> {
                        binding.progressBar.isVisible = false
                        activity?.toast(uiState.successMessage)
                        val action = AddItemFragmentDirections.navigateToItem()
                        findNavController().navigate(action)
                    }
                    is ItemViewModel.UiState.Error -> {
                        binding.progressBar.isVisible = false
                        checkFormErrors()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun submitForm() {
        val item = Item().apply {
            name = binding.etName.text.toString()
            dueDate = binding.etDueDate.text.toString()
        }
        viewModel.insertItem(item)
    }

    private fun checkFormErrors() {
        clearFormErrors()
        viewModel.formErrors.forEach { (key, value) ->
            if (key == ItemViewModel.FIELD_NAME) {
                binding.tilName.error = value
            }
            if (key == ItemViewModel.FIELD_DUE_DATE) {
                binding.tilDueDate.error = value
            }
        }
    }

    private fun clearFormErrors() {
        binding.tilName.error = null
        binding.tilDueDate.error = null
    }
}