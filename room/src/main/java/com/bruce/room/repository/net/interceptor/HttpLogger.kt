package com.bruce.room.repository.net.interceptor

import android.util.Log
import com.bruce.room.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

/**
 * 显示日志的拦截器
 *
 * @author：tqzhang on 18/9/8 21:31
 */
class HttpLogger : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        if (BuildConfig.DEBUG) {
            //日志打印的最大长度大约是4*1024，超过就回丢失
            val segmentSize = 3800
            val length = message.length.toLong()
            if (length <= segmentSize) { // 长度小于等于限制直接打印
                Timber.v(message)
                Log.i("HTTP", message)
            } else {
                var longMessage = message
                while (longMessage.length > segmentSize) { // 循环分段打印日志
                    val logContent = longMessage.substring(0, segmentSize)
                    longMessage = longMessage.replace(logContent, "")
                    Timber.v(logContent)
                }
                Timber.v(longMessage) // 打印剩余日志
            }
        }
    }
}