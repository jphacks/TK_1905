package jp.co.myowndict.data

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.squareup.moshi.Moshi
import jp.co.myowndict.MyApplication
import jp.co.myowndict.model.*
import retrofit2.Response
import timber.log.Timber
import java.net.ConnectException
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    private val application: MyApplication,
    private val apiService: ApiService,
    private val moshi: Moshi,
    private val sharedPreferences: SharedPreferences
) {
    private val errorMessagesAdapter = moshi.adapter(ErrorMessages::class.java)

    suspend fun signUp(): Result<Token> {
        val uuid = generateUuid()

        val result = safeApiCall {
            apiService.signUp(Uuid(uuid))
        }

        if (result is Result.Success) {
            TokenManager.put(result.data.token)
            saveUuid(uuid)
        }

        return result
    }

    suspend fun signIn(uuid: String): Result<Token> {
        val result = safeApiCall {
            apiService.signIn(Uuid(uuid))
        }

        if (result is Result.Success) {
            TokenManager.put(result.data.token)
        }

        return result
    }

    suspend fun sendText(text: String): Result<Unit> {
        return safeApiCall {
            apiService.sendText(SpeechText(text))
        }
    }

    fun getUuid(): String? = sharedPreferences.getString(KEY_TOKEN, null)

    fun saveUuid(uuid: String) {
        sharedPreferences
            .edit()
            .putString(KEY_TOKEN, uuid)
            .apply()
    }

    fun generateUuid(): String = UUID.randomUUID().toString()

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
        return try {
            if (!isNetworkConnected()) throw ConnectException()

            val response = call.invoke()
            val body = (response.body() ?: Unit) as T

            if (response.isSuccessful) {
                Result.Success(body)
            } else {
                val responseBodyString = response.errorBody()?.string()
                val errorMessage = runCatching {
                    responseBodyString?.let {
                        errorMessagesAdapter.fromJson(it)
                    }?.errorMessages?.fold("") { bodyString, string -> bodyString + string }
                }.getOrElse {
                    "Unknown Exception"
                }
                throw HandledException(errorMessage ?: "", response.code())
            }
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(e)
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }


    companion object {
        private const val KEY_TOKEN: String = "TokenString"

    }
}


