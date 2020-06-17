package tronku.project.firebasecloner

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.gson.Gson

private const val CLONER_SHARED_PREF = "firebase.cloner.pref"
private val context = FirebaseApp.getInstance().applicationContext
private val pref = context.getSharedPreferences(CLONER_SHARED_PREF, Context.MODE_PRIVATE)

val isUpdate = pref.getBoolean("isUpdate", false)

fun setUpdateValue(isData: Boolean) {
    pref.edit().putBoolean("isUpdate", isData).apply()
}