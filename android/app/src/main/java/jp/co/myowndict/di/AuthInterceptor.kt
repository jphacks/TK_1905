package jp.co.myowndict.di

import jp.co.myowndict.model.Token
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
        val token = Token.get()
        if (authAnnotation != null && token != null) {
            request = request
                .newBuilder()
                .addHeader("Authorization", "Bearer $token").build()
        }
        return chain.proceed(request)
    }
}
