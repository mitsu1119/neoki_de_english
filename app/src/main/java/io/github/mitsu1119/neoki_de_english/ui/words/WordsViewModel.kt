package io.github.mitsu1119.neoki_de_english.ui.words

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WordsViewModel: ViewModel() {
    private val _dicName = MutableLiveData<String>().apply {
        value = ""
    }
    fun setDicName(name: String) {
        _dicName.value = name
    }
    val dicName: LiveData<String> = _dicName
}