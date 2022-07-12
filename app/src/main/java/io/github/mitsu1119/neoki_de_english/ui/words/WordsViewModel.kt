package io.github.mitsu1119.neoki_de_english.ui.words

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import java.io.*

class WordsViewModel: ViewModel() {
    private val _dicName = MutableLiveData<String>().apply {
        value = ""
    }
    fun setDicName(dicName: String) {
        _dicName.value = dicName
    }
    val dicName: LiveData<String> = _dicName

    private var _words = MutableLiveData<MutableList<Word>>().apply {
        value = mutableListOf()
    }
    fun setWord(word: Word) {
        _words.value?.add(word)
    }
    fun loadWords(internalDir: File, dicName: String) {
        val v = mutableListOf<Word>()
        for(i in DicSet.loadOnlyEnglish(internalDir, dicName)) v.add(Word(i, false))
        _words.value = v
    }
    val words: LiveData<MutableList<Word>> = _words

    fun cbChange(position: Int) {
        _words.value?.get(position)?.checked = !(_words.value!!.get(position)?.checked)
    }

    fun getChecked(): ArrayList<Int> {
        val ret = arrayListOf<Int>()
        for(i in (0..(_words.value!!.size - 1))) {
            if(_words.value!!.get(i).checked) ret.add(i)
        }
        return ret
    }

    fun removeWords(internalDir: File, posess: MutableList<Int>) {
        val positions = posess
        val f = File(internalDir.absolutePath + "/dics/" + dicName.value + "/words.txt")
        var lines = mutableListOf<String>()
        BufferedReader(FileReader(f)).use { br ->
            var line: String?
            var cnt = 0
            while(br.readLine().also { line = it } != null) {
                if((positions.size == 0) or (cnt != positions.first())) {
                    lines.add(line!!)
                    lines.add(br.readLine())
                } else {
                    br.readLine()
                    positions.removeAt(0)
                }
                cnt += 1
            }
        }

        val bw = BufferedWriter(FileWriter(f))
        for(i in lines) {
            bw.write(i)
            bw.newLine()
        }
        bw.close()
    }
}