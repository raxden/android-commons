package com.raxdenstudios.commons.android.intent

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.net.toUri
import com.raxdenstudios.commons.core.ext.EMPTY

/**
 * Builds the [Intent] that opens the screen or action described by an [IntentType].
 *
 * The returned intents are not started; the caller passes them to `startActivity`. Each intent
 * carries `FLAG_ACTIVITY_NEW_TASK`, so it can be started from an application context too.
 *
 * ## Setup
 *
 * Create the default implementation with the context to read the package name from:
 *
 * ```kotlin
 * val intentFactory: IntentFactory = IntentFactoryImpl(applicationContext)
 * ```
 *
 * ## Usage
 *
 * ```kotlin
 * startActivity(intentFactory.build(IntentType.ShareText(subject = "Check this", text = "Try the app")))
 * startActivity(intentFactory.build(IntentType.AppSettings))
 * ```
 */
interface IntentFactory {

    /** Returns a new [Intent] for [type]; every call builds a fresh instance. */
    fun build(
        type: IntentType,
    ): Intent
}

/**
 * Default [IntentFactory] that reads the package name from [context].
 *
 * [IntentType.ExactAlarmSettings] and [IntentType.FullScreenIntentSettings] only resolve on API 31+
 * and API 34+ respectively; on older releases starting them fails, so check the matching
 * `Context.isExactAlarmAllowed()` or `Context.isFullScreenIntentAllowed()` first.
 */
class IntentFactoryImpl(
    private val context: Context,
) : IntentFactory {

    override fun build(
        type: IntentType,
    ): Intent = when (type) {
        is IntentType.AppSettings -> buildAppSettingsIntent()
        is IntentType.PlayStore -> buildPlayStoreIntent()
        is IntentType.ShareText -> buildShareTextIntent(type)
        is IntentType.NotificationSettings -> buildNotificationSettingsIntent()
        is IntentType.ExactAlarmSettings -> buildExactAlarmSettingsIntent()
        is IntentType.FullScreenIntentSettings -> buildFullScreenIntentSettingsIntent()
    }

    private fun buildAppSettingsIntent(): Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    private fun buildPlayStoreIntent(): Intent = Intent(
        Intent.ACTION_VIEW,
        "market://details?id=${getReleasePackageName()}".toUri(),
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    private fun buildShareTextIntent(
        intentType: IntentType.ShareText,
    ): Intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, intentType.text)
        putExtra(Intent.EXTRA_SUBJECT, intentType.subject)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    private fun buildNotificationSettingsIntent(): Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    @SuppressLint("InlinedApi")
    private fun buildExactAlarmSettingsIntent(): Intent = Intent(
        Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
    ).apply {
        data = "package:${context.packageName}".toUri()
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    @SuppressLint("InlinedApi")
    private fun buildFullScreenIntentSettingsIntent(): Intent = Intent(
        Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT,
    ).apply {
        data = "package:${context.packageName}".toUri()
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    private fun getReleasePackageName(): String = context.packageName.replace(".debug", String.EMPTY)
}
