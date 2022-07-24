package io.github.mitsu1119.neoki_de_english.conn

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.github.mitsu1119.neoki_de_english.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
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
        Thread.sleep(1000)
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
                if(!dir.exists()) {
                    dir.mkdir()
                    File(dir, "words.txt").createNewFile()
                }

                val str = result
                File(filesDir, "dics/$WordbookName/words.txt").writer().use {
                    it.write(str)
                }
                Log.v("yey", "単語帳 $WordbookName を作成しました")
            }
        })
    }

    //単語帳を一行ずつ読み取り、ダウンロード指示を出す関数 保存先は"${externalMediaDirs.first()}/${単語帳名}/"
    fun downloadAudio(name:String, filesDir: File){
        val readFile = File(filesDir, "dics/$name/words.txt")
        if(!readFile.exists()) {
            Log.e("m2_dics", "単語帳 ${name} が見つかりません")
        } else {
            try {
                BufferedReader(FileReader(readFile)).use { br ->
                    var line: String
                    while (br.readLine().also { line = it } != null) { //1単語ずつ読みだしてダウンロードする
                        if(!File(filesDir,"dics/${name}/${line}.mp3").exists()){ //音声ファイルがない場合ダウンロード
                            Log.v("m2_server", "${line}.mp3 をダウンロードします")
                            // getAudioByHttpPOST(name, line, filesDir)
                        }
                        br.readLine().also { line = it }
                    }
                    Log.e("m2_server", "音声ファイルのダウンロードが終わりました downloadAudio")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getAudioByHttpPOST(name: String,word: String, filesDir: File) {
        // binding.progressBar.show()
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("kudo1122.pythonanywhere.com")
            .addPathSegment("Wordbook")
            .addPathSegment("getAudio")
            .build()
        val JSON_MEDIA = "application/json; charset=utf-8".toMediaType()
        val sendDataJson = "{\"name\":\"${word}\"}"
        val request = Request.Builder()
            .url(url)
            .post(sendDataJson.toRequestBody(JSON_MEDIA))
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.v("m2_server", "error: $e")
                // binding.progressBar.hide()
            }

            //レスポンス(bodyにバイナリデータが入っている)
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call, response: Response) {
                val result: InputStream?
                result = if (response.isSuccessful) {
                    response.body?.byteStream()
                } else {
                    throw Exception("音声データのダウンロードに失敗しました")
                }
                if (result != null) {
                    SaveBinToMp3(result, name, word, filesDir)
                }else{
                    throw Exception("音声データのダウンロードに失敗しました")
                }

                Log.v("m2_server", "音声データのダウンロードが完了しました")
            }

        })
        Thread.sleep(1000)//一秒待機
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun SaveBinToMp3(result: InputStream, name: String, word: String, internalDir: File) {
        var path = Paths.get("${internalDir.absolutePath}/dics/${name}")
        try {
            if (!Files.isDirectory(path)) {
                Files.createDirectory(path)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val dataOutStream = DataOutputStream(
            BufferedOutputStream(
                //FileOutputStream("${Environment.getExternalStorageDirectory()}/$packageName")
                FileOutputStream(File(path.toString(),"/${word}.mp3"))
            )
        )
        var b :Int
        b = result.read()
        while(b != -1){
            dataOutStream.writeByte(b)
            b=  result.read()
        }

        dataOutStream.close()
    }
}