package eu.sisik.kioskbrowser

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkIfDeviceOwner()
        initWebView()
    }

    override fun onResume() {
        super.onResume()

        tryToStartLockTask()
    }

    private fun checkIfDeviceOwner() {

        val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        if (!dpm.isDeviceOwnerApp(packageName)) {
            Toast.makeText(this, getString(R.string.err_not_device_owner), Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun initWebView() {

        webView.webViewClient = KioskWebViewClient()
        webView.loadUrl(MY_URL)
    }

    private fun tryToStartLockTask() {
        try {

            val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            if (dpm.isDeviceOwnerApp(packageName)) {
                // Allow locktask for my app if not already allowed
                if (!dpm.isLockTaskPermitted(packageName)) {
                    val cn = ComponentName(this, DevAdminReceiver::class.java!!)
                    dpm?.setLockTaskPackages(cn, arrayOf(packageName))
                }

                startLockTask()

            } else {
                Toast.makeText(this, getString(R.string.err_locktask_not_permitted), Toast.LENGTH_LONG).show()
                finish()
            }

        } catch (e: Exception) {

            // Cannot start locktask... try a bit later
            Handler(mainLooper).postDelayed({

                tryToStartLockTask()
            }, 2000)
        }
    }

    private fun hideApps() {

        val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?

        val packagesToHide = arrayOf(
            "com.android.launcher3",
            "some.other.package"
        )

        for (pkg in packagesToHide)
            dpm?.setApplicationHidden(componentName, pkg, false)
    }


    // WebViewClient will handle the blacklisting/whitelisting of URLs
    internal class KioskWebViewClient : WebViewClient() {

        // Whitelisted hosts
        private val allowedHosts = arrayOf(
            "sisik.eu",
            "www.sisik.eu"
        )

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

            // Allow to load only whitelisted hosts
            if (allowedHosts.contains(request?.url?.host))
                return false

            // Not allowed to load this url - do nothing..
            return true
        }
    }


    companion object {

        // This is the page that will be loaded by kiosk app
        private const val MY_URL = "https://sisik.eu"
    }
}
