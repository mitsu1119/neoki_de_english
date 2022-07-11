package io.github.mitsu1119.neoki_de_english.alarm

import android.util.Log
import io.github.mitsu1119.neoki_de_english.R
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalTime

class AlarmSet {
    var date = LocalDate.now()
        private set

    var time = LocalTime.now()
        private set

    companion object {
        fun create(internalDir: File, hour: Int, minute: Int): Boolean {
            // 新規作成
            val new = internalDir.absolutePath + "/alarms/"

            val alarmNames = File(new).list()
            var max = -1
            for(i in alarmNames) {
                val n = i.split("/").last().split(".")[0].toInt()
                if(max < n) max = n
            }
            val alarmNum = max + 1
            Log.v("yey", "alarmNum: $alarmNum")

            Files.createFile(Paths.get(new + alarmNum.toString() + ".txt"))

            recording(new + alarmNum.toString() + ".txt", hour, minute)
            return true
        }

        private fun recording(fileName: String, hour: Int, minute: Int) {
            val f = File(fileName)
            val fw = FileWriter(f, true)
            fw.write("$hour,$minute")
            fw.close()
        }
    }
}