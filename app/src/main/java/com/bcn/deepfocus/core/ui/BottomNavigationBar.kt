package com.bcn.deepfocus.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bcn.deepfocus.navigation.NavigationItem

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    currentRoute: String,
    navigationItem: List<NavigationItem>
) {
    NavigationBar(
        modifier = modifier
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