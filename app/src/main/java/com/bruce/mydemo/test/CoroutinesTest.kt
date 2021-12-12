package com.bruce.mydemo.test

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
    GlobalScope.launch {
        delay(2000L)
        println("world!")
    }
    println("hello")
}