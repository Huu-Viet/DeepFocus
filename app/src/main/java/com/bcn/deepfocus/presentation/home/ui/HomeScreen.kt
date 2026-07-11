package com.bcn.deepfocus.presentation.home.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bcn.deepfocus.R
import com.bcn.deepfocus.presentation.home.ui.components.TimeCircle
import com.bcn.deepfocus.presentation.home.viewmodel.HomeViewModel
import com.bcn.deepfocus.core.ui.theme.DeepFocusTheme
import com.bcn.deepfocus.navigation.Route
import com.bcn.deepfocus.presentation.home.model.TimerPhase
import com.bcn.deepfocus.presentation.home.ui.components.BottomSheetContent
import com.bcn.deepfocus.utils.toTimerText

private const val TAG = "HomeScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeVM: HomeViewModel = hiltViewModel()
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var pendingDeleteId by rememberSaveable { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // to open full content in sheet
    )
    val timerState = homeVM.timerState.collectAsStateWithLifecycle()
    val activeFocusSet = homeVM.activeFocusSet.collectAsStateWithLifecycle()
    val customFocusSets = homeVM.customFocusSets.collectAsStateWithLifecycle()
    val animatedProgress by animateFloatAsState(
        targetValue = timerState.value.progress,
        label = ""
    )

    Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .padding(16.dp)
                        .clip(shape = RoundedCornerShape(26.dp))
                        .background(color = MaterialTheme.colorScheme.primary)
                ) {
                    Row (
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.HourglassTop,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )

                        Spacer(Modifier.padding(4.dp))

                        Text(
                            text =  if (timerState.value.timerPhase == TimerPhase.FOCUS) {
                                stringResource(R.string.focus_time)
                            } else {stringResource(R.string.break_time)},
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(Modifier.size(60.dp))

                //TimeCircle
                TimeCircle(
                    progress = animatedProgress,
                    remainTime = timerState.value.remainingSeconds.toTimerText(),
                    setDuration = "${activeFocusSet.value.focusMinute}m focus / ${activeFocusSet.value.breakMinute}m break"
                )

                Spacer(Modifier.size(60.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            homeVM.reset()
                        },
                        modifier = Modifier
                            .size(70.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = CircleShape
                            ),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            modifier = Modifier.size(24.dp),
                            contentDescription = null
                        )
                    }

                    IconButton(
                        onClick = {
                            if (timerState.value.isRunning) homeVM.endSession()
                            else homeVM.start()
                        },
                        modifier = Modifier.size(90.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor =
                                if (timerState.value.isRunning) MaterialTheme.colorScheme.onError
                                else MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector =
                                if (timerState.value.isRunning) Icons.Default.Stop
                                else Icons.Default.PlayArrow,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp),
                            contentDescription = null
                        )
                    }
                    Spacer(Modifier.size(70.dp))
                }

                Spacer(Modifier.size(20.dp))

                Box(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .clickable(onClick = {showBottomSheet = true})
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = stringResource(R.string.set_duration), fontSize = 18.sp)
                        Row {
                            Text(text = "${activeFocusSet.value.focusMinute}m / ${activeFocusSet.value.breakMinute}m", fontSize = 18.sp)
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                    }
                }

                pendingDeleteId?.let { id ->
                    AlertDialog(
                        onDismissRequest = {pendingDeleteId = null},
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    homeVM.deleteFocusSet(id)
                                    pendingDeleteId = null
                                }
                            ) {
                                Text(stringResource(R.string.delete))
                            }
                        }
                    )
                }

                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.choose_duration),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Spacer(Modifier.size(20.dp))
                            BottomSheetContent(
                                activeFocusSet = activeFocusSet.value,
                                customFocusSets = customFocusSets.value,
                                changeSetDuration = { preset ->
                                    homeVM.changeSetDuration(preset)
                                    showBottomSheet = false
                                },
                                onCustomFocusSetSelected = { customSet ->
                                    homeVM.selectFocusSet(customSet)
                                    showBottomSheet = false
                                },
                                onContentSelected = { focusSet ->
                                    showBottomSheet = false
                                    navController.navigate(
                                        Route.TimeCustom().putArgs(
                                            focus = focusSet.focusMinute,
                                            breakTime = focusSet.breakMinute,
                                            id = focusSet.id)
                                    ){
                                        popUpTo(navController.graph.startDestinationId){ saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onCustomTime = {
                                    showBottomSheet = false
                                    navController.navigate(Route.TimeCustom().route){
                                        // clear upper destination but still ensure retain state
                                        popUpTo(navController.graph.startDestinationId){ saveState = true }
                                        launchSingleTop = true //prevent duplicate same screen
                                        restoreState = true //saveState -> restoreState when return
                                    }
                                },
                                onDeleteItem = { id ->
                                    pendingDeleteId = id
                                }
                            )
                        }
                    }
                }
            }
        }
    }