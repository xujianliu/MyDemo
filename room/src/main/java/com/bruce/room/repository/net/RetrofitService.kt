package com.bruce.room.repository.net

import com.bruce.room.repository.bean.BaseResponse
import com.bruce.room.repository.bean.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 *
 *@author xujian
 *@date 2021/12/12
 */
interface RetrofitService {

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") username: String, @Field("password") password: String): BaseResponse<LoginResponse>

    /**
     * 刷新token
     */
    @FormUrlEncoded
    @POST("oauth/refresh/token")
    suspend fun refreshToken(
        @Field("type") type: String,
        @Field("refreshToken") refreshToken: String
    ): BaseResponse<LoginResponse>

}