package io.github.mitsu1119.neoki_de_english.ui.transform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.mitsu1119.neoki_de_english.alarm.AlarmSet

class TransformViewModel : ViewModel() {

    private val _alarms = MutableLiveData<List<AlarmSet>>().apply() {
        value = List<AlarmSet>(16) { AlarmSet() }
    }

    private val _texts = MutableLiveData<List<String>>().apply {
        value = (0..15).mapIndexed { _, i ->
            _alarms.value!!.get(i).date.toString()
        }
    }
    val texts: LiveData<List<String>> = _texts
}