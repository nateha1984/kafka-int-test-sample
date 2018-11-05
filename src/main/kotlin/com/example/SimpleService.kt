package com.example

import org.springframework.stereotype.Service

@Service
class SimpleService() {
    fun handleMessage(message: String) {
        println(message)
    }
}