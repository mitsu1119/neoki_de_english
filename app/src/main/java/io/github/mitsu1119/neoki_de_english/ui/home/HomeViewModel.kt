package io.github.mitsu1119.neoki_de_english.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.mitsu1119.neoki_de_english.alarm.AlarmSet
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import java.io.File

class HomeViewModel : ViewModel() {

    private val _alarms = MutableLiveData<MutableList<AlarmSet>>().apply {
        value = mutableListOf()
    }
    fun loadAlarms(internalDir: File) {
        val alarmDir = internalDir.absolutePath + "/alarms/"
        for(name in File(alarmDir).list()) {
            val al = AlarmSet.load(alarmDir, name)
            addAlarm(al)
        }
    }
    fun addAlarm(alarm: AlarmSet) {
        _alarms.value?.add(alarm)
        applyAlarms()
    }
    fun removeAlarm(position: Int, internalDir: File) {
        val f = File(internalDir, "alarms/${position}.txt")
        if(f.exists()) {
            f.deleteRecursively()
            _alarms.value?.removeAt(position)
        }
    }
    val alarms: LiveData<MutableList<AlarmSet>> = _alarms

    private val _texts = MutableLiveData<List<String>>().apply {
        value = (0..(_alarms.value!!.size- 1)).mapIndexed { _, i ->
            _alarms.value?.get(i)?.date.toString() + " " + _alarms.value?.get(i)?.time.toString()
        }
    }
    private fun applyAlarms() {
        _texts.value = (0..(_alarms.value!!.size - 1)).mapIndexed { _, i ->
            _alarms.value?.get(i)?.date.toString() + " " + _alarms.value?.get(i)?.time.toString()
        }
    }
    val texts: LiveData<List<String>> = _texts
}