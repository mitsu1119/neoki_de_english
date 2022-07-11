package io.github.mitsu1119.neoki_de_english.alarm

import android.util.Log
import io.github.mitsu1119.neoki_de_english.R
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalTime

class AlarmSet {
    var date: LocalDate
        private set

    var time: LocalTime
        private set

    constructor() {
        date = LocalDate.now()
        time = LocalTime.now()
    }

    constructor(hour: Int, minute: Int) {
        this.date = LocalDate.now()

        this.time = LocalTime.now()
        this.time = this.time.withHour(hour).withMinute(minute)
    }

    companion object {
        fun create(internalDir: File, hour: Int, minute: Int): AlarmSet {
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
            return AlarmSet(hour, minute)
        }

        private fun recording(fileName: String, hour: Int, minute: Int) {
            val f = File(fileName)
            val fw = FileWriter(f, true)
            fw.write("$hour,$minute")
            fw.close()
        }

        fun load(fileName: String): AlarmSet {
            val f = File(fileName)
            val st = f.readLines().first()

            val hour = st.split(",")[0].toInt()
            val minute = st.split(",")[1].toInt()
            val ret = AlarmSet(hour, minute)

            return ret
        }
    }
}