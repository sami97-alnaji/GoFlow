package com.goflow.app.onboarding.integration

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.wear.activity.ConfirmationActivity
import dagger.hilt.android.AndroidEntryPoint
import com.goflow.app.common.R as commonR
import com.goflow.app.database.server.TemporaryServer
import com.goflow.app.databinding.ActivityIntegrationBinding
import com.goflow.app.home.HomeActivity
import com.goflow.app.util.adjustInset
import javax.inject.Inject

@AndroidEntryPoint
class MobileAppIntegrationActivity :
    AppCompatActivity(),
    MobileAppIntegrationView {
    companion object {
        private const val EXTRA_TEMP_SERVER = "temp_server"

        fun newInstance(context: Context, temporaryServer: TemporaryServer): Intent {
            return Intent(context, MobileAppIntegrationActivity::class.java).apply {
                putExtra(EXTRA_TEMP_SERVER, temporaryServer)
            }
        }
    }

    @Inject
    lateinit var presenter: MobileAppIntegrationPresenter
    private lateinit var binding: ActivityIntegrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val temporaryServer =
            IntentCompat.getParcelableExtra<TemporaryServer>(intent, EXTRA_TEMP_SERVER, TemporaryServer::class.java)
        if (temporaryServer == null) {
            finish()
            return
        }

        binding = ActivityIntegrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.deviceName.setText(Build.MODEL)

        binding.finish.setOnClickListener {
            presenter.onRegistrationAttempt(temporaryServer, binding.deviceName.text.toString())
        }

        adjustInset(applicationContext, binding, null)
    }

    override fun onResume() {
        super.onResume()

        binding.loadingView.visibility = View.GONE
        binding.constraintLayout.visibility = View.VISIBLE
    }

    override fun deviceRegistered() {
        val intent = HomeActivity.newInstance(this, fromOnboarding = true)
        // empty the back stack
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun showLoading() {
        binding.loadingView.visibility = View.VISIBLE
        binding.constraintLayout.visibility = View.GONE
    }

    override fun showError() {
        // Show failure message
        val intent = Intent(this, ConfirmationActivity::class.java).apply {
            putExtra(
                ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                ConfirmationActivity.FAILURE_ANIMATION,
            )
            putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(commonR.string.failed_registration))
        }
        startActivity(intent)
        binding.loadingView.visibility = View.GONE
        binding.constraintLayout.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        presenter.onFinish()
        super.onDestroy()
    }
}
