package com.fingerprintjs.android.playground.ui.foundation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fingerprintjs.android.playground.utils.animatedRotation


@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_3,
)
@Composable
fun PreviewInFingerprintScreen() {
    com.fingerprintjs.android.playground.ui.screens.home.fingerprint.PreviewFingerprintScreen()
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun SignalItem(
    name: String,
    value: String,
    isExpanded: Boolean,
    onExpand: (Boolean) -> Unit,
    tags: (@Composable RowScope.() -> Unit)? = null,
) {
    val textMeasurer = rememberTextMeasurer()
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        Column() {
            if (tags != null) {
                Row() { tags() }
                Spacer(Modifier.height(16.dp))
            }
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.W600,
            )
            Spacer(Modifier.height(4.dp))
            val style = MaterialTheme.typography.bodyMedium
            val overflow = TextOverflow.Ellipsis
            val layoutRes = textMeasurer.measure(
                text = AnnotatedString(text = value),
                maxLines = COLLAPSED_LINES_COUNT,
                style = style,
                overflow = overflow,
                constraints = this@BoxWithConstraints.constraints
            )
            Text(
                modifier = Modifier.animateContentSize(),
                text = value,
                style = style,
                maxLines = if (isExpanded) Int.MAX_VALUE else COLLAPSED_LINES_COUNT,
                overflow = overflow,
            )
            if (layoutRes.hasVisualOverflow) {
                Spacer(Modifier.height(8.dp))
                ExpandToggle(
                    expanded = isExpanded,
                    onExpand = onExpand,
                )
            }
        }
    }
}

@Composable
private fun ExpandToggle(
    expanded: Boolean,
    onExpand: (Boolean) -> Unit
) {
    Row(modifier = Modifier
        .clip(MaterialTheme.shapes.extraSmall)
        .clickable { onExpand.invoke(!expanded) }) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .animateContentSize(),
            text = if (expanded) "Collapse" else "Expand",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            modifier = Modifier
                .size(18.dp)
                .animatedRotation(rotated = expanded),
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "arrow dropdown"
        )
    }
}

private const val COLLAPSED_LINES_COUNT = 3
