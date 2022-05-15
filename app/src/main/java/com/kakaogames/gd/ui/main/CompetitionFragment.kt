package com.kakaogames.gd.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakaogames.gd.adapter.CompetitionAdapter
import com.kakaogames.gd.databinding.FragmentCompetitionBinding
import com.kakaogames.gd.interfaces.ListInterface
import com.kakaogames.gd.utils.kakShowErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompetitionFragment : Fragment(), ListInterface<Int> {
    private var _binding: FragmentCompetitionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompetitionViewModel by viewModels()
    private lateinit var mAdapter: CompetitionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompetitionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
        initLoading()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    it?.let {
                        kakShowErrorDialog(requireContext(), { initLoading() }, it.message)
                        viewModel.uiState.update { null }
                    }
                }
            }
        }
        viewModel.competitions.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.pBar.visibility = View.GONE
            } else {
                binding.pBar.visibility = View.VISIBLE
            }
            mAdapter.submitList(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(id: Int) {
        val action = CompetitionFragmentDirections.actionCompetitionFragmentToScoresFragment(id)
        findNavController().navigate(action)
    }


    private fun initRv() = with(binding.rvCompetitions) {
        mAdapter = CompetitionAdapter(this@CompetitionFragment)
        adapter = mAdapter
        layoutManager = LinearLayoutManager(requireContext())
        setHasFixedSize(true)
    }

    private fun initLoading() {
        viewModel.getCompetitions()
    }
}