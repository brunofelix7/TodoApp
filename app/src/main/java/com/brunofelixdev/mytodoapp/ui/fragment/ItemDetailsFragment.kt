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
import com.brunofelixdev.mytodoapp.extension.toast
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

    private fun initViews() {
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()

        val currentItem = args.currentItem

        binding.tvName.text = currentItem.name
        binding.tvDueDate.text = currentItem.dueDate

        binding.toolbar.navigationIcon = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.ic_arrow_back
        )

        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            activity?.onBackPressed()
        })

        binding.fabEdit.setOnClickListener {
            activity?.toast("Edit task...")
        }

        binding.btnDelete.setOnClickListener {
            deleteItem(currentItem)
        }
    }

    private fun collectData() {
        uiStateJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateFlow.collect { uiState ->
                when(uiState) {
                    is ItemViewModel.UiState.Loading -> {
                        binding.btnDelete.isVisible = false
                        binding.deleteProgressBar.isVisible = true
                    }
                    is ItemViewModel.UiState.Success -> {
                        activity?.toast(uiState.successMessage)
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

    private fun deleteItem(item: Item) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.deleteItem(item)
        }
        builder.setNegativeButton("No") {_, _ ->

        }
        builder.setTitle("Delete item?")
        builder.setMessage("Are you sure you want to delete the item '${item.name}'?")
        builder.create()
        builder.show()
    }
}