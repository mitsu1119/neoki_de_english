package io.github.mitsu1119.neoki_de_english.alarm

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class MPService: Service() {
    companion object {
        val tag = "MPService"
    }

    private lateinit var mp: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // アラーム音作成
        val audioFileName = intent!!.getStringExtra("AudioFileName")
        mp = MediaPlayer().apply {
            setDataSource(audioFileName)
            isLooping = true
        }
        mp.prepare()
        mp.start()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mp.isPlaying) mp.stop()
    }
}