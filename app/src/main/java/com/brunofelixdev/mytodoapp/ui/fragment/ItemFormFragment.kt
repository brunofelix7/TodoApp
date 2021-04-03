package com.brunofelixdev.mytodoapp.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.databinding.FragmentItemFormBinding
import com.brunofelixdev.mytodoapp.extension.*
import com.brunofelixdev.mytodoapp.util.Constants
import com.brunofelixdev.mytodoapp.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class ItemFormFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var _binding: FragmentItemFormBinding? = null
    private val binding: FragmentItemFormBinding get() = _binding!!

    private val args: ItemFormFragmentArgs by navArgs()

    private val currentItem get() = args.currentItem

    private val viewModel: ItemViewModel by viewModels()

    private var uiStateJob: Job? = null

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var hour: Int = 0
    private var minute: Int = 0

    companion object {
        private val TAG = ItemFormFragment::class.java.simpleName
        private const val CALENDAR_MASK = "##-##-####"
        private const val CLOCK_MASK = "##:##"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemFormBinding.inflate(inflater, container, false)
        initViews()
        collectData()
        return binding.root
    }

    override fun onDestroyView() {
        uiStateJob?.cancel()
        activity?.hideKeyboard()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()

        //  If the user changes the theme when this screen is open.
        if (currentItem != null) {
            (activity as AppCompatActivity?)!!.supportActionBar?.title =
                resources.getString(R.string.label_edit_item)
        }
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

        TimePickerDialog(requireContext(), this, hour, minute, false).show()

        binding.tilDueDate.error = null

        binding.etDueDate.setText("${pickedMonth}-${pickedDay}-${pickedYear}")
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        this.hour = hourOfDay
        this.minute = minute

        val pickedHour = if (hourOfDay < 10) "0${this.hour}" else hourOfDay.toString()
        val pickedMinute = if (minute < 10) "0${this.minute}" else minute.toString()

        binding.tilDueTime.error = null
        binding.etDueTime.setText("${pickedHour}:${pickedMinute}")
    }

    private fun initViews() {
        (activity as AppCompatActivity?)!!.supportActionBar?.show()

        binding.etDueDate.myCustomMask(CALENDAR_MASK)
        binding.etDueTime.myCustomMask(CLOCK_MASK)

        if (currentItem != null) {
            (activity as AppCompatActivity?)!!.supportActionBar?.title =
                resources.getString(R.string.label_edit_item)
            binding.etName.setText(currentItem?.name)
            binding.etDueDate.setText(currentItem?.dueDate
                ?.parseToDate(Constants.PATTERN_EEE_MMM_DD_YYY)
                ?.parseToString(Constants.PATTERN_MM_DD_YYYY))
            binding.etDueTime.setText(currentItem?.dueTime)
        }

        binding.etDueDate.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        binding.etDueTime.setOnClickListener {
            getDateTimeCalendar()
            TimePickerDialog(requireContext(), this, hour, minute, false).show()
        }
    }

    private fun getDateTimeCalendar() {
        val now = Calendar.getInstance()

        this.day = now.get(Calendar.DAY_OF_MONTH)
        this.month = now.get(Calendar.MONTH)
        this.year = now.get(Calendar.YEAR)
        this.hour = now.get(Calendar.HOUR)
        this.minute = now.get(Calendar.MINUTE)
    }

    private fun collectData() {
        uiStateJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateFlow.collect { uiState ->
                when (uiState) {
                    is ItemViewModel.UiState.Loading -> {
                        clearFormErrors()
                        binding.progressBar.isVisible = true
                    }
                    is ItemViewModel.UiState.Success -> {
                        activity?.hideKeyboard()
                        binding.progressBar.isVisible = false
                        activity?.toast(uiState.successMessage)

                        if (currentItem != null) {
                            val action =
                                ItemFormFragmentDirections.navigateToItemDetails(currentItem!!)
                            findNavController().navigate(action)
                        } else {
                            val action = ItemFormFragmentDirections.navigateToItem()
                            findNavController().navigate(action)
                        }
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
            dueTime = binding.etDueTime.text.toString()
        }
        if (currentItem != null) {
            currentItem?.name = item.name
            currentItem?.dueDate = item.dueDate
            currentItem?.dueTime = item.dueTime
            viewModel.updateItem(currentItem!!)
        } else {
            viewModel.insertItem(item)
        }
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
            if (key == ItemViewModel.FIELD_DUE_TIME) {
                binding.tilDueTime.error = value
            }
        }
    }

    private fun clearFormErrors() {
        binding.tilName.error = null
        binding.tilDueDate.error = null
        binding.tilDueTime.error = null
    }
}