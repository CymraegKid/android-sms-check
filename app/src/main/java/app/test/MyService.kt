package app.test

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class MyService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        start()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(){

        val broadcastReceiver = MyBroadcastReceiver()
        val filter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        val receiverFlags = ContextCompat.RECEIVER_EXPORTED

        ContextCompat.registerReceiver(
            this,
            broadcastReceiver,
            filter,
            receiverFlags
        )

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "1",
                "MAIN",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this,"1")
            .setSmallIcon(R.drawable.sms_app_icon)
            .setContentTitle("Your messages are caught by this app now")
            .build()

        startForeground(1, notification)

    }
}