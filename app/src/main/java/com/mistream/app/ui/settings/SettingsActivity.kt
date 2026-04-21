package com.mistream.app.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.mistream.app.databinding.ActivitySettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsActivity : FragmentActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadCurrentValues()

        binding.btnSave.setOnClickListener {
            val rdToken = binding.etRdToken.text.toString().trim()
            val tmdbKey = binding.etTmdbKey.text.toString().trim()
            viewModel.saveSettings(rdToken, tmdbKey)
        }

        lifecycleScope.launch {
            viewModel.saveSuccess.collect { success ->
                if (success) {
                    Toast.makeText(this@SettingsActivity, "Configuración guardada", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun loadCurrentValues() {
        lifecycleScope.launch {
            val token = viewModel.getCurrentRdToken()
            if (token.isNotEmpty()) binding.etRdToken.setText(token)
        }
        lifecycleScope.launch {
            val key = viewModel.getCurrentTmdbKey()
            if (key.isNotEmpty()) binding.etTmdbKey.setText(key)
        }
    }
}
