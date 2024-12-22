package com.stdio.lilith.presentation

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.stdio.lilith.R
import com.stdio.lilith.domain.operationState.OperationState
import com.stdio.lilith.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCollectors()
    }

    private fun initCollectors() {
        val textView = findViewById<TextView>(R.id.text_view)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenEvents.collect { uiState ->
                    when (uiState) {
                        is OperationState.Error -> Toast.makeText(
                            this@MainActivity,
                            uiState.exception,
                            Toast.LENGTH_SHORT
                        ).show()

                        is OperationState.ErrorSingle -> Toast.makeText(
                            this@MainActivity,
                            "TADA!",
                            Toast.LENGTH_SHORT
                        ).show()

                        else -> {}
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.lastOperationState.collect { uiState ->
                    progressBar.isVisible = uiState is OperationState.Loading
                    when (uiState) {
                        is OperationState.Empty204 -> {}
                        is OperationState.Error -> textView.text = "Error"
                        is OperationState.Loading -> textView.text = "Loading..."
                        is OperationState.NoState -> {}
                        is OperationState.Success -> textView.text = "Success"
                        else -> {}
                    }
                }
            }
        }
    }
}