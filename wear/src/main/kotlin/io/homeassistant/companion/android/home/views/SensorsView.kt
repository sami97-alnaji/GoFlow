package com.goflow.app.home.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.goflow.app.common.R as commonR
import com.goflow.app.common.sensors.SensorManager
import com.goflow.app.sensors.SensorReceiver
import com.goflow.app.theme.WearAppTheme
import com.goflow.app.theme.getFilledTonalButtonColors
import com.goflow.app.views.ListHeader
import com.goflow.app.views.ThemeLazyColumn

@Composable
fun SensorsView(onClickSensorManager: (SensorManager) -> Unit) {
    WearAppTheme {
        val sensorManagers = getSensorManagers()
        ThemeLazyColumn {
            item {
                ListHeader(id = commonR.string.sensors)
            }
            items(sensorManagers.size, { sensorManagers[it].name }) { index ->
                val manager = sensorManagers[index]
                Row {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = getFilledTonalButtonColors(),
                        label = { Text(stringResource(manager.name)) },
                        onClick = { onClickSensorManager(manager) },
                    )
                }
            }
        }
    }
}

@Composable
fun getSensorManagers(): List<SensorManager> {
    val context = LocalContext.current
    return SensorReceiver.MANAGERS.sortedBy { context.getString(it.name) }.filter { it.hasSensor(context) }
}

@Preview(device = WearDevices.LARGE_ROUND)
@Composable
private fun PreviewSensorsView() {
    CompositionLocalProvider {
        SensorsView {}
    }
}
