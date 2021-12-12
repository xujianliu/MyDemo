/*
 * Copyright 2018 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bruce.room.repository.net

import com.bruce.room.BruceApplication
import com.bruce.room.R
import com.bruce.room.repository.bean.BaseResponse
import timber.log.Timber
import java.io.IOException

inline fun <T, R : Any> BaseResponse<T>.toLiveResultX(crossinline mapper: (BaseResponse<T>) -> R): LiveResult<R> {
    return if (this.code == BaseResponse.CODE_SUCCESS) {
        LiveResult.Success(mapper.invoke(this))
    } else {
        LiveResult.Error(errorMessage = this.code.toString() + this.message)
    }
}


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class LiveResult<out T : Any> {

    data class Success<out T : Any>(val data: T = Any() as T) : LiveResult<T>()
    data class Error(
        val exception: Exception = IOException("Default Error"),
        val errorMessage: String = BruceApplication.instance.getString(R.string.request_all_fail)
    ) : LiveResult<Nothing>()


    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception message=$errorMessage]"
        }
    }
}


suspend fun <T : Any> safeApiCall(
    call: suspend () -> LiveResult<T>,
    errorMessage: String = BruceApplication.instance.getString(R.string.request_all_fail)
): LiveResult<T> {
    return try {
        call()
    } catch (e: Exception) {
        Timber.e("safeCallError $e")
        e.printStackTrace()
        // An exception was thrown when calling the API so we're converting this to an IOException
        LiveResult.Error(e, errorMessage)
    }
}
