package com.bruce.room

import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bruce.room.databinding.ActivityMainBinding
import com.bruce.room.repository.db.User
import com.bruce.room.repository.net.LiveResult
import com.bruce.room.viewModel.LoginViewModel
import com.bruce.room.viewModel.LoginViewModelFactory
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG: String = this.javaClass.simpleName
    private lateinit var mbinding: ActivityMainBinding
    private val loginViewModel: LoginViewModel by viewModels { LoginViewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mbinding.root
        window.statusBarColor = getColor(R.color.purple_500)
        setContentView(view)


        val language = Locale.getDefault().language
        Log.i(TAG, "onCreate: ${language}")

        val languag = if (language == "en") {
            "eng"
        } else {
            language
        }
        println(languag)

        getValidSampleRates()

        val userList = mutableListOf<User>()
        userList.add(User(firstName = "liu", lastName = "bruce1", age = 18))
        userList.add(User(firstName = "li", lastName = "bruce2", age = 28))
        userList.add(User(firstName = "lu", lastName = "bruce3", age = 38))
        userList.add(User(firstName = "yang", lastName = "bruce4", age = 48))

        println(userList.iterator().next())
        mbinding.run {

            btnInsertUser.setOnClickListener {
//                loginViewModel.insertUser(User(firstName = "gao", lastName = "yuan", age = 8)).observe(this@MainActivity,{
//                    Toast.makeText(this@MainActivity, "insert success $it", Toast.LENGTH_SHORT).show()
//
//                })
            }

            btnInsertUserList.setOnClickListener {
                loginViewModel.insertUserList(userList).observe(this@MainActivity, {
                    Log.i(TAG, "insertUsers: ${it}")
                    Toast.makeText(this@MainActivity, "insert success", Toast.LENGTH_SHORT).show()
                })

                Log.i(TAG, "onCreate: ${Locale.getDefault().language}")
            }


            btnLoginIn.setOnClickListener {
                Log.i(TAG,"setOnClickListener: start")
                loginViewModel.netlogin("bruceliulxj", "wa123456").observe(this@MainActivity, {
                    when (it) {
                        is LiveResult.Error -> {
                            it.exception.printStackTrace()
                        }
                        is LiveResult.Success -> {
                            val response = it.data
                            mbinding.tvContent.text = response.nickname

                        }
                    }
                })

//                runBlocking {
//                    val user = loginViewModel.login("bruceliulxj", "wa123456")
//                    mbinding.tvContent.text = user.toString()
//                }
                Log.i(TAG,"setOnClickListener: end")
            }


            btnLoginIn2.setOnClickListener {
//                loginViewModel.login4LiveData("liu", "bruce1").observe(this@MainActivity, {
//                    mbinding.tvContent.text = it.toString()
//                })

                loginViewModel.findUser("yang111", "bruce4").observe(this@MainActivity, {
                    mbinding.tvContent.text = it.toString() + "----"
                })
            }

            btnAsync.setOnClickListener {
                Log.i(TAG, "btnAsync.setOnClickListener: start")
                loginViewModel.featchTwoDocs()

                Log.i(TAG, "btnAsync.setOnClickListener: end")
            }
        }
    }

    /**
     * 检测有效采样率
     */
    fun getValidSampleRates() {
        val list =
            listOf<Int>(8000, 11025, 16000, 22050, 44100, 48000, 50000, 96000, 192000, 100000000)
        for (rate in list) { // add the rates you wish to check against
            val bufferSize = AudioTrack.getMinBufferSize(
                rate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            );

            Log.i(TAG, "getValidSampleRates: rate=$rate\tbufferSize=$bufferSize")
            if (bufferSize > 0) {
// buffer size is valid, Sample rate supported
            }
        }
    }
}