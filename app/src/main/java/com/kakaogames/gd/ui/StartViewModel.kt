package com.kakaogames.gd.ui

import android.content.Context
import android.net.Uri
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
    private val kakMainLink = WebLink()

    fun kakCollectWebLink(context: Context): String {
        val link = kakMainLink.kakCollectWebLink(context)
        webLinkRepositoryImpl.link = link
        return link
    }

    fun kakGetCachedLink() = webLinkRepositoryImpl.link

    fun kakGetMediaSource(): String? = kakMainLink.kakMediaSource

    fun kakGetOrganicAccess(): Boolean? = kakMainLink.kakOrganicAccess

    fun kakSetOrganicAndUrl(organic: Boolean, url: String) {
        kakMainLink.kakOrganicAccess = organic
        kakMainLink.kakUrl = url
    }

    fun kakSetGoogleAID(googleAID: String) {
        kakMainLink.kakGoogleId = googleAID
        OneSignal.setExternalUserId(googleAID)
    }

    fun kakSetDeepLink(uri: Uri?) {
        kakMainLink.kakDeepLink = uri?.toString()

        kakMainLink.kakDeepLink?.let {
            val arrayDeepLink = it.split("//")
            kakMainLink.kakSubAll = arrayDeepLink[1].split("_")
        }
    }

    fun kakSetAppsFlyerUID(appsUID: String?) {
        kakMainLink.kakAppsFlyerUserId = appsUID
    }

    fun kakSetAppsFlyerStatus(value: String) {
        val organicWithBigO = "qfsknsc".vigenere().replaceFirstChar { it.uppercase() }
        if (value == organicWithBigO && kakMainLink.kakDeepLink == null
        ) {
            kakMainLink.kakMediaSource = "qfsknsc".vigenere()
        }
    }

    fun kakSetAppsFlyerCampaign(value: String) {
        kakMainLink.kakCampaign = value
        kakMainLink.kakCampaign?.let {
            kakMainLink.kakSubAll = it.split("_")
        }
    }

    fun kakSetAppsFlyerMediaSource(value: String) {
        kakMainLink.kakMediaSource = value
    }

    fun kakSetAppsFlyerChannel(value: String) {
        kakMainLink.kakAfChannel = value
    }
}