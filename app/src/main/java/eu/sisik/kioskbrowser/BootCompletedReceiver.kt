package eu.sisik.kioskbrowser

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Copyright (c) 2019 by Roman Sisik. All rights reserved.
 */
class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val i = Intent(context, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(i)
    }
}