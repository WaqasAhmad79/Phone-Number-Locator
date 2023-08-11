package com.example.phonenumberlocator

import android.app.Activity
import android.app.role.RoleManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Telephony
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.example.phonenumberlocator.pnlHelper.PERMISSION_CALL_PHONE
import com.example.phonenumberlocator.pnlHelper.PERMISSION_POST_NOTIFICATIONS
import com.example.phonenumberlocator.pnlHelper.PERMISSION_READ_CONTACTS
import com.example.phonenumberlocator.pnlHelper.PERMISSION_READ_SMS
import com.example.phonenumberlocator.pnlHelper.PERMISSION_SEND_SMS
import com.example.phonenumberlocator.pnlHelper.isMarshmallowPlus
import com.example.phonenumberlocator.pnlHelper.isQPlus
import com.example.phonenumberlocator.pnlHelper.isTiramisuPlus
import com.example.phonenumberlocator.pnlExtensionFun.getPermissionString
import com.example.phonenumberlocator.pnlExtensionFun.hasNotificationListenerGranted
import com.example.phonenumberlocator.pnlExtensionFun.hasPermission
import com.example.phonenumberlocator.pnlExtensionFun.showErrorToast
import org.jetbrains.anko.toast


abstract class PNLBaseClass<B : ViewBinding> : AppCompatActivity() {

    lateinit var binding: B
    open val TAG = "TESTING"

    var actionOnPermissionHandlePermission: ((granted: Boolean) -> Unit)? = null
    private val GENERIC_PERM_HANDLER = 100
    var isAskingPermissionsHandlePermission = false


