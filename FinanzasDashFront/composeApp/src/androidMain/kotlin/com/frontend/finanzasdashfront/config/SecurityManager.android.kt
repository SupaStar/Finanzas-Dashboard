// androidMain
package com.frontend.finanzasdashfront.config

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.frontend.finanzasdashfront.AppContextProvider
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

actual class SecurityManager actual constructor() {
    actual fun createSettings(): Settings {
        val context: Context = AppContextProvider.context
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val delegate = EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return SharedPreferencesSettings(delegate)
    }
}
