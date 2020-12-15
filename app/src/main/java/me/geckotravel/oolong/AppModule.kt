package me.geckotravel.oolong

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import me.geckotravel.oolong.media.RingtonePlayer
import me.geckotravel.oolong.media.RingtonePlayerImpl
import me.geckotravel.oolong.repositories.recipesrepository.RecipesRepository
import me.geckotravel.oolong.repositories.recipesrepository.RecipesRepositoryImpl
import me.geckotravel.oolong.timersscreen.viewmodel.TimersViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { TimersViewModel(get()) }

    single<RecipesRepository> { RecipesRepositoryImpl(get()) }

    single<SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(androidApplication()) }

    single<RingtonePlayer> { RingtonePlayerImpl(get(), get()) }
}