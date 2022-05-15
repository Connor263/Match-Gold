package com.kakaogames.gd.ui.web

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kakaogames.gd.databinding.FragmentWebViewBinding
import com.kakaogames.gd.utils.vigenere
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFragment : Fragment() {
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    private var kakFileData: ValueCallback<Uri>? = null
    private var kakFilePath: ValueCallback<Array<Uri>>? = null
    private val startFileChooseForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                kakProcessFileChooseResult(result.data)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.webView.apply {
                    if (isFocused && canGoBack()) goBack()
                }
            }
        })

        val navArgs: WebViewFragmentArgs by navArgs()
        val link = navArgs.link
        kakLoadWebView(link)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled")
    fun kakLoadWebView(link: String) = with(binding.webView) {
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url!!.contains("cdbkfks3t".vigenere()) || url.contains("fwekbver.ntyp".vigenere())) {
                    val action = WebViewFragmentDirections.actionGlobalCompetitionFragment()
                    findNavController().navigate(action)
                }
            }
        }
        webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                kakFilePath = filePathCallback
                Intent(Intent.ACTION_GET_CONTENT).run {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "kamqe/*".vigenere()
                    startFileChooseForResult.launch(this)
                }
                return true
            }
        }

        settings.apply {
            javaScriptEnabled = true
            allowContentAccess = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(false)
            builtInZoomControls = true
            useWideViewPort = true
            setAppCacheEnabled(true)
            displayZoomControls = false
            allowFileAccess = true
            lightTouchEnabled = true
        }
        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(this@with, true)
        }
        clearCache(false)
        loadUrl(link)
    }

    private fun kakProcessFileChooseResult(kakData: Intent?) {
        if (kakFileData == null && kakFilePath == null) return

        var kakResultData: Uri? = null
        var kakResultsFilePath: Array<Uri>? = null
        if (kakData != null) {
            kakResultData = kakData.data
            kakResultsFilePath = arrayOf(Uri.parse(kakData.dataString))
        }
        kakFileData?.onReceiveValue(kakResultData)
        kakFilePath?.onReceiveValue(kakResultsFilePath)
    }
}