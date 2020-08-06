package ru.zkv.barcodescanner.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ru.zkv.barcodescanner.presentation.main.MainFragment

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState ?: supportFragmentManager.commit {
            add(android.R.id.content, MainFragment.newInstance())
        }
    }
}