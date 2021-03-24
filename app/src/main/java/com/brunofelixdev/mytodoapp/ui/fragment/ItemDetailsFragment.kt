package com.brunofelixdev.mytodoapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.databinding.FragmentItemDetailsBinding
import com.brunofelixdev.mytodoapp.extension.toast

class ItemDetailsFragment : Fragment() {

    private var _binding: FragmentItemDetailsBinding? = null
    private val binding: FragmentItemDetailsBinding get() = _binding!!

    private val args: ItemDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemDetailsBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
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
            activity?.toast("Delete task...")
        }
    }
}