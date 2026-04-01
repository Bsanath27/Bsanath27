package com.skyrik.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.skyrik.core.ui.theme.SkyrikTheme

/**
 * SkyrikBottomSheet — branded modal bottom sheet with the Skyrik shape and color tokens.
 *
 * Wraps [ModalBottomSheet] from Material 3 and applies:
 *  - [SkyrikShapes.shapeTopRounded] as the container shape
 *  - [MaterialTheme.colorScheme.surface] as the container color
 *  - Navigation-bar inset padding so content doesn't overlap the gesture bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkyrikBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
    content: @Composable ColumnScope.() -> Unit,
) {
    val shapes = SkyrikTheme.shapes
    val spacing = SkyrikTheme.spacing

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = shapes.shapeTopRounded,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = spacing.space_base),
        ) {
            content()
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SkyrikBottomSheetPreview() {
    SkyrikTheme(darkTheme = true) {
        // Cannot preview modal sheets directly; shown as an inline column mock
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = "Where to?",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
