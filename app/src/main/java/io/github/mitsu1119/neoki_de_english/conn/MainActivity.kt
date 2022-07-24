/*
package com.example.serverdriver2

//import android.R
//import jp.gcreate.sample.networksample.databinding.ActivityMainBinding
import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.serverdriver2.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.io.IOException
import java.nio.file.Files.isDirectory
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri


class https://terra-historicus.hypergryph.com/comic/1421/episode/1506MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var binding: ActivityMainBinding
    private val okHttpClient = OkHttpClient.Builder().build()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //単語帳の名前リストを取得ボタン
        binding.getlist.setOnClickListener {
            getWBlist()
        }

        //単語帳データをダウンロードボタン
        binding.WBDL.setOnClickListener {
            var name = binding.WBname.text.toString()//単語帳の名前が入力
            runOnUiThread {
                binding.progressBar.show()
                downloadWB(name)//単語帳をダウンロードする関数
                binding.textView.text = "単語帳をダウンロードしました"
                binding.progressBar.hide()
            }
        }

        //ファイルのアップロードボタン
        binding.WBUP.setOnClickListener {
            //ファイルの読み出し
            var name = binding.WBname.text.toString()//単語帳名
            runOnUiThread {
                binding.progressBar.show()
                uploadWB(name)//単語帳をアップロードする関数
                binding.textView.text = "単語帳をアップロードしました"
                binding.progressBar.hide()
            }
        }

        binding.AudioDL.setOnClickListener {
            var name = binding.WBname.text.toString()//単語帳名
            //音声データのダウンロード
            downloadAudio(name)
        }
    }

    //単語帳をソート(未実装)実行しても何も変わらない
    fun sortWordbook(name :String, mode:String){//mode: SNS / RANDOM / ABC
        launch {
            binding.progressBar.show()
            val url = HttpUrl.Builder()
                .scheme("https")
                .host("kudo1122.pythonanywhere.com")
                .addPathSegment("Wordbook")
                .addPathSegment("sort")
                .addQueryParameter("name", name)
                .addQueryParameter("mode", mode)
                .build()
            val request = Request.Builder()
                .url(url)
                .build()
            val result = withContext(Dispatchers.IO) {
                okHttpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        response.body?.string()
                    } else {
                        "failed/ code: ${response.code} / message: ${response.message}"
                    }
                }
            }
            binding.textView.text = result //戻り値未定
            binding.progressBar.hide()
        }
    }

    //単語帳を一行ずつ読み取り、ダウンロード指示を出す関数 保存先は"${externalMediaDirs.first()}/${単語帳名}/"
    fun downloadAudio(name:String){
        var file = "${name}.txt"//ファイル名は単語帳名.txt
        val readFile = File(applicationContext.filesDir, file)
        if(!readFile.exists()){
            binding.textView.text = "${name}.txt がみつかりません"
        }else {
            try {
                BufferedReader(FileReader(readFile)).use { br ->
                    var line: String
                    while (br.readLine().also { line = it } != null) {//1単語ずつ読みだしてダウンロードする
                        br.readLine().also { line = it }
                        if(!File("${externalMediaDirs.first()}/${name}/${line}.mp3").exists()){//音声ファイルがない場合ダウンロード
                            println(line+"を取得します")
                            getAudioByHttpPOST(name,line)
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getAudioByHttpPOST(name: String,word: String) {
        binding.progressBar.show()
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
        //binding.textView.text = contents
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    binding.textView.text = "error: $e"
                    binding.progressBar.hide()
                }
            }
            //レスポンス(bodyにバイナリデータが入っている)
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call, response: Response) {
                val result:InputStream?
                result = if (response.isSuccessful) {
                    response.body?.byteStream()
                } else {
                    throw Exception("音声データのダウンロードに失敗しました")
                }
                    if (result != null) {
                        SaveBinToMp3(result,name,word)
                    }else{
                        throw Exception("音声データのダウンロードに失敗しました")
                    }

                runOnUiThread {
                    binding.textView.text = "音声データのダウンロードが完了しました"
                    binding.progressBar.hide()
                }
            }

        })
        Thread.sleep(1000)//一秒待機
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun SaveBinToMp3(result: InputStream, name: String,word: String) {
        var path = Paths.get("${externalMediaDirs.first()}/${name}")
        try {
            if (!isDirectory(path)) {
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

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}

 */