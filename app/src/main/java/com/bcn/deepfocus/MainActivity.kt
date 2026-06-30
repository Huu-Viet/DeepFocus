package com.bcn.deepfocus

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.InsertChart
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bcn.deepfocus.core.ui.components.BottomNavigationBar
import com.bcn.deepfocus.navigation.Navigation
import com.bcn.deepfocus.navigation.NavigationItem
import com.bcn.deepfocus.navigation.Route
import com.bcn.deepfocus.core.ui.theme.DeepFocusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigationItems = listOf(
                NavigationItem(
                    title = "Home",
                    icon = Icons.Rounded.Home,
                    route = Route.Home().route
                ),
                NavigationItem(
                    title = "Statistic",
                    icon = Icons.Rounded.InsertChart,
                    route = Route.Statistic().route
                ),
                NavigationItem(
                    title = "Setting",
                    icon = Icons.Rounded.Settings,
                    route = Route.Setting().route
                ),
            )
            DeepFocusTheme {
                RequestNotificationPermissionOnStart()
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

@Composable
fun RequestNotificationPermissionOnStart() {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        //TODO
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}