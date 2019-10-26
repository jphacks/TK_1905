package jp.co.myowndict.di

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import jp.co.myowndict.BuildConfig
import jp.co.myowndict.MyApplication
import jp.co.myowndict.data.ApiService
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * AppModule
 * write provider function for Application level object like Retrofit or something.
 */
@Module
abstract class AppModule {
    @Module
    companion object {
        private const val apiUrl = ""

        @Provides
        @Singleton
        @JvmStatic
        fun provideMoshi(): Moshi {
            return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideFirebaseAnalytics(application: MyApplication): FirebaseAnalytics {
            return FirebaseAnalytics.getInstance(application)
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideAuthInterceptor(): AuthInterceptor {
            return AuthInterceptor()
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideSharedPreferences(application: MyApplication): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(application)
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideOkHttpBuilder(): OkHttpClient.Builder {
            return OkHttpClient.Builder()
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideOkHttpClient(
            builder: OkHttpClient.Builder,
            authInterceptor: AuthInterceptor
        ): OkHttpClient {
            builder
                .addInterceptor(authInterceptor)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = with(original.newBuilder()) {
                        header("X-Device-Type", "android")
                        header("X-App-Version", BuildConfig.VERSION_NAME)
                        method(original.method(), original.body())
                    }.build()
                    chain.proceed(request)
                }

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(logging)
            }

            return builder.build()
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideRetrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client)
                .build()
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideApiService(retrofit: Retrofit): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
}

class ChatOkHttpClientBuilder(val instance: OkHttpClient.Builder)

class ChatOkHttpClient(val instance: OkHttpClient)

class ChatRetrofit(val instance: Retrofit)
