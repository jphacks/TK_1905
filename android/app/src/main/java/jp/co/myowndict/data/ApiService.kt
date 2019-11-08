package jp.co.myowndict.data

import jp.co.myowndict.di.RequireAuth
import jp.co.myowndict.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Auth
    @POST("/api/register/uuid/")
    suspend fun signUp(@Body uuid: Uuid): Response<Token>

    @POST("/api/auth/uuid/")
    suspend fun signIn(@Body uuid: Uuid): Response<Token>

    @RequireAuth
    @POST("/api/user/texts/")
    suspend fun sendText(@Body text: SpeechText): Response<Unit>

    @RequireAuth
    @GET("/api/user/sentences/?score__gt=0.8&order_by=-created_at")
    suspend fun getSentences(): Response<SentenceContainer>

    @RequireAuth
    @PUT("/api/user/sentences/{id}/")
    suspend fun editSentence(@Path("id") sentence: String, @Body text: SpeechTextNew): Response<Sentence>

    @RequireAuth
    @DELETE("/api/user/sentences/{id}/")
    suspend fun deleteSentence(@Path("id") sentence: String): Response<Unit>
}
