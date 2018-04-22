package uk.co.zedeff.lastly.domain.main

import android.databinding.ObservableField
import io.reactivex.Single
import uk.co.zedeff.lastly.services.ApiService
import uk.co.zedeff.lastly.services.ApiClient

data class ArtistViewModel(val name: String, val imageUrl: String, val listeners: String)

enum class Status {
    Empty,
    Loading,
    Loaded
}

class MainViewModel(private val apiClient: ApiService = ApiClient().apiClient) {

    val status = ObservableField(Status.Empty)

    fun searchArtist(artist: String): Single<Array<ArtistViewModel>> {
        status.set(Status.Loading)
        return apiClient.artistSearch(artist)
                .map {
                    it.results.artistMatches.artist
                            .map { ArtistViewModel(it.name, it.image[3].text, it.listeners) }.toTypedArray()
                }
                .doOnEvent { _, _ -> status.set(Status.Loaded) }
    }
}
