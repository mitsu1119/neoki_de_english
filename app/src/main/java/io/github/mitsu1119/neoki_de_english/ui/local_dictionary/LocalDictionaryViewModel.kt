package io.github.mitsu1119.neoki_de_english.ui.local_dictionary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.mitsu1119.neoki_de_english.alarm.AlarmSet
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import java.io.File

class LocalDictionaryViewModel: ViewModel() {
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
    fun removeDic(internalDir: File, name: String) {
        DicSet.remove(internalDir, name)
        _dicNames.value?.remove(name)
    }
    val dicNames: LiveData<MutableList<String>> = _dicNames

    fun print() {
        Log.v("myTest", "[dictionary list]")
        for(i in (0..(_dicNames.value!!.size - 1))) {
            Log.v("myTest", _dicNames.value!!.get(i))
        }
    }
}