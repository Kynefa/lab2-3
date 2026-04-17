package com.example.lab2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val resultText = MutableLiveData<String>()
}
