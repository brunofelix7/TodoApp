package com.brunofelixdev.mytodoapp.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
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
import com.brunofelixdev.mytodoapp.data.pref.getItemsFilter
import com.brunofelixdev.mytodoapp.data.pref.setItemsFilter
import com.brunofelixdev.mytodoapp.databinding.FragmentItemBinding
import com.brunofelixdev.mytodoapp.extension.toast
import com.brunofelixdev.mytodoapp.rv.adapter.ItemAdapter
import com.brunofelixdev.mytodoapp.rv.adapter.ItemLoadStateAdapter
import com.brunofelixdev.mytodoapp.rv.listener.ItemClickListener
import com.brunofelixdev.mytodoapp.util.Constants
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
                sortByDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        (activity as AppCompatActivity?)!!.supportActionBar?.show()

        val filter = getItemsFilter(requireContext())
        if (filter != null) {
            when(filter) {
                Constants.SORT_BY_NAME -> {
                    binding.tvSortedBy.text = activity?.resources?.getString(R.string.txt_sorted_by_name)
                }
                Constants.SORT_BY_DUE_DATE -> {
                    binding.tvSortedBy.text = activity?.resources?.getString(R.string.txt_sorted_by_due_date)
                }
            }
        }

        binding.fab.setOnClickListener {
            val action = ItemFragmentDirections.navigateToItemForm(null)
            findNavController().navigate(action)
        }

        binding.rvItems.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    @SuppressLint("SetTextI18n")
    private fun initAdapter() {
        adapter.listener = this
        adapter.context = requireContext()
        binding.rvItems.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ItemLoadStateAdapter { adapter.retry() },
            footer = ItemLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            binding.tvListTitle.text = "${adapter.itemCount} items"
            binding.tvListTitle.isVisible = adapter.itemCount > 0
            binding.tvSortedBy.isVisible = adapter.itemCount > 0
            binding.includEmptyList.root.isVisible = (adapter.itemCount == 0 &&
                    loadState.source.refresh is LoadState.NotLoading)
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

    private fun sortByDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val itemsArray = resources.getStringArray(R.array.sort_by_array)
        val checkedItem = -1

        builder.setTitle(activity?.resources?.getString(R.string.title_dialog_sort_by))
        builder.setSingleChoiceItems(itemsArray, checkedItem) { _, which ->
            val item = itemsArray[which]
            setItemsFilter(requireContext(), item)
        }
        builder.setPositiveButton(activity?.resources?.getString(R.string.btn_dialog_ok)) {_, _ ->
            initAdapter()
            collectData()
        }
        builder.setNeutralButton(activity?.resources?.getString(R.string.btn_dialog_cancel)) { dialog, _ ->
            dialog.cancel()
        }
        builder.create()
        builder.show()
    }

    override fun onCheckedClick(item: Item, cbItem: CheckBox) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(activity?.resources?.getString(R.string.btn_dialog_yes)) { _, _ ->
            viewModel.checkItemAsDone(item)
        }
        builder.setNegativeButton(activity?.resources?.getString(R.string.btn_dialog_no)) {_, _ ->
            cbItem.isChecked = false
        }
        builder.setOnDismissListener {
            cbItem.isChecked = false
        }
        builder.setTitle(activity?.resources?.getString(R.string.title_dialog_item_done))
        builder.setMessage("${activity?.resources?.getString(R.string.txt_dialog_item_done)} '${item.name}'?")
        builder.create()
        builder.show()
    }

    override fun onItemClick(item: Item) {
        val action = ItemFragmentDirections.navigateToItemDetails(item)
        findNavController().navigate(action)
    }
}