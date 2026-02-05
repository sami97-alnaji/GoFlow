package com.goflow.app.developer.catalog

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.goflow.app.util.enableEdgeToEdgeCompat

class HAComposeCatalogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeCompat()
        setContent { HAComposeCatalogScreen() }
    }
}
