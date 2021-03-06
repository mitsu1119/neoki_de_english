package io.github.mitsu1119.neoki_de_english.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkBuilder
import io.github.mitsu1119.neoki_de_english.R
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet


class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Toast.makeText(context, "Received ", Toast.LENGTH_LONG).show()

        val dic = intent!!.getStringExtra("dictionary")!!

        // 最初だけ AlarmReceiver 側で出題する問題を選択
        val ds = DicSet.load(context.filesDir, dic)
        var hist = DicSet.loadHistory(context.filesDir, dic)

        // できるだけ過去に出題した問題を出題
        var n = 0
        var nd = hist.get(0).second
        for(i in (0..(hist.size - 1))) {
            val day = hist[i].second
            if(nd.before(day)) {
                nd = day
                n = i
            }
        }
        hist[n] = hist.get(n).copy(second = nd)
        DicSet.updateHistory(context.filesDir, dic, hist)

        val word = ds[n]
        val eng = word.first
        val jp = word.second
        val audioFileName = context.filesDir.absolutePath + "/dics/" + dic + "/" + eng + ".mp3"

        // アラーム再生用のサービスを開始
        val serviceIntent = Intent(context, MPService::class.java)
        serviceIntent.putExtra("AudioFileName", audioFileName)
        context.startService(serviceIntent)

        // 通知作成
        val CHANNEL_ID = "channel_id"
        val channel_name = "channel_name"
        val channel_description = "channel_description "

        // チャンネルを作成
        val name = channel_name
        val descriptionText = channel_description
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.nav_quiz)
            .setArguments(
                Bundle().apply {
                    putString("dicName", dic)
                    putInt("numberOfQuiz", 0)
                    putString("cw", "")
                    putString("engs", eng)
                    putString("answers", jp)
                    putString("corrects", "")
                }
            )
            .createPendingIntent()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("通知")
            .setContentText("alarm, dic: $dic, eng: $eng")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
    }
}