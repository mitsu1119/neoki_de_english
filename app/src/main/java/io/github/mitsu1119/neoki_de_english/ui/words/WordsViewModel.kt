package io.github.mitsu1119.neoki_de_english.ui.words

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import java.io.File

class WordsViewModel: ViewModel() {
    private val _dicName = MutableLiveData<String>().apply {
        value = ""
    }
    fun setDicName(dicName: String) {
        _dicName.value = dicName
    }
    val dicName: LiveData<String> = _dicName

    private val _words = MutableLiveData<MutableList<String>>().apply {
        value = mutableListOf()
    }
    fun setWord(word: String) {
        _words.value?.add(word)
    }
    fun loadWords(internalDir: File, dicName: String) {
        _words.value = DicSet.loadOnlyEnglish(internalDir, dicName)
    }
    val words: LiveData<MutableList<String>> = _words
}