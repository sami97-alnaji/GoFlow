package com.goflow.app.settings.notification

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.goflow.app.common.R as commonR
import com.goflow.app.settings.addHelpMenuProvider
import com.goflow.app.settings.notification.views.NotificationChannelView
import com.goflow.app.util.compose.HomeAssistantAppTheme

@AndroidEntryPoint
class NotificationChannelFragment : Fragment() {

    val viewModel: NotificationViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                HomeAssistantAppTheme {
                    NotificationChannelView(notificationViewModel = viewModel)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addHelpMenuProvider(
            "https://companion.home-assistant.io/docs/notifications/notifications-basic#notification-channels",
        )
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(commonR.string.notification_channels)
    }
}
