package com.brunofelixdev.mytodoapp.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.brunofelixdev.mytodoapp.databinding.FragmentAddItemBinding
import com.brunofelixdev.mytodoapp.extension.hideKeyboard
import com.brunofelixdev.mytodoapp.extension.myCustomMask
import java.util.*

class AddItemFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentAddItemBinding? = null
    private val binding: FragmentAddItemBinding get() = _binding!!

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    companion object {
        private const val CALENDAR_MASK = "##/##/####"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.hideKeyboard()
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

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.day = dayOfMonth
        this.month = month
        this.year = year

        val pickedDay = if (dayOfMonth < 10) "0${this.day}" else dayOfMonth.toString()
        val pickedMonth = if (month < 10) "0${this.month}" else month.toString()
        val pickedYear = year.toString()

        getDateTimeCalendar()

        binding.etDueDate.setText("${pickedDay}/${pickedMonth}/${pickedYear}")
    }
}