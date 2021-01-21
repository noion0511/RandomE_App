package com.noion.randome.common

import android.app.Activity
import android.content.Intent
import com.noion.randome.App
import splitties.activities.start

//인텐트 플래그를 설정해 모든 작업들을 없애고 새 액티비티를 시작하는 함수

inline fun <reified A: Activity> clearTasksAndStartNewActivity() {
    App.instance.start<A> {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}