package com.brunofelixdev.mytodoapp.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.databinding.ActivityMainBinding
import com.brunofelixdev.mytodoapp.ui.fragment.ItemFragmentDirections
import com.brunofelixdev.mytodoapp.ui.widget.AppWidget
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyToDoApp)
        initViews()
    }

    private fun initViews() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerMenuSetup()

        val extras = intent.getStringExtra(AppWidget.KEY_WIDGET)

        if (extras != null && extras == AppWidget.EXTRAS_VALUE) {
            Log.d(TAG, extras)
            val action = ItemFragmentDirections.navigateToItemForm(null)
            navController = findNavController(R.id.fragment)
            navController.navigate(action)
        } else {
            Log.d(TAG, "extras es null")
        }
    }

    private fun drawerMenuSetup() {
        navController = findNavController(R.id.fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}