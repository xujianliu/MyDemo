package com.bruce.room.repository.bean

data class ResponseNoData(
    @JvmField
    var code: Int = 0,
    @JvmField
    var message: String = "",
)