    var actionOnPermission: ((granted: Boolean) -> Unit)? = null
    var isAskingPermissions = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
    }

    abstract fun getViewBinding(): B

    fun askDefaultSMSPermission(callback: (granted: Boolean) -> Unit) {
        isAskingPermissions = true
        actionOnPermission = callback
        //asking default sms
        if (isQPlus()) {
            val roleManager = ContextCompat.getSystemService(this, RoleManager::class.java)
            if (roleManager!!.isRoleAvailable(RoleManager.ROLE_SMS)) {
                if (roleManager.isRoleHeld(RoleManager.ROLE_SMS)) {
                    Log.d(TAG, "initViews: 1 default check other permissions")
                    askPermissions()
                } else {
                    Log.d(TAG, "initViews: 2 request default")
                    val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS)
                    someActivityResultLauncher.launch(intent)
                }
            } else {
                toast(R.string.unknown_error_occurred)
            }
        } else {
            if (Telephony.Sms.getDefaultSmsPackage(this) == packageName) {
                Log.d(TAG, "initViews: 3 not Q plus  ask other permissions")
                askPermissions()
            } else {
                Log.d(TAG, "initViews: 4 request default less Qplus")
                val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
                someActivityResultLauncher.launch(intent)
            }
        }

    }

    fun askDefaultDialerPermission(callback: (granted: Boolean) -> Unit) {
        isAskingPermissions = true
        actionOnPermission = callback
        if (isQPlus()) {
            val roleManager = getSystemService(RoleManager::class.java)
            if (roleManager!!.isRoleAvailable(RoleManager.ROLE_DIALER) && !roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                defaultDialerResultLauncher.launch(intent)
            }
        } else {
            Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).putExtra(
                TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                packageName
            ).apply {
                try {
                    defaultDialerResultLauncher.launch(this)
                } catch (e: ActivityNotFoundException) {

                } catch (e: Exception) {
                    showErrorToast(e)
                }
            }
        }
    }

    private val defaultDialerResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "initViews: on activity result")
            if (result.resultCode == Activity.RESULT_OK) {
                isAskingPermissions = false
                actionOnPermission?.invoke(true)
            } else {
                isAskingPermissions = false
                actionOnPermission?.invoke(false)
            }
        }


   fun handleNotificationPermission(callback: (granted: Boolean) -> Unit) {
        if (!isTiramisuPlus()) {
            callback(true)
        } else {
            handlePermission(PERMISSION_POST_NOTIFICATIONS) { granted ->
                callback(granted)
            }
        }
    }

    var someActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "initViews: on activity result")
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "initViews: 6 result okk")
                askPermissions()
            } else {
                Log.d(TAG, "initViews: result cancelled")
                actionOnPermission?.invoke(false)
            }

        }

    private fun askPermissions() {
        Log.d(TAG, "initViews: 7")
        handlePermission(PERMISSION_READ_SMS) {
            Log.d(TAG, "initViews: 8")
            if (it) {
                handlePermission(PERMISSION_SEND_SMS) {
                    Log.d(TAG, "initViews: 9")
                    if (it) {
                        handlePermission(PERMISSION_READ_CONTACTS) {
                            Log.d(TAG, "initViews: 10")
                            handleNotificationPermission { granted ->
                                if (!granted) {
                                    toast(R.string.no_post_notifications_permissions)
                                }
                            }
                            isAskingPermissions = false
                            actionOnPermission?.invoke(true)
                        }
                    } else {
//                        finish()
                    }
                }
            } else {
//                finish()
            }
        }
    }

    fun handlePermission(permissionId: Int, callback: (granted: Boolean) -> Unit) {
        actionOnPermissionHandlePermission = null
        if (hasPermission(permissionId)) {
            callback(true)
        } else {
            isAskingPermissionsHandlePermission = true
            actionOnPermissionHandlePermission = callback
            ActivityCompat.requestPermissions(
                this,
                arrayOf(getPermissionString(permissionId)),
                GENERIC_PERM_HANDLER
            )
        }
    }


    fun handleNotificationListenerPermission(callback: (granted: Boolean) -> Unit) {
        if (hasNotificationListenerGranted) {
            Log.d(TAG, "initViews: already granted return")
            callback.invoke(true)
            return
        }
        actionOnPermission = null
        actionOnPermission = callback
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d(TAG, "initViews: if-- request permission")
            notificationListenerResultLauncher.launch(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        } else {
            Log.d(TAG, "initViews: else-- request permission")
            notificationListenerResultLauncher.launch(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
        }
    }

    private val notificationListenerResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "initViews: on activity result")
            if (hasNotificationListenerGranted) {
                Log.d(TAG, "initViews: on activity result granted")
                actionOnPermission?.invoke(true)
            } else {
                Log.d(TAG, "initViews: on activity result not granted")
                actionOnPermission?.invoke(false)
            }
        }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        isAskingPermissionsHandlePermission = false
        if (requestCode == GENERIC_PERM_HANDLER && grantResults.isNotEmpty()) {
            actionOnPermissionHandlePermission?.invoke(grantResults[0] == 0)
        }
    }


  /*  fun callContactWithSim(recipient: String, useSimOne: Boolean) {
        handlePermission(PERMISSION_READ_PHONE_STATE) {
            val wantedSimIndex = if (useSimOne) 0 else 1
            val handle = getAvailableSIMCardLabels().sortedBy { it.id }[wantedSimIndex].handle
            launchCallIntent(recipient, handle)
        }
    }*/

    fun launchCallIntent(recipient: String, handle: PhoneAccountHandle? = null) {
        handlePermission(PERMISSION_CALL_PHONE) {
            val action = if (it) Intent.ACTION_CALL else Intent.ACTION_DIAL
            Intent(action).apply {
                data = Uri.fromParts("tel", recipient, null)

                if (handle != null && isMarshmallowPlus()) {
                    putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, handle)
                }
                launchActivityIntent(this)
            }
        }
    }

    fun dialNumber(phoneNumber: String, callback: (() -> Unit)? = null) {
        Intent(Intent.ACTION_DIAL).apply {
            data = Uri.fromParts("tel", phoneNumber, null)
            try {
                startActivity(this)
                callback?.invoke()
            } catch (e: ActivityNotFoundException) {
                toast(R.string.no_app_found)
            } catch (e: Exception) {
                showErrorToast(e)
            }
        }
    }


    fun startCallIntent(recipient: String) {
        launchCallIntent(recipient, null)
    }

    fun launchActivityIntent(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            toast(R.string.no_app_found)
        } catch (e: Exception) {
            showErrorToast(e)
        }
    }


}