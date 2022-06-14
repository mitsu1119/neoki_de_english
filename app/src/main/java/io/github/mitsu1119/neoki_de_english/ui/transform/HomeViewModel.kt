package io.github.mitsu1119.neoki_de_english.ui.transform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.mitsu1119.neoki_de_english.alarm.AlarmSet

class HomeViewModel : ViewModel() {

    private val _alarms = MutableLiveData<MutableList<AlarmSet>>().apply() {
        value = MutableList(16) { AlarmSet() }
    }
    fun addAlarm() {
        _alarms.value?.add(AlarmSet())
        applyAlarms()
    }
    val alarms: LiveData<MutableList<AlarmSet>> = _alarms

    private val _texts = MutableLiveData<List<String>>().apply {
        value = (0..(_alarms.value!!.size - 1)).mapIndexed { _, i ->
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