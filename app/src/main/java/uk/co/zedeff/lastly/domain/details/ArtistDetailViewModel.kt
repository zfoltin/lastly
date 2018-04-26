package uk.co.zedeff.lastly.domain.details

import android.databinding.ObservableField
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import uk.co.zedeff.lastly.services.ApiClient
import uk.co.zedeff.lastly.services.ApiService
import uk.co.zedeff.lastly.services.ArtistBio


data class ArtistDetails(val name: String, val imageUrl: String, val bio: ArtistBio)

enum class Status {
    LOADING,
    LOADED
}

class ArtistDetailViewModel(private val apiClient: ApiService = ApiClient().apiClient) {

    val status = ObservableField(Status.LOADING)
    val artistDetails = ObservableField<ArtistDetails>(ArtistDetails("", "", ArtistBio("", "")))

    fun initWith(artistName: String, artistImageUrl: String) {
        artistDetails.set(ArtistDetails(artistName, artistImageUrl, ArtistBio("", "")))
    }

    fun loadInfo(artist: String): Single<Boolean> {
        status.set(Status.LOADING)
        return apiClient.artistInfo(artist)
                .retry(1)
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    artistDetails.set(ArtistDetails(it.artist.name, it.artist.image[4].text, it.artist.bio))
                    true
                }
                .doOnEvent { _, _ -> status.set(Status.LOADED) }
    }
}
