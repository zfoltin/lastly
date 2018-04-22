package uk.co.zedeff.lastly.domain.details

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uk.co.zedeff.lastly.R
import uk.co.zedeff.lastly.databinding.ActivityArtistDetailBinding
import uk.co.zedeff.lastly.view.BaseActivity

class ArtistDetailActivity : BaseActivity() {

    private val viewModel = ArtistDetailViewModel()

    companion object {
        private const val EXTRA_ARTIST_NAME = "artist_name"

        fun newIntent(context: Context, artistName: String): Intent {
            val intent = Intent(context, ArtistDetailActivity::class.java)
            intent.putExtra(EXTRA_ARTIST_NAME, artistName)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityArtistDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_artist_detail)
        binding.viewModel = viewModel

        setSupportActionBar(findViewById(R.id.toolbar))

        val artistName = intent.getStringExtra(ArtistDetailActivity.EXTRA_ARTIST_NAME)

        disposables.add(viewModel.loadInfo(artistName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ }, this::showError))
    }
}
