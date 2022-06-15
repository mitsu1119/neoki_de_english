package io.github.mitsu1119.neoki_de_english.ui.menu3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Menu3ViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Fragment3"
    }
    val text: LiveData<String> = _text
}