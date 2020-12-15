package me.geckotravel.oolong.models

import android.content.Context
import android.content.SharedPreferences
import android.text.format.DateUtils
import me.geckotravel.oolong.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.roundToInt

data class TeaRecipe(
    val id: Int,
    val name: String,
    val brewingDurations: Map<BrewIntensity, Duration>,
    val brewingTemperatures: Map<BrewIntensity, Temperature>,
    var intensity: BrewIntensity = BrewIntensity.MEDIUM
) {
    val brewingDuration: Duration
        get() = brewingDurations[intensity] ?: brewingDurations.values.first()

    val brewingTemperature: Temperature
        get() = brewingTemperatures[intensity] ?: brewingTemperatures.values.first()
}

enum class BrewIntensity {
    LIGHT, MEDIUM, STRONG;

    fun toLocalizedString(context: Context): String {
        return when (this) {
            LIGHT -> context.getString(R.string.tea_intensity_light)
            MEDIUM -> context.getString(R.string.tea_intensity_medium)
            STRONG -> context.getString(R.string.tea_intensity_strong)
        }
    }
}

data class Temperature(
    val celsius: Int
): KoinComponent {

    private val sharedPreferences: SharedPreferences by inject()

    private val fahrenheit: Int
        get() = (celsius * 1.8 + 32).roundToInt().let { it - it % 5 } // round to 5

    override fun toString(): String {
        return if (sharedPreferences.getString("temperature_unit", "Celsius") == "Celsius") {
            "$celsius°C"
        } else {
            "$fahrenheit°F"
        }
    }
}

data class Duration(
    val minutes: Long = 0,
    val seconds: Long = 0
) {
    companion object {
        fun fromMilliseconds(milliseconds: Long): Duration {
            return Duration(
                minutes = milliseconds / 1000 / 60,
                seconds = milliseconds / 1000 % 60
            )
        }
    }

    val milliseconds = (minutes * 60 + seconds) * 1000

    override fun toString(): String {
        return DateUtils.formatElapsedTime(minutes * 60 + seconds)
    }
}