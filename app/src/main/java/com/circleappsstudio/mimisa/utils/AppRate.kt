package com.circleappsstudio.mimisa.utils

import android.app.Activity
import android.content.Context

class AppRate {

    fun initAppRate(context: Context, activity: Activity) {
        /*
            Método para inicializar la función AppRate.
        */
        hotchemi.android.rate.AppRate.with(context)
            .setInstallDays(5)
            .setLaunchTimes(10)
            .setRemindInterval(3)
            .monitor()

        hotchemi.android.rate.AppRate.showRateDialogIfMeetsConditions(activity)

    }

}