package com.kakaogames.gd.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.kakaogames.gd.data.model.web.WebLink
import com.kakaogames.gd.data.source.local.repo.WebLinkRepositoryImpl
import com.kakaogames.gd.utils.vigenere
import com.onesignal.OneSignal
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val webLinkRepositoryImpl: WebLinkRepositoryImpl
) : ViewModel() {
    private val mainLink = WebLink()

    fun collectWebLink(context: Context): String {
        val link = mainLink.collectWebLink(context)
        Log.d("TAG", "collectWebLink: $link")
        webLinkRepositoryImpl.link = link
        return link
    }

    fun getCachedLink() = webLinkRepositoryImpl.link

    fun getMediaSource(): String? = mainLink.mediaSource

    fun getOrganicAccess(): Boolean? = mainLink.organicAccess

    fun setOrganicAndUrl(organic: Boolean, url: String) {
        mainLink.organicAccess = organic
        mainLink.url = url
        Log.d("TAG", "setOrganicAndUrl: $organic $url")
    }

    fun setGoogleAID(googleAID: String) {
        mainLink.googleId = googleAID
        OneSignal.setExternalUserId(googleAID)
        Log.d("TAG", "setGoogleAID: $googleAID")
    }

    fun setDeepLink(uri: Uri?) {
        mainLink.deepLink = uri?.toString()

        mainLink.deepLink?.let {
            val arrayDeepLink = it.split("//")
            mainLink.subAll = arrayDeepLink[1].split("_")
        }
        Log.d("TAG", "setDeepLink: ${mainLink.deepLink} ${mainLink.subAll} ")
    }

    fun setAppsFlyerUID(appsUID: String?) {
        mainLink.appsFlyerUserId = appsUID
        Log.d("TAG", "setAppsFlyerUID: $appsUID")
    }

    fun setAppsFlyerStatus(value: String) {
        val organicWithBigO = "qfsknsc".vigenere().replaceFirstChar { it.uppercase() }
        if (value == organicWithBigO && mainLink.deepLink == null
        ) {
            mainLink.mediaSource = "qfsknsc".vigenere()
        }
        Log.d("TAG", "setAppsFlyerStatus: $value")
    }

    fun setAppsFlyerCampaign(value: String) {
        mainLink.campaign = value
        mainLink.campaign?.let {
            mainLink.subAll = it.split("_")
        }
    }

    fun setAppsFlyerMediaSource(value: String) {
        mainLink.mediaSource = value
    }

    fun setAppsFlyerChannel(value: String) {
        mainLink.afChannel = value
    }
}