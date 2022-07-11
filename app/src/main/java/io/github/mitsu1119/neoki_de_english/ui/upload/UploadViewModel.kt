package io.github.mitsu1119.neoki_de_english.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import java.io.File

class UploadViewModel: ViewModel() {
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