package uk.co.zedeff.lastly.domain.main

import android.databinding.ObservableField
import io.reactivex.Single
import uk.co.zedeff.lastly.services.ApiClient
import uk.co.zedeff.lastly.services.ApiService

data class ArtistViewModel(val name: String, val imageUrl: String, val listeners: String)

enum class Status {
    EMPTY,
    LOADING,
    LOADED
}

class MainViewModel(private val apiClient: ApiService = ApiClient().apiClient) {

    val status = ObservableField(Status.EMPTY)

    fun searchArtist(artist: String): Single<Array<ArtistViewModel>> {
        status.set(Status.LOADING)
        return apiClient.artistSearch(artist)
                .retry(1)
                .map {
                    it.results.artistMatches.artist
                            .map { ArtistViewModel(it.name, it.image[3].text, it.listeners) }.toTypedArray()
                }
                .doOnEvent { _, _ -> status.set(Status.LOADED) }
    }
}
