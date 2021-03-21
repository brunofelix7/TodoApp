package com.brunofelixdev.mytodoapp.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.rv.adapter.ItemAdapter
import com.brunofelixdev.mytodoapp.rv.adapter.ItemLoadStateAdapter
import com.brunofelixdev.mytodoapp.databinding.FragmentItemBinding
import com.brunofelixdev.mytodoapp.extension.toast
import com.brunofelixdev.mytodoapp.rv.listener.ItemClickListener
import com.brunofelixdev.mytodoapp.rv.viewholder.ItemViewHolder
import com.brunofelixdev.mytodoapp.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ItemFragment : Fragment(), ItemClickListener {

    private var _binding: FragmentItemBinding? = null
    private val binding: FragmentItemBinding get() = _binding!!

    private val viewModel: ItemViewModel by viewModels()

    private var uiStateJob: Job? = null

    @Inject
    lateinit var adapter: ItemAdapter

    companion object {
        private val TAG: String = ItemFragment::class.java.simpleName
        lateinit var optionsMenu: Menu
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        initViews()
        initAdapter()
        collectData()
        return binding.root
    }

    override fun onDestroyView() {
        uiStateJob?.cancel()
        super.onDestroyView()
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

    private fun initViews() {
        binding.fab.setOnClickListener {
            val action = ItemFragmentDirections.navigateToAddItem()
            findNavController().navigate(action)
        }

        binding.rvItems.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun initAdapter() {
        adapter.listener = this
        binding.rvItems.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ItemLoadStateAdapter { adapter.retry() },
            footer = ItemLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            binding.rvItems.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                activity?.toast("\uD83D\uDE28 Oops! ${it.error}")
            }
        }
    }

    private fun collectData() {
        uiStateJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.itemsList.collect {
                adapter.submitData(it)
            }
        }
    }

    override fun onCheckedClick(item: Item, cbItem: CheckBox) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.checkItemAsDone(item.id)
        }
        builder.setNegativeButton("No") {_, _ ->
            cbItem.isChecked = false
        }
        builder.setOnDismissListener {
            cbItem.isChecked = false
        }
        builder.setTitle("Mark as done?")
        builder.setMessage("Are you sure you want to mark as done the item '${item.name}'?")
        builder.create()
        builder.show()
    }

    override fun onItemClick(item: Item) {
        activity?.toast("Item clicked: ${item.name}")
    }
}