package org.itstep.liannoi.sqliteroomusers.presentation

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.reactivex.plugins.RxJavaPlugins

abstract class AbstractActivity : AppCompatActivity() {

    protected fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, text, duration).show()
    }

    protected fun catchErrors() {
        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace)
    }

    protected fun processException(exception: String, method: String) {
        exception.also {
            Log.d(method, it)
            showToast(it, Toast.LENGTH_LONG)
        }
    }

    protected fun RecyclerView.layout(
        count: Int = 2,
        orientation: Int = StaggeredGridLayoutManager.VERTICAL
    ) {
        this.layoutManager = StaggeredGridLayoutManager(count, orientation)
    }
}
