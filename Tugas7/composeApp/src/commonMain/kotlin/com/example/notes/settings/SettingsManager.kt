package com.example.notes.settings

import com.russhwolf.multiplatformsettings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SettingsManager(private val flowSettings: FlowSettings, scope: CoroutineScope) {

    companion object {
        private const val KEY_THEME = "theme"
        private const val KEY_SORT_ORDER = "sort_order"
        
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
        const val THEME_SYSTEM = "system"
        
        const val SORT_DATE_DESC = "date_desc"
        const val SORT_DATE_ASC = "date_asc"
    }

    val theme: StateFlow<String> = flowSettings
        .getStringFlow(KEY_THEME, THEME_SYSTEM)
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = THEME_SYSTEM
        )

    val sortOrder: StateFlow<String> = flowSettings
        .getStringFlow(KEY_SORT_ORDER, SORT_DATE_DESC)
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SORT_DATE_DESC
        )

    suspend fun updateTheme(newTheme: String) {
        flowSettings.putString(KEY_THEME, newTheme)
    }

    suspend fun updateSortOrder(newSortOrder: String) {
        flowSettings.putString(KEY_SORT_ORDER, newSortOrder)
    }
}
