package com.bcn.deepfocus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bcn.deepfocus.core.ui.BottomNavigationBar
import com.bcn.deepfocus.navigation.Navigation
import com.bcn.deepfocus.navigation.NavigationItem
import com.bcn.deepfocus.navigation.Route
import com.bcn.deepfocus.ui.theme.DeepFocusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigationItems = listOf(
                NavigationItem(
                    title = "Home",
                    icon = Icons.Default.Home,
                    route = Route.Home().route
                ),
                NavigationItem(
                    title = "History",
                    icon = Icons.Default.History,
                    route = Route.History().route
                ),
                NavigationItem(
                    title = "Setting",
                    icon = Icons.Default.Settings,
                    route = Route.Setting().route
                ),
            )
            DeepFocusTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute =navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        currentRoute?.let {
                            //put screens that don't need navigation bar into quotation mark
                            if(currentRoute !in ""){
                                BottomNavigationBar(
                                    navController = navController,
                                    currentRoute = currentRoute,
                                    navigationItem = navigationItems,
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Navigation(
                        modifier = Modifier.padding(innerPadding),
                        navHostController = navController
                    )
                }
            }
        }
    }
}