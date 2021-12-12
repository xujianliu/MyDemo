package com.bruce.room.viewModel

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.*
import com.bruce.room.repository.LoginRepository
import com.bruce.room.repository.bean.LoginResponse
import com.bruce.room.repository.db.User
import kotlinx.coroutines.*

/**
 *
 *@author xujian
 *@date 2021/9/25
 */
class LoginViewModel(private val mRepository: LoginRepository) : ViewModel() {

    private val TAG: String = this.javaClass.simpleName


    /**
     * 登录玩Android
     */
     fun netlogin(name: String, password: String) = liveData {
        emit(mRepository.netLogin(name, password))
    }

    suspend fun login(firstName: String, lastName: String): User {
        Log.i(TAG, "login: start")
//        viewModelScope.launch {// 声明一个协程，为执行下面的  被suspend标注的makeLoginRequest函数.
        //1. UI thread
        Log.i(TAG, "1-login: 当前线程${Thread.currentThread().name}")

        val makeLoginRequest = mRepository.makeLoginRequest(
            firstName,
            lastName
        ) // 2. IO thread ,协程会被挂起，直到makeLoginRequest执行结束，协程才恢复

        //3. UI thread  直到2执行结束，拿到结果，但是不阻塞UI thread
        Log.i(TAG, "3-login: 当前线程${Thread.currentThread().name}")
//        }
        Log.i(TAG, "login: end")

        return makeLoginRequest
    }

    fun login4LiveData(firstName: String, lastName: String) = liveData {
        emit(mRepository.makeLoginRequest(firstName, lastName))
    }

    fun insertUserList(list: List<User>): LiveData<List<Long>> {
        return liveData<List<Long>> {
            emit(mRepository.insertUsers(list))
        }
    }

    //    fun insertUser(user: User)=loginRepository.insertUser(user)
    fun findUser(firstName: String, lastName: String) = mRepository.findUser(firstName, lastName)


    fun featchTwoDocs() {
        Log.i(TAG, "featchTwoDocs: 0 ${Thread.currentThread().name}")
        runBlocking {

            Log.i(TAG, "featchTwoDocs: 1 ${Thread.currentThread().name}")

            val doc1 = async(Dispatchers.IO) {
                Log.i(TAG, "featchTwoDocs: async1 start")//并行开始
                var result = false
                method2(5000) {
                    result = it
                }
                Log.i(TAG, "featchTwoDocs: async1 end,doc1 ${Thread.currentThread().name}")
                "async1 end"
                result
            }
            Log.i(TAG, "featchTwoDocs: 2 ${Thread.currentThread().name}")

            val doc2 = async(Dispatchers.IO) {
                Log.i(TAG, "featchTwoDocs: async2 start")//并行开始
                var result = false
                method2(1000) {
                    result = it
                }
                Log.i(TAG, "featchTwoDocs: async2 end,doc2 ${Thread.currentThread().name}")
                "async2 end"
                result
            }

            Log.i(TAG, "featchTwoDocs: 3 ${Thread.currentThread().name}")

//            val list=listOf(doc1.await(),doc2.await())//拿到两个结果再结束
//            Log.i(TAG, "featchTwoDocs: list-$list")
            val result1 = doc1.await()
            val result2 = doc2.await()
            Log.i(TAG, "featchTwoDocs: $result1\t $result2")
        }
        Log.i(TAG, "featchTwoDocs: 4 ${Thread.currentThread().name}")
    }

    fun method2(time: Long, result: (Boolean) -> Unit) {

        SystemClock.sleep(time)
//        result.invoke(true)
        result(true)
    }
}


object LoginViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(LoginRepository()) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}