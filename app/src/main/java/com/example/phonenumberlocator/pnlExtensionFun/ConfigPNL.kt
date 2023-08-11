package com.example.phonenumberlocator.pnlExtensionFun

import android.content.Context
import com.example.phonenumberlocator.BaseConfigPNL
import com.example.phonenumberlocator.pnlHelper.DISABLE_PROXIMITY_SENSOR
import com.example.phonenumberlocator.pnlHelper.DISABLE_SWIPE_TO_ANSWER
import com.example.phonenumberlocator.pnlHelper.GROUP_SUBSEQUENT_CALLS
import com.example.phonenumberlocator.pnlHelper.REMEMBER_SIM_PREFIX
import com.example.phonenumberlocator.pnlHelper.SPEED_DIAL


class ConfigPNL(context: Context) : BaseConfigPNL(context) {
    companion object {
        fun newInstance(context: Context) =  ConfigPNL(context)
    }

    var speedDial: String
        get() = prefs.getString(SPEED_DIAL, "")!!
        set(speedDial) = prefs.edit().putString(SPEED_DIAL, speedDial).apply()

//    fun getSpeedDialValues(): ArrayList<SpeedDial> {
//        val speedDialType = object : TypeToken<List<SpeedDial>>() {}.type
//        val speedDialValues = Gson().fromJson<ArrayList<SpeedDial>>(speedDial, speedDialType) ?: ArrayList(1)
//
//        for (i in 1..9) {
//            val speedDial = SpeedDial(i, "", "")
//            if (speedDialValues.firstOrNull { it.id == i } == null) {
//                speedDialValues.add(speedDial)
//            }
//        }
//
//        return speedDialValues
//    }

    fun removeCustomSIM(number: String) {
        prefs.edit().remove(REMEMBER_SIM_PREFIX + number).apply()
    }

    var disableSwipeToAnswer: Boolean
        get() = prefs.getBoolean(DISABLE_SWIPE_TO_ANSWER, false)
        set(disableSwipeToAnswer) = prefs.edit().putBoolean(DISABLE_SWIPE_TO_ANSWER, disableSwipeToAnswer).apply()

    var disableProximitySensor: Boolean
        get() = prefs.getBoolean(DISABLE_PROXIMITY_SENSOR, false)
        set(disableProximitySensor) = prefs.edit().putBoolean(DISABLE_PROXIMITY_SENSOR, disableProximitySensor).apply()


    var groupSubsequentCalls: Boolean
        get() = prefs.getBoolean(GROUP_SUBSEQUENT_CALLS, true)
        set(groupSubsequentCalls) = prefs.edit().putBoolean(GROUP_SUBSEQUENT_CALLS, groupSubsequentCalls).apply()
}
