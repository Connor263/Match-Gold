package com.kakaogames.gd.data.model.web

import android.content.Context
import com.kakaogames.gd.R
import com.kakaogames.gd.utils.vigenere


data class KakWebLink(
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
        val kakResources = context.resources
        val kakPackageName = context.packageName
        val kakAppsFlyerDevKey = kakResources.getString(R.string.apps_dev_key)
        val kakFbToken = kakResources.getString(R.string.fb_at)
        val kakFbAppId = kakResources.getString(R.string.fb_app_id)

        var kakIndex = 0
        val kakSubsString = kakSubAll.joinToString(separator = "") {
            kakIndex++
            "&sub$kakIndex=$it"
        }

        val kakStrMediaSource = "?ospsa_coixcq=".vigenere()
        val kakStrGoogleId = "&icaqlo_arod=".vigenere()
        val kakStrAppsFlyerUserId = "&ct_gcebir=".vigenere()
        val kakStrPackageName = "&diznlo=".vigenere()
        val kakStrAppsFlyerDevKey = "&fsh_uei=".vigenere()
        val kakStrFbToken = "&hp_md=".vigenere()
        val kakStrFbAppId = "&hp_mzp_sd=".vigenere()
        val kakStrAfChannel = "&ct_oraxnsr=".vigenere()
        val kakStrCampaign = "&eoyzasgb=".vigenere()


        return "${this.kakUrl}" +
                "$kakStrMediaSource${this.kakMediaSource}" +
                "$kakStrGoogleId${this.kakGoogleId}" +
                "$kakStrAppsFlyerUserId${this.kakAppsFlyerUserId}" +
                "$kakStrPackageName$kakPackageName" +
                "$kakStrAppsFlyerDevKey$kakAppsFlyerDevKey" +
                "$kakStrFbToken$kakFbToken" +
                "$kakStrFbAppId$kakFbAppId" +
                "$kakStrAfChannel${this.kakAfChannel}" +
                "$kakStrCampaign${this.kakCampaign}" +
                kakSubsString
    }
}
