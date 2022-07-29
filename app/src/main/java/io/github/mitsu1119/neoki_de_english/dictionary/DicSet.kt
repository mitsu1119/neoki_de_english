package io.github.mitsu1119.neoki_de_english.dictionary

import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DicSet {
    companion object {
        fun getDicNames(internalDir: File): Array<String> {
            return internalDir.list()
        }

        fun create(internalDir: File, name: String): Boolean {
            // すでに同名の辞書が存在していたら false
            if(internalDir.list().contains(name)) return false

            // 新規作成
            val new = internalDir.absolutePath + "/" + name
            Files.createDirectory(Paths.get(new))
            Files.createFile(Paths.get(new + "/words.txt"))
            Files.createFile(Paths.get(new + "/hist.txt"))

            return true
        }

        fun remove(internalDir: File, name: String) {
            val dir = File(internalDir, "dics/$name")
            if(dir.exists() and dir.isDirectory) dir.deleteRecursively()
        }

        fun recording(internalDir: File, dicName: String, english: String, japanese: String) {
            val f = File(internalDir.absolutePath + "/" + dicName + "/words.txt")
            val fw = FileWriter(f, true)
            fw.write(japanese + "\n")
            fw.write(english + "\n")
            fw.close()

            val ff = File(internalDir.absolutePath + "/" + dicName + "/hist.txt")
            val fww = FileWriter(ff, true)
            fww.write(english + "\n")
            fww.write("01/01/1970\n")
            fww.close()
        }

        fun loadOnlyEnglish(internalDir: File, dicName: String): ArrayList<String> {
            val f = File(internalDir.absolutePath + "/dics/" + dicName + "/words.txt")
            var buf = 0
            var ret = arrayListOf<String>()
            BufferedReader(FileReader(f)).use { br ->
                var line: String?
                while(br.readLine().also { line = it } != null) {
                    if(buf == 1) ret.add(line!!)
                    buf = buf xor 1
                }
            }
            return ret
        }

        fun load(internalDir: File, dicName: String): ArrayList<Pair<String, String>> {
            val f = File(internalDir.absolutePath + "/dics/" + dicName + "/words.txt")
            var buf = 0
            var ret = arrayListOf<Pair<String, String>>()
            BufferedReader(FileReader(f)).use { br ->
                var line: String?
                var jp = ""
                while(br.readLine().also { line = it } != null) {
                    if(buf == 0)  jp = line!!
                    else ret.add(Pair(line!!, jp))
                    buf = buf xor 1
                }
            }
            return ret
        }

        fun loadHistory(internalDir: File, dicName: String): ArrayList<Pair<String, Date>> {
            val f = File(internalDir.absolutePath + "/dics/" + dicName + "/hist.txt")
            var buf = 0
            var ret = arrayListOf<Pair<String, Date>>()
            BufferedReader(FileReader(f)).use { br ->
                var line: String?
                var jp = ""
                while(br.readLine().also { line = it } != null) {
                    if(buf == 0)  jp = line!!
                    else {
                        val s = SimpleDateFormat("mm/dd/yyyy").parse(jp)
                        ret.add(Pair(line!!, s))
                    }
                    buf = buf xor 1
                }
            }
            return ret
        }

        fun updateHistory(internalDir: File, dicName: String, hist: ArrayList<Pair<String, Date>>) {
            val ff = File(internalDir.absolutePath + "/" + dicName + "/hist.txt")
            val fww = FileWriter(ff, false)
            for(i in hist) {
                fww.write(i.first + "\n")
                fww.write("${i.second.day.toString().padStart(2, '0')}/${i.second.month.toString().padStart(2, '0')}/${i.second.year.toString().padStart(4, '0')}\n")
            }
            fww.close()

        }
    }
}