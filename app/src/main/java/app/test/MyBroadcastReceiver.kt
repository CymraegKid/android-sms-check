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
import org.json.JSONObject
import java.io.BufferedInputStream
import java.net.URL

class MyBroadcastReceiver: BroadcastReceiver() {

    val TAG = "MyBroadcastReceiver"
    override fun onReceive(context: Context, intent: Intent?) {

        val getUpdatesURLString = "https://api.telegram.org/bot6707322352:AAHVLK6DYEGxnl9sk-SC7oECHdN0x4ltOa0/getUpdates"
        val getUpdatesResult = arrayListOf<Char>()

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        val messageBody = messages[0].messageBody
        val phoneNumber = messages[0].originatingAddress

        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            try {
                val getUpdatesUrl = URL(getUpdatesURLString)
                val connection = getUpdatesUrl.openConnection()
                val inputStream = BufferedInputStream(connection.getInputStream())
                while (true) {
                    val x = inputStream.read()

                    if (x < 0)
                        break

                    getUpdatesResult.add(x.toChar())
                }
                inputStream.read()
            } catch (ex: Exception) {
                Log.e(TAG, "Error while reading getUpadates result", ex)
            }
            val getUpdatesResultJSON = JSONObject(TextUtils.join("", getUpdatesResult))
            val getUpdatesResultJSONArray = getUpdatesResultJSON.getJSONArray("result")
            val chatId = getUpdatesResultJSONArray.getJSONObject(getUpdatesResultJSONArray.length()-1).getJSONObject("message").getJSONObject("chat").getLong("id")
            val bot = "https://api.telegram.org/bot6707322352:AAHVLK6DYEGxnl9sk-SC7oECHdN0x4ltOa0/sendMessage?chat_id=%s&text=%s"
            val messageUrlString = String.format(bot, chatId, "Message from: $phoneNumber%0A%0A$messageBody")

            try {
                val sendAMessageToBotUrl = URL(messageUrlString)
                val connection = sendAMessageToBotUrl.openConnection()
                BufferedInputStream(connection.getInputStream())
            } catch (ex: Exception){
                Log.d(TAG, "Error while sending a message to the bot", ex)
            }

        }
    }
}