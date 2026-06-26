package com.bcn.deepfocus.presentation.custom.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bcn.deepfocus.core.ui.theme.DeepFocusTheme
import com.bcn.deepfocus.presentation.custom.ui.components.MinuteSelector
import com.bcn.deepfocus.presentation.custom.viewmodel.TimeCustomViewModel

private const val TAG = "TimeCustomScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeCustomScreen(
    onBack: () -> Unit = {},
    viewModel: TimeCustomViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Show toast when warning message appears
    LaunchedEffect(uiState.value.warningMessage) {
        uiState.value.warningMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.dismissWarning()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar area
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            navigationIcon = {
                IconButton(onClick = {onBack()}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            title = {
                Text(
                    text = "Custom Timer",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        )

        Spacer(Modifier.size(16.dp))

        Text(
            text = "Set your preferred focus and break duration",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(Modifier.size(32.dp))

        // Focus Minute Selector
        MinuteSelector(
            label = "Focus Time",
            value = uiState.value.focusMinute,
            minValue = 1,
            maxValue = 180,
            onValueChange = viewModel::onFocusMinuteChange,
            onIncrement = viewModel::incrementFocusMinute,
            onDecrement = viewModel::decrementFocusMinute
        )

        Spacer(Modifier.size(20.dp))

        // Break Minute Selector
        MinuteSelector(
            label = "Break Time",
            value = uiState.value.breakMinute,
            minValue = 1,
            maxValue = (uiState.value.focusMinute * 0.3f).toInt().coerceAtLeast(1),
            onValueChange = viewModel::onBreakMinuteChange,
            onIncrement = viewModel::incrementBreakMinute,
            onDecrement = viewModel::decrementBreakMinute
        )

        Spacer(Modifier.size(24.dp))

        // Save Button
        Button(
            onClick = {
                viewModel.saveCustomFocusSet()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            enabled = !uiState.value.isSaving
        ) {
            Text(
                text = if (uiState.value.isSaving) "Saving..." else "Save & Start",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimeCustomScreenPreview() {
    DeepFocusTheme {
        TimeCustomScreen()
    }
}
