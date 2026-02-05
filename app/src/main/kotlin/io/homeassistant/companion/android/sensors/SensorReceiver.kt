package com.goflow.app.sensors

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.nfc.NfcAdapter
import android.os.PowerManager
import dagger.hilt.android.AndroidEntryPoint
import com.goflow.app.BuildConfig
import com.goflow.app.common.sensors.AndroidOsSensorManager
import com.goflow.app.common.sensors.AudioSensorManager
import com.goflow.app.common.sensors.BatterySensorManager
import com.goflow.app.common.sensors.BluetoothSensorManager
import com.goflow.app.common.sensors.DNDSensorManager
import com.goflow.app.common.sensors.DisplaySensorManager
import com.goflow.app.common.sensors.KeyguardSensorManager
import com.goflow.app.common.sensors.LastRebootSensorManager
import com.goflow.app.common.sensors.LastUpdateManager
import com.goflow.app.common.sensors.LightSensorManager
import com.goflow.app.common.sensors.MobileDataManager
import com.goflow.app.common.sensors.NetworkSensorManager
import com.goflow.app.common.sensors.NextAlarmManager
import com.goflow.app.common.sensors.NfcSensorManager
import com.goflow.app.common.sensors.PhoneStateSensorManager
import com.goflow.app.common.sensors.PowerSensorManager
import com.goflow.app.common.sensors.PressureSensorManager
import com.goflow.app.common.sensors.ProximitySensorManager
import com.goflow.app.common.sensors.SensorManager
import com.goflow.app.common.sensors.SensorReceiverBase
import com.goflow.app.common.sensors.StepsSensorManager
import com.goflow.app.common.sensors.StorageSensorManager
import com.goflow.app.common.sensors.TimeZoneManager
import com.goflow.app.common.sensors.TrafficStatsManager
import com.goflow.app.settings.SettingsActivity

@AndroidEntryPoint
class SensorReceiver : SensorReceiverBase() {

    override val currentAppVersion: String
        get() = BuildConfig.VERSION_NAME

    override val managers: List<SensorManager>
        get() = MANAGERS

    companion object {
        val MANAGERS = listOf(
            ActivitySensorManager(),
            AndroidAutoSensorManager(),
            AndroidOsSensorManager(),
            AppSensorManager(),
            AudioSensorManager(),
            BatterySensorManager(),
            BluetoothSensorManager(),
            CarSensorManager(),
            DisplaySensorManager(),
            DNDSensorManager(),
            DynamicColorSensorManager(),
            DevicePolicyManager(),
            GeocodeSensorManager(),
            HealthConnectSensorManager(),
            KeyguardSensorManager(),
            LastAppSensorManager(),
            LastRebootSensorManager(),
            LastUpdateManager(),
            LightSensorManager(),
            LocationSensorManager(),
            MobileDataManager(),
            NetworkSensorManager(),
            NfcSensorManager(),
            NextAlarmManager(),
            NotificationSensorManager(),
            PhoneStateSensorManager(),
            PowerSensorManager(),
            PressureSensorManager(),
            ProximitySensorManager(),
            QuestSensorManager(),
            StepsSensorManager(),
            StorageSensorManager(),
            TimeZoneManager(),
            TrafficStatsManager(),
        )

        const val ACTION_REQUEST_SENSORS_UPDATE =
            "com.goflow.app.background.REQUEST_SENSORS_UPDATE"

        fun updateAllSensors(context: Context) {
            val intent = Intent(context, SensorReceiver::class.java)
            intent.action = ACTION_UPDATE_SENSORS
            context.sendBroadcast(intent)
        }
    }

    // Suppress Lint because we only register for the receiver if the android version matches the intent
    @SuppressLint("InlinedApi")
    override val skippableActions = mapOf(
        "android.app.action.NEXT_ALARM_CLOCK_CHANGED" to listOf(NextAlarmManager.nextAlarm.id),
        "android.bluetooth.device.action.ACL_CONNECTED" to listOf(BluetoothSensorManager.bluetoothConnection.id),
        "android.bluetooth.device.action.ACL_DISCONNECTED" to listOf(BluetoothSensorManager.bluetoothConnection.id),
        "com.oculus.intent.action.MOUNT_STATE_CHANGED" to listOf(QuestSensorManager.headsetMounted.id),
        "android.net.wifi.WIFI_AP_STATE_CHANGED" to listOf(NetworkSensorManager.hotspotState.id),
        BluetoothAdapter.ACTION_STATE_CHANGED to listOf(BluetoothSensorManager.bluetoothState.id),
        Intent.ACTION_SCREEN_OFF to listOf(PowerSensorManager.interactiveDevice.id),
        Intent.ACTION_SCREEN_ON to listOf(PowerSensorManager.interactiveDevice.id),
        PowerManager.ACTION_POWER_SAVE_MODE_CHANGED to listOf(PowerSensorManager.powerSave.id),
        PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED to listOf(PowerSensorManager.doze.id),
        NotificationManager.ACTION_INTERRUPTION_FILTER_CHANGED to listOf(DNDSensorManager.dndSensor.id),
        AudioManager.ACTION_MICROPHONE_MUTE_CHANGED to listOf(AudioSensorManager.micMuted.id),
        AudioManager.ACTION_SPEAKERPHONE_STATE_CHANGED to listOf(AudioSensorManager.speakerphoneState.id),
        AudioManager.RINGER_MODE_CHANGED_ACTION to listOf(AudioSensorManager.audioSensor.id),
        AudioSensorManager.VOLUME_CHANGED_ACTION to listOf(
            AudioSensorManager.volAccessibility.id,
            AudioSensorManager.volAlarm.id,
            AudioSensorManager.volCall.id,
            AudioSensorManager.volDTMF.id,
            AudioSensorManager.volNotification.id,
            AudioSensorManager.volMusic.id,
            AudioSensorManager.volRing.id,
            AudioSensorManager.volSystem.id,
        ),
        Intent.ACTION_MANAGED_PROFILE_UNAVAILABLE to listOf(DevicePolicyManager.isWorkProfile.id),
        Intent.ACTION_MANAGED_PROFILE_AVAILABLE to listOf(DevicePolicyManager.isWorkProfile.id),
        WifiManager.WIFI_STATE_CHANGED_ACTION to listOf(NetworkSensorManager.wifiState.id),
        NfcAdapter.ACTION_ADAPTER_STATE_CHANGED to listOf(NfcSensorManager.nfcStateSensor.id),
        Intent.ACTION_CONFIGURATION_CHANGED to listOf(
            DisplaySensorManager.screenOrientation.id,
            DisplaySensorManager.screenRotation.id,
        ),
    )

    override fun getSensorSettingsIntent(
        context: Context,
        sensorId: String,
        sensorManagerId: String,
        notificationId: Int,
    ): PendingIntent? {
        val intent = SettingsActivity.newInstance(context, SettingsActivity.Deeplink.Sensor(sensorId)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        }
        return PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_IMMUTABLE)
    }
}
