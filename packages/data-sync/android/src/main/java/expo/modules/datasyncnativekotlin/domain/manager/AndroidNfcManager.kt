package expo.modules.datasyncnativekotlin.domain.manager

import android.app.Activity

interface AndroidNfcManager {
    fun startListening(activity: Activity, onTagRead: (String) -> Unit): Boolean
    fun stopListening(activity: Activity)
    // fun writeTag(text: String): Boolean // Dành cho tính năng Ghi
}