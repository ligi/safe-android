package io.gnosis.safe.ui.base.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import io.gnosis.safe.BuildConfig
import io.gnosis.safe.HeimdallApplication
import io.gnosis.safe.ScreenId
import io.gnosis.safe.Tracker
import io.gnosis.safe.di.components.DaggerViewComponent
import io.gnosis.safe.di.components.ViewComponent
import io.gnosis.safe.di.modules.ViewModule
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var tracker: Tracker

    abstract fun screenId(): ScreenId?

    override fun onCreate(savedInstanceState: Bundle?) {
        //FIXME: remove when dark mode is implemented
        if (delegate.localNightMode != AppCompatDelegate.MODE_NIGHT_NO) {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }
        HeimdallApplication[this].inject(this)
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG) {
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        }
        screenId()?.let {
            tracker.logScreen(it)
        }
    }

    protected fun viewComponent(): ViewComponent =
        DaggerViewComponent.builder()
            .applicationComponent(HeimdallApplication[this])
            .viewModule(ViewModule(this))
            .build()
}
