package com.mistream.app.di
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.mistream.app.data.api.*
import dagger.Module; import dagger.Provides; import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient; import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit; import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named; import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mistream_prefs")

@Module @InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore
    @Provides @Singleton fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }).build()
    @Provides @Singleton @Named("realdebrid") fun provideRdOkHttpClient(base: OkHttpClient): OkHttpClient = base.newBuilder()
        .addInterceptor { chain -> val token = RealDebridTokenHolder.token
            val req = if (token.isNotEmpty()) chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build() else chain.request()
            chain.proceed(req) }.build()
    @Provides @Singleton fun provideTmdbRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .client(okHttpClient.newBuilder().addInterceptor { chain ->
            chain.proceed(chain.request().newBuilder().url(chain.request().url.newBuilder().addQueryParameter("api_key", TmdbTokenHolder.apiKey).build()).build())
        }.build()).addConverterFactory(GsonConverterFactory.create()).build()
    @Provides @Singleton fun provideTmdbApiService(retrofit: Retrofit): TmdbApiService = retrofit.create(TmdbApiService::class.java)
    @Provides @Singleton @Named("torrentio") fun provideTorrentioRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://torrentio.strem.fun/").client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()
    @Provides @Singleton fun provideTorrentioApiService(@Named("torrentio") retrofit: Retrofit): TorrentioApiService = retrofit.create(TorrentioApiService::class.java)
    @Provides @Singleton @Named("rdRetrofit") fun provideRdRetrofit(@Named("realdebrid") okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.real-debrid.com/rest/1.0/").client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()
    @Provides @Singleton fun provideRdApiService(@Named("rdRetrofit") retrofit: Retrofit): RealDebridApiService = retrofit.create(RealDebridApiService::class.java)
}
object RealDebridTokenHolder { var token: String = "" }
object TmdbTokenHolder { var apiKey: String = "37f0f3da6ff530a870cfd96cce4e55cf" }