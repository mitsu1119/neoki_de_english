package io.github.mitsu1119.neoki_de_english.conn

import android.util.Log
import io.github.mitsu1119.neoki_de_english.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.CoroutineContext

class Server(val coroutineContext: CoroutineContext) {
    private val okHttpClient = OkHttpClient.Builder().build()

    //単語帳一覧取得
    fun getWBlist(): String {
        var str:String = ""
        CoroutineScope(coroutineContext).launch {
            val url = HttpUrl.Builder()
                .scheme("https")
                .host("kudo1122.pythonanywhere.com")
                .addPathSegment("Wordbook")
                .addPathSegment("getList")
                //.addQueryParameter("name","test2")
                .build()
            val request = Request.Builder()
                .url(url)
                .build()
            val result = withContext(Dispatchers.IO) {
                okHttpClient.newCall(request).execute().use { response ->
                    if(response.isSuccessful) response.body?.string()
                    else "falsed/ code: ${response.code} / message: ${response.message}"
                }
            }

            Log.v("yey", "yey")
            str = result.toString()
            Log.v("yey", "tangochou: $str")

            /*
            MainActivity().runOnUiThread {
                binding.progressBar.show()
                binding.textView.text = str//単語帳の名前リストを取得する関数
                binding.progressBar.hide()
            }
          */
        }
        return str
    }
}