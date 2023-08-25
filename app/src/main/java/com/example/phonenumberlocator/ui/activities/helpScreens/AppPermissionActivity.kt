package com.example.phonenumberlocator.ui.activities.helpScreens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ActivityAppPermissionBinding
import com.example.phonenumberlocator.pnlDatabases.TinyDB
import com.example.phonenumberlocator.pnlExtensionFun.toast
import com.example.phonenumberlocator.pnlHelper.IS_PERMISSION_ON
import com.example.phonenumberlocator.ui.MainActivity

class AppPermissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        binding.btnNext.setOnClickListener { launchHomeScreen() }
    }

    private fun setupViews() {
        val isSwitchOn = TinyDB.getInstance(this).getBoolean(IS_PERMISSION_ON)
        enablePermissionSwitch(isSwitchOn)

        binding.permissionSwitch.setOnToggledListener { _, isOn ->
            TinyDB.getInstance(this).putBoolean(IS_PERMISSION_ON, isOn)
            if (!isOn) toast(R.string.permission_required)
            runtimePer(isOn)
        }
    }

    private fun enablePermissionSwitch(isOn: Boolean) {
        binding.permissionSwitch.isOn = isOn
    }

    private fun launchHomeScreen() {
        val arePermissionsGranted = areAllPermissionsGranted()
        if (binding.permissionSwitch.isOn && arePermissionsGranted) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            toast(R.string.grant_all_permissions)
        }
    }

    private fun areAllPermissionsGranted(): Boolean {
        val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun runtimePer(isSwitchOn: Boolean) {
        val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (!requiredPermissions.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(
                this, requiredPermissions, 1
            )
        } else {
            // Handle permissions granted
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Handle permissions granted
                } else {
                    toast(R.string.permission_denied)
                }
            }
        }
    }
}

/** permission of camera,location,read external storage,contacts**/
/* private fun areAllPermissionsGranted(): Boolean {
        val requiredPermissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun runtimePer(isSwitchOn: Boolean) {
        val requiredPermissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (!requiredPermissions.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(
                this, requiredPermissions, 1
            )
        } else {
            // Handle permissions granted
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Handle permissions granted
                } else {
                    toast(R.string.permission_denied)
                }
            }
        }
    }
}*/
