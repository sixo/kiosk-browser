package eu.sisik.kioskbrowser

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import java.lang.Exception

/**
 * Copyright (c) 2019 by Roman Sisik. All rights reserved.
 */
class DevAdminReceiver: DeviceAdminReceiver() {
    override fun onEnabled(context: Context?, intent: Intent?) {
        super.onEnabled(context, intent)
        Log.d(TAG, "Device Owner Enabled")

        try {
            makeLockTaskPackage(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun makeLockTaskPackage(context: Context?) {

        val dpm = context?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?
        val cn = ComponentName(context, DevAdminReceiver::class.java!!)
        dpm?.setLockTaskPackages(cn, arrayOf(context?.packageName))
    }

    companion object {
        val TAG = "DevAdminReceiver"
    }
}