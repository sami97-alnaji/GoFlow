package com.goflow.app.settings.gestures

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.goflow.app.common.data.prefs.PrefsRepository
import com.goflow.app.common.util.GestureAction
import com.goflow.app.common.util.HAGesture
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class GesturesViewModel @Inject constructor(private val prefsRepository: PrefsRepository) : ViewModel() {

    val gestureActions = mutableStateMapOf<HAGesture, GestureAction>()

    init {
        viewModelScope.launch {
            HAGesture.entries.forEach {
                gestureActions[it] = prefsRepository.getGestureAction(it)
            }
        }
    }

    fun setGestureAction(gesture: HAGesture, action: GestureAction) {
        viewModelScope.launch {
            prefsRepository.setGestureAction(gesture, action)
            gestureActions[gesture] = action
        }
    }
}
