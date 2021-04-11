package com.brunofelixdev.mytodoapp.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.databinding.FragmentItemDetailsBinding
import com.brunofelixdev.mytodoapp.extension.*
import com.brunofelixdev.mytodoapp.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemDetailsFragment : Fragment() {

    private var _binding: FragmentItemDetailsBinding? = null
    private val binding: FragmentItemDetailsBinding get() = _binding!!

    private val args: ItemDetailsFragmentArgs by navArgs()

    private val viewModel: ItemViewModel by viewModels()

    private var uiStateJob: Job? = null

    companion object {
        private val TAG = ItemDetailsFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentItemDetailsBinding.inflate(inflater, container, false)
        initViews()
        collectData()
        return binding.root
    }

    override fun onDestroyView() {
        uiStateJob?.cancel()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()

        //  If the user changes the theme when this screen is open.
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
    }

    private fun initViews() {
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()

        val currentItem = args.currentItem

        currentItem.apply {
            binding.tvName.text = currentItem.name
            binding.tvDueDate.text = currentItem.dueDateTime.getDueDateView()
            binding.tvDueTime.text = currentItem.dueDateTime.getDueTime()
            binding.collapsingToolbar.title = currentItem.name
        }

        checkIfDateHasPassed(currentItem)

        binding.toolbar.navigationIcon = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.ic_arrow_back
        )

        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            activity?.onBackPressed()
        })

        binding.fabEdit.setOnClickListener {
            updateItem(currentItem)
        }

        binding.btnDelete.setOnClickListener {
            deleteItem(currentItem)
        }
    }

    private fun collectData() {
        uiStateJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { uiState ->
                when(uiState) {
                    is ItemViewModel.UiState.Loading -> {
                        binding.btnDelete.isVisible = false
                        binding.deleteProgressBar.isVisible = true
                    }
                    is ItemViewModel.UiState.Success -> {
                        activity?.toast(uiState.message ?: "")
                        val action = ItemDetailsFragmentDirections.navigateToItem()
                        findNavController().navigate(action)
                    }
                    is ItemViewModel.UiState.Error -> {
                        binding.deleteProgressBar.isVisible = false
                        binding.btnDelete.isVisible = true
                        activity?.toast(uiState.errorMessage)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun updateItem(item: Item) {
        val action = ItemDetailsFragmentDirections.navigateToItemForm(item)
        findNavController().navigate(action)
    }

    private fun deleteItem(item: Item) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(activity?.resources?.getString(R.string.btn_dialog_yes)) { _, _ ->
            viewModel.deleteItem(item)
        }
        builder.setNegativeButton(activity?.resources?.getString(R.string.btn_dialog_no)) {_, _ ->

        }
        builder.setTitle(activity?.resources?.getString(R.string.title_dialog_item_delete))
        builder.setMessage("${activity?.resources?.getString(R.string.txt_dialog_item_delete)} '${item.name}'?")
        builder.create()
        builder.show()
    }

    private fun checkIfDateHasPassed(item: Item) {
        item.apply {
            val duration = dueDateTime.getDurationBetweenDates()

            if (duration < 0) {
                binding.tvName.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_500))
                binding.tvDueDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_500))
                binding.tvDueTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_500))
            }
        }
    }
}