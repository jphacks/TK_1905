package jp.co.myowndict.data

import android.content.Context
import android.net.ConnectivityManager
import com.squareup.moshi.Moshi
import jp.co.myowndict.MyApplication
import jp.co.myowndict.model.ErrorMessages
import jp.co.myowndict.model.HandledException
import retrofit2.Response
import timber.log.Timber
import java.net.ConnectException
import javax.inject.Inject
import jp.co.myowndict.model.Result
import java.lang.Exception

class Repository @Inject constructor(
    private val application: MyApplication,
    private val apiService: ApiService,
    private val moshi: Moshi
) {
    private val errorMessagesAdapter = moshi.adapter(ErrorMessages::class.java)

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
                    }?.errors.toString()
                }.getOrElse {
                    "Unknown Exception"
                }
                throw HandledException(errorMessage, response.code())
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
}
