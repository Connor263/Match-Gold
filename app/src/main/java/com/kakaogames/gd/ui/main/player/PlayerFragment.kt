package com.kakaogames.gd.ui.main.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.kakaogames.gd.R
import com.kakaogames.gd.databinding.FragmentPlayerBinding
import com.kakaogames.gd.utils.kakShowErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lastUpdatedFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            Locale.getDefault()
        )

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
        viewModel.playerInfo.observe(viewLifecycleOwner) {
            binding.apply {
                if (it != null) {
                    pBar.visibility = View.GONE
                    mCv.visibility = View.VISIBLE
                } else {
                    pBar.visibility = View.VISIBLE
                    mCv.visibility = View.INVISIBLE
                }
                tvLastUpdated.text =
                    resources.getString(R.string.last_updated, it.lastUpdated?.let {
                        DateFormat.getDateInstance(DateFormat.SHORT)
                            .format(lastUpdatedFormat.parse(it) ?: "")
                    })

                tvPosition.text = resources.getString(R.string.position, it.position)

                tvFirstName.text = it.firstName

                tvLastName.text = it.lastName

                tvName.text = resources.getString(R.string.name, it.name)

                tvNationality.text = resources.getString(R.string.nationality, it.nationality)

                tvDateBirth.text = resources.getString(R.string.date_of_birth, it.dateOfBirth)

                tvCountryBirth.text = resources.getString(R.string.country_birth, it.countryOfBirth)

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initLoading() {
        val navArgs: PlayerFragmentArgs by navArgs()
        viewModel.getPlayerInfo(navArgs.playerId)
    }
}