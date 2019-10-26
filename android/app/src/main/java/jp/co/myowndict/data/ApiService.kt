package jp.co.myowndict.data

import jp.co.myowndict.di.RequireAuth
import jp.co.myowndict.model.SpeechText
import jp.co.myowndict.model.Token
import jp.co.myowndict.model.Uuid
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Auth
    @POST("/api/register/uuid/")
    suspend fun signUp(@Body uuid: Uuid): Response<Token>

    @POST("/api/auth/uuid/")
    suspend fun signIn(@Body uuid: Uuid): Response<Token>

    @POST("/api/user/texts/")
    suspend fun sendText(@Body text: SpeechText): Response<Unit>
}
