package com.nasa.demo.presentation.ui.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.nasa.demo.presentation.ui.compose.screens.NasaImageScreen
import com.nasa.demo.presentation.viewmodel.NasaImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainComposeActivity : ComponentActivity() {
    @Inject
    lateinit var viewModel: NasaImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NasaImageScreen(viewModel)
        }
    }
}