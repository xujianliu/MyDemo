package com.bruce.room.repository.bean

/**
 *
 *@author xujian
 *@date 2021/12/12
 */
data class LoginResponse(
    val admin: Boolean? = false,
    val chapterTops: List<String?>? = listOf(),
    val coinCount: Int? = 0,
    val collectIds: List<String?>? = listOf(),
    val email: String? = null,
    val icon: String? = null,
    val id: Int? = 0,
    val nickname: String? = null,
    val password: String? = null,
    val publicName: String? = null,
    val token: String? = null,
    val type: Int? = 0,
    val username: String? = null,

    )