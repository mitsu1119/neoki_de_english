package io.github.mitsu1119.neoki_de_english.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class QuizViewModel: ViewModel() {
    private var _nowTime = MutableLiveData<String>()
    var nowTime: LiveData<String> = _nowTime

    fun updateTime() {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        _nowTime.value = hour.toString().padStart(2, '0') + ":" + minute.toString().padStart(2, '0')
    }

    private var _q = MutableLiveData<Pair<String, String>>()
    var q: LiveData<Pair<String, String>> = _q

    fun setQuestion(eng: String, jp: String) {
        _q.value = Pair(eng, jp)
    }

    init {
        updateTime()
    }
}