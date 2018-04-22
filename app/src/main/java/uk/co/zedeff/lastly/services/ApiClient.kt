package uk.co.zedeff.lastly.services

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.zedeff.lastly.BuildConfig


data class ArtistSearchResult(val results: ArtistSearchResults)
data class ArtistSearchResults(@SerializedName("artistmatches") val artistMatches: ArtistMatches)
data class ArtistMatches(@Suppress("ArrayInDataClass") val artist: Array<Artist>)
data class Artist(val name: String, val listeners: String, val streamable: String, val url: String, @Suppress("ArrayInDataClass") val image: Array<ArtistImage>)
data class ArtistImage(@SerializedName("#text") val text: String, val size: String)

interface ApiService {
    @GET("2.0/?method=artist.search&api_key=5f2007448583f41dc8f211e455d90b1b&format=json")
    fun artistSearch(@Query("artist") artist: String): Single<ArtistSearchResult>
}

class ApiClient {
    val apiClient: ApiService by lazy(this::createApiClient)
    private val baseUrl = "https://ws.audioscrobbler.com/"

    private fun createApiClient(): ApiService {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build()
        return retrofit.create(ApiService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }
}
