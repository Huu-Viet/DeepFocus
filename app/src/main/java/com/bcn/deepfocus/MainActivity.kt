package com.bcn.deepfocus

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bcn.deepfocus.core.ui.components.BottomNavigationBar
import com.bcn.deepfocus.core.ui.theme.DeepFocusTheme
import com.bcn.deepfocus.core.ui.theme.LanguageMode
import com.bcn.deepfocus.core.ui.theme.ThemeMode
import com.bcn.deepfocus.navigation.Navigation
import com.bcn.deepfocus.navigation.NavigationItem
import com.bcn.deepfocus.navigation.Route
import com.bcn.deepfocus.presentation.setting.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingViewModel: SettingViewModel = hiltViewModel()
            val themeMode by settingViewModel.themeMode.collectAsStateWithLifecycle()
            val languageMode by settingViewModel.languageMode.collectAsStateWithLifecycle()
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
            DeepFocusTheme(themeMode = themeMode) {
                RequestNotificationPermissionOnStart()
                LanguageUpdater(languageMode)
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute =navBackStackEntry?.destination?.route

                Scaffold(
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
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                        navHostController = navController,
                        themeMode = themeMode,
                        onThemeModeChange = settingViewModel::onThemeModeChange,
                        languageMode = languageMode,
                        onLanguageModeChange = settingViewModel::onLanguageModeChange
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageUpdater(languageMode: LanguageMode) {
    val activity = LocalContext.current as ComponentActivity

    LaunchedEffect(languageMode) {
        val targetLocale = when (languageMode) {
            LanguageMode.VIETNAMESE -> java.util.Locale("vi")
            LanguageMode.ENGLISH -> java.util.Locale.ENGLISH
        }

        if (activity.resources.configuration.locales[0].language != targetLocale.language) {
            updateActivityLocale(activity, targetLocale)
        }
    }
}

private fun updateActivityLocale(activity: ComponentActivity, locale: java.util.Locale) {
    val configuration = Configuration(activity.resources.configuration)
    java.util.Locale.setDefault(locale)
    configuration.setLocale(locale)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        configuration.setLocales(LocaleList(locale))
    }
    activity.resources.updateConfiguration(configuration, activity.resources.displayMetrics)
    activity.recreate()
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
