package jp.co.myowndict.data

import jp.co.myowndict.di.RequireAuth
import jp.co.myowndict.model.SentenceContainer
import jp.co.myowndict.model.SpeechText
import jp.co.myowndict.model.Token
import jp.co.myowndict.model.Uuid
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
    @GET("/api/user/sentences/")
    suspend fun getSentences(): Response<SentenceContainer>

    @RequireAuth
    @DELETE("/api/user/sentences/{id}/")
    suspend fun deleteSentence(@Path("id") sentence: String): Response<Unit>
}
