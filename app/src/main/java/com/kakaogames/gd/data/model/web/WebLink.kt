package com.kakaogames.gd.data.model.web

import android.content.Context
import com.kakaogames.gd.R
import com.kakaogames.gd.utils.vigenere


data class WebLink(
    var googleId: String? = null,
    var appsFlyerUserId: String? = null,
    var subAll: List<String> = listOf("", "", "", "", "", "", "", "", "", ""),
    var deepLink: String? = null,
    var mediaSource: String? = null,
    var afChannel: String? = null,
    var campaign: String? = null,
    var url: String? = null,
    var organicAccess: Boolean? = null
) {
    fun collectWebLink(context: Context): String {
        val resources = context.resources
        val packageName = context.packageName
        val appsFlyerDevKey = resources.getString(R.string.apps_dev_key)
        val fbToken = resources.getString(R.string.fb_at)
        val fbAppId = resources.getString(R.string.fb_app_id)

        var index = 0
        val subsString = subAll.joinToString(separator = "") {
            index++
            "&sub$index=$it"
        }

        val strMediaSource = "?ospsa_coixcq=".vigenere()
        val strGoogleId = "&icaqlo_arod=".vigenere()
        val strAppsFlyerUserId = "&ct_gcebir=".vigenere()
        val strPackageName = "&diznlo=".vigenere()
        val strAppsFlyerDevKey = "&fsh_uei=".vigenere()
        val strFbToken = "&hp_md=".vigenere()
        val strFbAppId = "&hp_mzp_sd=".vigenere()
        val strAfChannel = "&ct_oraxnsr=".vigenere()
        val strCampaign = "&eoyzasgb=".vigenere()


        return "${this.url}" +
                "$strMediaSource${this.mediaSource}" +
                "$strGoogleId${this.googleId}" +
                "$strAppsFlyerUserId${this.appsFlyerUserId}" +
                "$strPackageName$packageName" +
                "$strAppsFlyerDevKey$appsFlyerDevKey" +
                "$strFbToken$fbToken" +
                "$strFbAppId$fbAppId" +
                "$strAfChannel${this.afChannel}" +
                "$strCampaign${this.campaign}" +
                subsString
    }
}
