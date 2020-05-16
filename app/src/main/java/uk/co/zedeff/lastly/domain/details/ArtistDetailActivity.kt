package uk.co.zedeff.lastly.domain.details

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import uk.co.zedeff.lastly.R
import uk.co.zedeff.lastly.databinding.ActivityArtistDetailBinding
import uk.co.zedeff.lastly.view.BaseActivity

class ArtistDetailActivity : BaseActivity() {
    private val viewModel = ArtistDetailViewModel()

    companion object {
        private const val EXTRA_ARTIST_NAME = "artist_name"
        private const val EXTRA_ARTIST_IMAGE_URL = "artist_image_url"

        fun newIntent(context: Context, artistName: String, artistImageUrl: String): Intent {
            val intent = Intent(context, ArtistDetailActivity::class.java)
            intent.putExtra(EXTRA_ARTIST_NAME, artistName)
            intent.putExtra(EXTRA_ARTIST_IMAGE_URL, artistImageUrl)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val artistName = intent.getStringExtra(EXTRA_ARTIST_NAME)
        val artistImageUrl = intent.getStringExtra(EXTRA_ARTIST_IMAGE_URL)
        viewModel.initWith(artistName, artistImageUrl)

        val binding: ActivityArtistDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_artist_detail)
        binding.viewModel = viewModel
        binding.artistImage.transitionName = "$artistName-image"
        binding.artistName.transitionName = artistName

        setSupportActionBar(findViewById(R.id.toolbar))

        disposables.add(viewModel.loadInfo(artistName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ }, this::showError))
    }
}
