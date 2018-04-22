package uk.co.zedeff.lastly.view

import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {
    protected val disposables = CompositeDisposable()

    protected fun showError(error: Throwable) {
        Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
