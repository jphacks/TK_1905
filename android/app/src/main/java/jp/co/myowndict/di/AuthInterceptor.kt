package jp.co.myowndict.di

import jp.co.myowndict.model.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

annotation class RequireAuth

class AuthInterceptor(
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val invocation = request.tag(Invocation::class.java)
        val authAnnotation = invocation?.method()?.getAnnotation(RequireAuth::class.java)
        val token = TokenManager.get()
        if (authAnnotation != null && token != null) {
            request = request
                .newBuilder()
                .addHeader("Authorization", "JWT $token").build()
        }
        return chain.proceed(request)
    }
}
