package com.bcn.deepfocus.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bcn.deepfocus.navigation.NavigationItem

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    currentRoute: String,
    navigationItem: List<NavigationItem>
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.surface),
        tonalElevation = 8.dp,
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = modifier,
        ) {
            navigationItem.forEachIndexed{ index, item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    icon = {
                        if (index == 3) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            ) {
                                Icon(imageVector = item.icon, contentDescription = "")
                            }
                        } else {
                            Icon(imageVector = item.icon, contentDescription = "")
                        }
                    },
                    label = { Text(item.title) },
                    onClick = {
                        navController.navigate(item.route){
                            // clear upper destination but still ensure retain state
                            popUpTo(navController.graph.startDestinationId){ saveState = true }
                            launchSingleTop = true //prevent duplicate same screen
                            restoreState = true //saveState -> restoreState when return
                        }
                    }
                )
            }
        }
    }
}