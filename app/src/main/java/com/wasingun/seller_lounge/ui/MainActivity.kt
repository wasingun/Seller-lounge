package com.wasingun.seller_lounge.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val hideOnDestinationList = listOf(
        R.id.dest_login,
        R.id.dest_trend_comparison_result,
        R.id.dest_post_content,
        R.id.dest_post_detail
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        val navController =
            supportFragmentManager.findFragmentById(binding.containerMain.id)?.findNavController()
        navController?.let {
            binding.bnvMain.setupWithNavController(it)
        }
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in hideOnDestinationList) {
                binding.bnvMain.visibility = View.GONE
            } else {
                binding.bnvMain.visibility = View.VISIBLE
            }
        }
    }
}