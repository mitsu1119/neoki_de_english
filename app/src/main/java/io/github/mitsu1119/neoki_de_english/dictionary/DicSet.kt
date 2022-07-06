package io.github.mitsu1119.neoki_de_english.dictionary

import java.io.File
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
            return true
        }
    }
}