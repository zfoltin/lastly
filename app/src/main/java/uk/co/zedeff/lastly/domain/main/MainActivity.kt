package uk.co.zedeff.lastly.domain.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import uk.co.zedeff.lastly.R
import uk.co.zedeff.lastly.R.layout.activity_main
import uk.co.zedeff.lastly.databinding.ActivityMainBinding
import uk.co.zedeff.lastly.domain.details.ArtistDetailActivity
import uk.co.zedeff.lastly.view.BaseActivity

class MainActivity : BaseActivity() {
    private val viewModel = MainViewModel()
    private val adapter = ArtistListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setItemSelectedListener()

        initBinding()

        setSupportActionBar(findViewById(R.id.toolbar))

        handleIntent(intent)
    }

    private fun setItemSelectedListener() {
        adapter.setListener(object : OnArtistSelectedListener {
            override fun onArtistSelected(artist: ArtistViewModel, artistImageView: View, artistNameView: View) {
                val intent = ArtistDetailActivity.newIntent(this@MainActivity, artist.name, artist.imageUrl)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@MainActivity,
                        android.support.v4.util.Pair(artistImageView, "${artist.name}-image"),
                        android.support.v4.util.Pair(artistNameView, artist.name))
                startActivity(intent, options.toBundle())
            }
        })
    }

    private fun initBinding() {
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, activity_main)
        binding.viewModel = viewModel

        val recyclerView = binding.resultList
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_SEARCH) {
            val query = intent.getStringExtra(SearchManager.QUERY)

            disposables.add(viewModel.searchArtist(query)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ adapter.update(it) }, this::showError))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return true
    }
}
