package com.bruce.room.repository.bean

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("errorCode")
    val code: Int = 0,
    @SerializedName("errorMsg")
    val message: String = "",
    val data: T?,
) {
    companion object {
        const val CODE_SUCCESS = 0
    }
}