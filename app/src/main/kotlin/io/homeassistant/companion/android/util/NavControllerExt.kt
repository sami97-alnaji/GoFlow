package com.goflow.app.util

import androidx.navigation.NavController

/** Returns true if there is a previous destination in the back stack. */
fun NavController.canGoBack(): Boolean = previousBackStackEntry != null
