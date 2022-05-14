package com.kakaogames.gd.data.source.local.repo

import android.content.Context
import com.kakaogames.gd.interfaces.WebLinkRepository
import com.kakaogames.gd.utils.CACHED_LINK
import com.kakaogames.gd.utils.WEB_LINK_SHARED_PREF
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebLinkRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : WebLinkRepository {
    private val sharedPref =
        context.getSharedPreferences(WEB_LINK_SHARED_PREF, Context.MODE_PRIVATE)
    override var link: String
        get() = sharedPref.getString(CACHED_LINK, null) ?: ""
        set(value) {
            sharedPref.edit().putString(CACHED_LINK, value).apply()
        }
}