package com.example.jhomasinas.mshopping

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import com.example.jhomasinas.mshopping.Activity.MainActivity

class PushyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationTitle = "MShopping"
        var notificationText = "Request Update"

        if (intent.getStringExtra("message") != null) {
            notificationText = intent.getStringExtra("message")
        }

        val builder = NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setVibrate(longArrayOf(0, 400, 250, 400))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(PendingIntent.getActivity(context, 60, Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }
}
