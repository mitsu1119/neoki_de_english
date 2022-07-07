package io.github.mitsu1119.neoki_de_english.dictionary

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths

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
            return true
        }

        fun recording(internalDir: File, dicName: String, english: String, japanese: String) {
            val f = File(internalDir.absolutePath + "/" + dicName + "/words.txt")
            val fw = FileWriter(f, true)
            fw.write(english + "\n")
            fw.write(japanese + "\n")
            fw.close()
        }

        fun loadOnlyEnglish(internalDir: File, dicName: String): ArrayList<String> {
            val f = File(internalDir.absolutePath + "/" + dicName + "/words.txt")
            var buf = 0
            var ret = arrayListOf<String>()
            BufferedReader(FileReader(f)).use { br ->
                var line: String?
                while(br.readLine().also { line = it } != null) {
                    if(buf == 0) ret.add(line!!)
                    buf = buf xor 1
                }
            }
            return ret
        }
    }
}