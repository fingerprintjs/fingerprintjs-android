package com.fingerprintjs.android.playground.ui.foundation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.playground.ui.foundation.theme.AppTheme
import com.fingerprintjs.android.playground.utils.animatedRotation
import com.fingerprintjs.android.playground.utils.mappers.description

@Preview
@Composable
private fun PreviewValuePicker() {
    AppTheme {
        ValuePicker(
            modifier = Modifier.width(100.dp),
            title = "Version",
            values = Fingerprinter.Version.values().toList(),
            currentValue = Fingerprinter.Version.V_5,
            valueDescription = { this.description },
            onValueChanged = {}
        )
    }
}

@Composable
fun <T> ValuePicker(
    modifier: Modifier = Modifier,
    title: String,
    values: List<T>,
    currentValue: T,
    valueDescription: T.() -> String,
    onValueChanged: (T) -> Unit,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.wrapContentSize(),
        contentColor = AppTheme.extendedColorScheme.onSurfaceHighlighted,
    ) {
        BoxWithConstraints {
            Box(
                modifier = Modifier
                    .clickable {
                        dropdownExpanded = true
                    }
                    .padding(all = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.W500,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                    Icon(
                        modifier = Modifier
                            .height(24.dp)
                            .animatedRotation(rotated = dropdownExpanded),
                        imageVector = Icons.Outlined.ExpandMore, contentDescription = "arrow dropdown"
                    )
                }
            }
            DropdownMenu(
                expanded = dropdownExpanded,
                modifier = Modifier.width(this.maxWidth),
                offset = DpOffset(x = 0.dp, y = 8.dp),
                onDismissRequest = { dropdownExpanded = false },
            ) {
                values.forEach { value ->
                    val isChecked = value == currentValue
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = value.valueDescription(),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.W500,
                            )
                        },
                        colors = if (isChecked) MenuDefaults.itemColors(
                            textColor = AppTheme.extendedColorScheme.onSurfaceHighlighted,
                            trailingIconColor = AppTheme.extendedColorScheme.onSurfaceHighlighted,
                        ) else MenuDefaults.itemColors(),
                        trailingIcon = {
                            if (isChecked)
                                Icon(
                                    Icons.Filled.RadioButtonChecked,
                                    "checked radio button"
                                )
                            else
                                Icon(
                                    Icons.Filled.RadioButtonUnchecked,
                                    "checked radio button"
                                )
                        },
                        onClick = {
                            onValueChanged.invoke(value)
                            dropdownExpanded = false
                        },
                    )
                }
            }
        }
    }
}
