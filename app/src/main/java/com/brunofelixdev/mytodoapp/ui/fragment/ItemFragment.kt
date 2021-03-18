package com.brunofelixdev.mytodoapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.adapter.ItemAdapter
import com.brunofelixdev.mytodoapp.databinding.FragmentItemBinding
import com.brunofelixdev.mytodoapp.extension.toast
import com.brunofelixdev.mytodoapp.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ItemFragment : Fragment() {

    @Inject
    lateinit var adapter: ItemAdapter

    private var _binding: FragmentItemBinding? = null

    private val binding: FragmentItemBinding get() = _binding!!

    private val viewModel: ItemViewModel by viewModels()

    private var uiStateJob: Job? = null

    companion object {
        private val TAG: String = ItemFragment::class.java.simpleName
        lateinit var optionsMenu: Menu
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        initializeViews()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()

        uiStateJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiStateFlow.collect { uiState ->
                when(uiState) {
                    is ItemViewModel.UiState.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.rvItems.isVisible = false
                    }
                    is ItemViewModel.UiState.Success -> {
                        binding.progressBar.isVisible = false
                        binding.rvItems.isVisible = true
                        adapter.submitData( uiState.items)
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onStop() {
        uiStateJob?.cancel()
        super.onStop()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        optionsMenu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                activity?.toast("Sort by...")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeViews() {
        binding.fab.setOnClickListener {
            val action = ItemFragmentDirections.navigateToAddItem()
            findNavController().navigate(action)
        }

        binding.rvItems.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvItems.adapter = adapter

        viewModel.fetchItems()
    }
}