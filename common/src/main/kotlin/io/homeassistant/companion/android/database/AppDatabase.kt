package com.goflow.app.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.goflow.app.database.authentication.Authentication
import com.goflow.app.database.authentication.AuthenticationDao
import com.goflow.app.database.location.LocationHistoryDao
import com.goflow.app.database.location.LocationHistoryItem
import com.goflow.app.database.migration.Migration27to28
import com.goflow.app.database.migration.Migration36to37
import com.goflow.app.database.notification.NotificationDao
import com.goflow.app.database.notification.NotificationItem
import com.goflow.app.database.qs.TileDao
import com.goflow.app.database.qs.TileEntity
import com.goflow.app.database.sensor.Attribute
import com.goflow.app.database.sensor.EntriesTypeConverter
import com.goflow.app.database.sensor.Sensor
import com.goflow.app.database.sensor.SensorDao
import com.goflow.app.database.sensor.SensorSetting
import com.goflow.app.database.sensor.SensorSettingTypeConverter
import com.goflow.app.database.server.Server
import com.goflow.app.database.server.ServerDao
import com.goflow.app.database.settings.LocalNotificationSettingConverter
import com.goflow.app.database.settings.LocalSensorSettingConverter
import com.goflow.app.database.settings.Setting
import com.goflow.app.database.settings.SettingsDao
import com.goflow.app.database.wear.CameraTile
import com.goflow.app.database.wear.CameraTileDao
import com.goflow.app.database.wear.EntityStateComplications
import com.goflow.app.database.wear.EntityStateComplicationsDao
import com.goflow.app.database.wear.FavoriteCaches
import com.goflow.app.database.wear.FavoriteCachesDao
import com.goflow.app.database.wear.Favorites
import com.goflow.app.database.wear.FavoritesDao
import com.goflow.app.database.wear.ThermostatTile
import com.goflow.app.database.wear.ThermostatTileDao
import com.goflow.app.database.widget.ButtonWidgetDao
import com.goflow.app.database.widget.ButtonWidgetEntity
import com.goflow.app.database.widget.CameraWidgetDao
import com.goflow.app.database.widget.CameraWidgetEntity
import com.goflow.app.database.widget.MediaPlayerControlsWidgetDao
import com.goflow.app.database.widget.MediaPlayerControlsWidgetEntity
import com.goflow.app.database.widget.StaticWidgetDao
import com.goflow.app.database.widget.StaticWidgetEntity
import com.goflow.app.database.widget.TemplateWidgetDao
import com.goflow.app.database.widget.TemplateWidgetEntity
import com.goflow.app.database.widget.TodoWidgetDao
import com.goflow.app.database.widget.TodoWidgetEntity
import com.goflow.app.database.widget.WidgetBackgroundTypeConverter
import com.goflow.app.database.widget.WidgetTapActionConverter

@Database(
    entities = [
        Attribute::class,
        Authentication::class,
        Sensor::class,
        SensorSetting::class,
        ButtonWidgetEntity::class,
        CameraWidgetEntity::class,
        MediaPlayerControlsWidgetEntity::class,
        StaticWidgetEntity::class,
        TodoWidgetEntity::class,
        TemplateWidgetEntity::class,
        NotificationItem::class,
        LocationHistoryItem::class,
        TileEntity::class,
        Favorites::class,
        FavoriteCaches::class,
        CameraTile::class,
        ThermostatTile::class,
        EntityStateComplications::class,
        Server::class,
        Setting::class,
    ],
    version = 51,
    autoMigrations = [
        AutoMigration(from = 24, to = 25),
        AutoMigration(from = 25, to = 26),
        AutoMigration(from = 26, to = 27),
        AutoMigration(from = 27, to = 28, spec = Migration27to28::class),
        AutoMigration(from = 28, to = 29),
        AutoMigration(from = 29, to = 30),
        AutoMigration(from = 30, to = 31),
        AutoMigration(from = 31, to = 32),
        AutoMigration(from = 32, to = 33),
        AutoMigration(from = 33, to = 34),
        AutoMigration(from = 34, to = 35),
        AutoMigration(from = 35, to = 36),
        AutoMigration(from = 36, to = 37, spec = Migration36to37::class),
        AutoMigration(from = 38, to = 39),
        AutoMigration(from = 39, to = 40),
        AutoMigration(from = 41, to = 42),
        AutoMigration(from = 42, to = 43),
        AutoMigration(from = 43, to = 44),
        AutoMigration(from = 44, to = 45),
        AutoMigration(from = 45, to = 46),
        AutoMigration(from = 46, to = 47),
        AutoMigration(from = 47, to = 48),
        AutoMigration(from = 48, to = 49),
        AutoMigration(from = 49, to = 50),
        AutoMigration(from = 50, to = 51),
    ],
)
@TypeConverters(
    LocalNotificationSettingConverter::class,
    LocalSensorSettingConverter::class,
    EntriesTypeConverter::class,
    SensorSettingTypeConverter::class,
    WidgetBackgroundTypeConverter::class,
    WidgetTapActionConverter::class,
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun authenticationDao(): AuthenticationDao
    abstract fun sensorDao(): SensorDao
    abstract fun buttonWidgetDao(): ButtonWidgetDao
    abstract fun cameraWidgetDao(): CameraWidgetDao
    abstract fun mediaPlayCtrlWidgetDao(): MediaPlayerControlsWidgetDao
    abstract fun staticWidgetDao(): StaticWidgetDao
    abstract fun todoWidgetDao(): TodoWidgetDao
    abstract fun templateWidgetDao(): TemplateWidgetDao
    abstract fun notificationDao(): NotificationDao
    abstract fun locationHistoryDao(): LocationHistoryDao
    abstract fun tileDao(): TileDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun favoriteCachesDao(): FavoriteCachesDao
    abstract fun cameraTileDao(): CameraTileDao
    abstract fun thermostatTileDao(): ThermostatTileDao
    abstract fun entityStateComplicationsDao(): EntityStateComplicationsDao
    abstract fun serverDao(): ServerDao
    abstract fun settingsDao(): SettingsDao
}
