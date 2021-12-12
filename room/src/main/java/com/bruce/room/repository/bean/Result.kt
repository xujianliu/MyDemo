package com.bruce.room.repository.bean

/**
 *
 *@author xujian
 *@date 2021/9/25
 */
sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}