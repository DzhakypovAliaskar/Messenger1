package com.example.messenger

interface ResultListener<T> {
    fun onResult(t: T?)
}