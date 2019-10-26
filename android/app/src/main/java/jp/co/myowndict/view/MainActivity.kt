package jp.co.myowndict.view

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import jp.co.myowndict.R

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
