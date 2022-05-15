package com.kakaogames.gd.data.model.web

import android.content.Context
import com.kakaogames.gd.R
import com.kakaogames.gd.utils.vigenere


data class WebLink(
    var kakGoogleId: String? = null,
    var kakAppsFlyerUserId: String? = null,
    var kakSubAll: List<String> = listOf("", "", "", "", "", "", "", "", "", ""),
    var kakDeepLink: String? = null,
    var kakMediaSource: String? = null,
    var kakAfChannel: String? = null,
    var kakCampaign: String? = null,
    var kakUrl: String? = null,
    var kakOrganicAccess: Boolean? = null
) {
    fun kakCollectWebLink(context: Context): String {
        val resources = context.resources
        val packageName = context.packageName
        val appsFlyerDevKey = resources.getString(R.string.apps_dev_key)
        val fbToken = resources.getString(R.string.fb_at)
        val fbAppId = resources.getString(R.string.fb_app_id)

        var index = 0
        val subsString = kakSubAll.joinToString(separator = "") {
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


        return "${this.kakUrl}" +
                "$strMediaSource${this.kakMediaSource}" +
                "$strGoogleId${this.kakGoogleId}" +
                "$strAppsFlyerUserId${this.kakAppsFlyerUserId}" +
                "$strPackageName$packageName" +
                "$strAppsFlyerDevKey$appsFlyerDevKey" +
                "$strFbToken$fbToken" +
                "$strFbAppId$fbAppId" +
                "$strAfChannel${this.kakAfChannel}" +
                "$strCampaign${this.kakCampaign}" +
                subsString
    }
}
