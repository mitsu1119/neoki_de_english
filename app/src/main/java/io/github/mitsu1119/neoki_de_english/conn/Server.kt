package io.github.mitsu1119.neoki_de_english.conn

import android.util.Log
import io.github.mitsu1119.neoki_de_english.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class Server(val coroutineContext: CoroutineContext) {
    private val okHttpClient = OkHttpClient.Builder().build()

    //単語帳一覧取得
    fun getWBlist(): String {
        var str:String = ""
        runBlocking {
            val url = HttpUrl.Builder()
                .scheme("https")
                .host("kudo1122.pythonanywhere.com")
                .addPathSegment("Wordbook")
                .addPathSegment("getList")
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

            str = result.toString()
            Log.v("yey", str)

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


    //単語帳をアップロードする関数
    fun uploadWB(name:String, filesDir: File){
        var file = "dics/${name}/words.txt"
        val readFile = File(filesDir, file)
        if(!readFile.exists()){
            Log.v("yey", "${name}.txt がみつかりません")
        } else {
            var contents = readFile.bufferedReader().use(BufferedReader::readText)
            val url = HttpUrl.Builder()
                .scheme("https")
                .host("kudo1122.pythonanywhere.com")
                .addPathSegment("Wordbook")
                .addPathSegment("upload")
                .build()

            val JSON_MEDIA = "application/json; charset=utf-8".toMediaType()
            val sendDataJson = "{\"name\":\"${name}\",\"contents\":\"${contents}\"}"
            val request = Request.Builder()
                .url(url)
                .post(sendDataJson.toRequestBody(JSON_MEDIA))
                .build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    MainActivity().runOnUiThread {
                        Log.v("yey", "error: $e")
                        // binding.progressBar.hide()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val result = if (response.isSuccessful) {
                        response.body?.string()
                    } else {
                        "failed/ code: ${response.code} / message: ${response.message}"
                    }
                }

            })
        }
    }

    //単語帳をダウンロードする関数
    fun downloadWB(WordbookName:String, filesDir: File){
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("kudo1122.pythonanywhere.com")
            .addPathSegment("Wordbook")
            .addPathSegment("download")
            .build()

        val JSON_MEDIA = "application/json; charset=utf-8".toMediaType()
        val sendDataJson = "{\"name\":\"${WordbookName}\"}"
        val request = Request.Builder()
            .url(url)
            .post(sendDataJson.toRequestBody(JSON_MEDIA))
            .build()
        Log.v("WB","body:"+request.body.toString())
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                MainActivity().runOnUiThread {
                    Log.v("yey", "error: $e")
                    // binding.progressBar.hide()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val result = if (response.isSuccessful) {
                    response.body?.string()
                } else {
                    "failed/ code: ${response.code} / message: ${response.message}"
                }

                //ファイルへ保存
                val dir = File(filesDir, "dics/$WordbookName")
                if(!dir.exists()) dir.mkdir()

                val str = result
                File(filesDir, "dics/$WordbookName/words.txt").writer().use {
                    it.write(str)
                }
            }
        })
    }
}