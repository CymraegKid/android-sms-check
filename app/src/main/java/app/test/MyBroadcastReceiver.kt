package app.test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.text.TextUtils
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.net.URL

class MyBroadcastReceiver: BroadcastReceiver() {

    val TAG = "MyBroadcastReceiver"
    override fun onReceive(context: Context, intent: Intent?) {

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        val messageBody = messages[0].messageBody
        val phoneNumber = messages[0].originatingAddress

//        val getUpdatesURLString = "https://api.telegram.org/bot6707322352:AAHVLK6DYEGxnl9sk-SC7oECHdN0x4ltOa0/getUpdates"
//        val getUpdatesResult = arrayListOf<Char>()
//
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {

//            try {
//                val getUpdatesUrl = URL(getUpdatesURLString)
//                val connection = getUpdatesUrl.openConnection()
//                val inputStream = BufferedInputStream(connection.getInputStream())
//                val portionOfGetUpdates = ByteArray(1024)
//                while (true) {
//                    val readingFromGetUpdatesResult = inputStream.read(portionOfGetUpdates)
//
//                    if (readingFromGetUpdatesResult < 0)
//                        break
//
//                    for(character in portionOfGetUpdates)
//                        getUpdatesResult.add(character.toInt().toChar())
//
//                }
//            } catch (ex: Exception) {
//                Log.e(TAG, "Error while reading getUpadates result", ex)
//            }
//            val getUpdatesResultJSON = JSONObject(TextUtils.join("", getUpdatesResult))
//            val getUpdatesResultJSONArray = getUpdatesResultJSON.getJSONArray("result")
//            val chatId = getUpdatesResultJSONArray.getJSONObject(getUpdatesResultJSONArray.length()-1).getJSONObject("message").getJSONObject("chat").getLong("id")

            val bot = "https://api.telegram.org/bot${MainActivity.botToken}/sendMessage?chat_id=${MainActivity.chatId}&text=%s"
            val messageUrlString = String.format(bot, "Сообщение от: $phoneNumber%0A%0A$messageBody")

            try {
                val sendAMessageToBotUrl = URL(messageUrlString)
                val connection = sendAMessageToBotUrl.openConnection()
                BufferedInputStream(connection.getInputStream())
            } catch (ex: Exception) {
                Log.e(TAG, "Error while sending a message to the bot", ex)
            }
        }
    }
}