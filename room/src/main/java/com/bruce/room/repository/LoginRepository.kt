package com.bruce.room.repository

import android.util.Log
import com.bruce.room.repository.bean.LoginResponse
import com.bruce.room.repository.db.AppDataBaseHelp
import com.bruce.room.repository.db.User
import com.bruce.room.repository.net.LiveResult
import com.bruce.room.repository.net.NetworkManagerK
import com.bruce.room.repository.net.toLiveResultX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 *@author xujian
 *@date 2021/9/25
 */
class LoginRepository {

    private val TAG: String = this.javaClass.simpleName

    suspend fun makeLoginRequest(firstName: String, lastName: String): User {
        Log.i(TAG, "makeLoginRequest: start${Thread.currentThread().name}")
        val user = withContext(Dispatchers.IO) {//在子线程执行耗时查询操作 并且返回查询的到的数据
            Log.i(TAG, "2-makeLoginRequest: 当前线程${Thread.currentThread().name}")
            val user1 = AppDataBaseHelp.getDatabase().userDao().findByName(firstName, lastName)
            Log.i(TAG, "makeLoginRequest: $user1")
            return@withContext user1
        }
        Log.i(TAG, "makeLoginRequest: end${Thread.currentThread().name}")
        return user
    }

    fun findUser(first: String, last: String) =
        AppDataBaseHelp.getDatabase().userDao().findUser(first, last)

//    fun  insertUser(user: User)=AppDataBaseHelp.getDatabase().userDao().insertUser(user)


    suspend fun insertUsers(list: List<User>): List<Long> {
        return withContext(Dispatchers.IO) {
            return@withContext AppDataBaseHelp.getDatabase().userDao().insertUserList(list)
        }
    }


    suspend fun getToken() {

        withContext(Dispatchers.IO) {

        }
    }

    suspend fun netLogin(name: String, password: String): LiveResult<LoginResponse> {
        return withContext(Dispatchers.IO) {
            return@withContext NetworkManagerK.retrofitService.login(name, password).toLiveResultX {
                Log.i("netLogin",Thread.currentThread().name+"\t"+Thread.currentThread().id)
                it.data ?: LoginResponse()
            }
        }

    }
}

//class LoginRepository(private val responseParser: LoginResponseParser) {
//    private const val loginUrl = "https://www.wanandroid.com/user/login"
//
//    // Function that makes the network request, blocking the current thread
//    fun makeLoginRequest(
//        jsonBody: String
//    ): Result<LoginResponse> {
//        val url = URL(loginUrl)
//        (url.openConnection() as? HttpURLConnection)?.run {
//            requestMethod = "POST"
//            setRequestProperty("Content-Type", "application/json; utf-8")
//            setRequestProperty("Accept", "application/json")
//            doOutput = true
//            outputStream.write(jsonBody.toByteArray())
//
//            return Result.Success(responseParser.parse(inputStream))
//        }
//        return Result.Error(Exception("Cannot open HttpURLConnection"))
//    }
//}