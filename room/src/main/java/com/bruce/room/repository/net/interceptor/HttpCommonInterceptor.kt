package com.bruce.room.repository.net.interceptor

import android.os.Build
import com.bruce.room.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * 拦截器
 *
 *
 * 向请求头里添加公共参数
 */
class HttpCommonInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest: Request = chain.request()
        // 添加新的参数，添加到url 中
        /* HttpUrl.Builder authorizedUrlBuilder = oldRequest.url().newBuilder()
         .scheme(oldRequest.url().scheme())
             .host(oldRequest.url().host());*/
        // 新的请求
        val requestBuilder = oldRequest.newBuilder()
        requestBuilder.method(
            oldRequest.method,
            oldRequest.body
        )
        val mHeaderParamsMap: MutableMap<String, String> = HashMap()
//        val loginInfo = DataManager.getInstance().loginInfo
//        if (loginInfo != null) {
//            if (!oldRequest.url.toString().endsWith("authentication/mobile")
//                && !oldRequest.url.toString().endsWith("code/sms")) {
//                //token信息
//                mHeaderParamsMap["access_token"] = loginInfo.access_token ?: ""
//            }
//        }

        //系统版本号和code
        mHeaderParamsMap["appVersionInfo"] = Build.VERSION.RELEASE+Build.VERSION.SDK_INT //DeviceUtils.getSDKVersionName() + "-" + DeviceUtils.getSDKVersionCode()
        //应用版本号信息
        mHeaderParamsMap["versionInfo"] =BuildConfig.VERSION_NAME //AppUtils.getAppVersionName()
        //设备信息，为声音采集任务cms后台展示使用
        mHeaderParamsMap["deviceInfo"] = Build.BOARD+Build.MODEL//DeviceUtils.getManufacturer() + "-" + DeviceUtils.getModel()
        //语言环境
        val language=Locale.getDefault().language
        mHeaderParamsMap["ecai-language"]=if (language=="en"){"eng"}else{language}

        //添加公共参数,添加到header中
        if (mHeaderParamsMap.isNotEmpty()) {
            for ((key, value) in mHeaderParamsMap) {
                requestBuilder.header(key, value)
            }
        }
        val newRequest: Request = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}