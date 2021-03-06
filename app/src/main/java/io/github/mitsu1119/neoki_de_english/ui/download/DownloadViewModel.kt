package io.github.mitsu1119.neoki_de_english.ui.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import java.io.File

class DownloadViewModel: ViewModel() {
    private val _dicNames = MutableLiveData<MutableList<String>>().apply {
        value = mutableListOf()
    }
    fun loadDicNames(internalDir: File) {
        for(name in DicSet.getDicNames(internalDir)) {
            if(!dicNames.value!!.contains(name)) addDicName(name)
        }
    }
    fun addDicName(name: String) {
        _dicNames.value?.add(name)
    }
    val dicNames: LiveData<MutableList<String>> = _dicNames
}