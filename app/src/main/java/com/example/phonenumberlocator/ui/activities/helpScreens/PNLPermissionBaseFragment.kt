package com.example.phonenumberlocator.ui.activities.helpScreens

import androidx.fragment.app.Fragment
import com.example.phonenumberlocator.pnlExtensionFun.getPermissionString
import com.example.phonenumberlocator.pnlExtensionFun.hasPermission


abstract class PNLPermissionBaseFragment : Fragment() {
    //permissions
    var actionOnPermissionHandlePermission: ((granted: Boolean) -> Unit)? = null
    var isAskingPermissionsHandlePermission = false

    var actionOnPermission: ((granted: Boolean) -> Unit)? = null
    var isAskingPermissions = false
    private val GENERIC_PERM_HANDLER = 100
    val TAG = "TESTINGFrag"


    fun handlePermission(permissionId: Int, callback: (granted: Boolean) -> Unit) {
        actionOnPermissionHandlePermission = null
        if (context?.hasPermission(permissionId) == true) {
            callback(true)
        } else {
            isAskingPermissionsHandlePermission = true
            actionOnPermissionHandlePermission = callback
            requestPermissions(
                arrayOf(getPermissionString(permissionId)),
                GENERIC_PERM_HANDLER
            )
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



}