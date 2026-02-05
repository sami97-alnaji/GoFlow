package com.goflow.app.vehicle

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.CarIcon
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Template
import com.goflow.app.common.R

class SwitchToDrivingOptimizedScreen(carContext: CarContext) : Screen(carContext) {

    override fun onGetTemplate(): Template {
        return MessageTemplate.Builder(carContext.getString(R.string.aa_driving_optimized_change))
            .setIcon(CarIcon.APP_ICON)
            .addAction(
                Action.Builder()
                    .setFlags(Action.FLAG_DEFAULT)
                    .setTitle(carContext.getString(R.string.continue_connect))
                    .setOnClickListener {
                        screenManager.pop()
                    }
                    .build(),
            ).build()
    }
}
