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

    var number: Int
        private set

    var dow: String
        private set

    var dic: String
        private set

    constructor(hour: Int, minute: Int, number: Int, dow: String, dic: String) {
        this.date = LocalDate.now()
        this.time = LocalTime.now()
        this.time = this.time.withHour(hour).withMinute(minute)
        this.dow = dow
        this.dic = dic
        this.number = number
    }

    companion object {
        fun create(internalDir: File, hour: Int, minute: Int, dow: String, dic: String): AlarmSet {
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

            recording(new + alarmNum.toString() + ".txt", hour, minute, dow, dic)
            return AlarmSet(hour, minute, alarmNum, dow, dic)
        }

        private fun recording(fileName: String, hour: Int, minute: Int, dow: String, dic: String) {
            val f = File(fileName)
            val fw = FileWriter(f, true)
            fw.write("$hour,$minute,$dow,$dic")
            fw.close()
        }

        fun load(alarmDir: String, fileName: String): AlarmSet {
            val number = fileName.split(".")[0].toInt()
            val f = File(alarmDir + fileName)
            val st = f.readLines().first()

            val hour = st.split(",")[0].toInt()
            val minute = st.split(",")[1].toInt()
            val dow = st.split(",")[2]
            val dic = st.split(",")[3]
            val ret = AlarmSet(hour, minute, number, dow, dic)

            return ret
        }
    }
}