package me.geckotravel.oolong.timersscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.geckotravel.oolong.models.TeaRecipe
import me.geckotravel.oolong.models.TeaTimer
import me.geckotravel.oolong.repositories.recipesrepository.RecipesRepository

class TimersViewModel(
        private val repository: RecipesRepository
) : ViewModel() {

    val teaRecipes: MutableLiveData<List<TeaRecipe>> = MutableLiveData(repository.getRecipes())

    val runningTimers: MutableLiveData<List<TeaTimer>> = MutableLiveData()

    fun setRunningTimers(timers: List<TeaTimer>) {
        runningTimers.postValue(timers)
    }

    fun removeTimer(timer: TeaTimer) {
        runningTimers.postValue(
                runningTimers.value?.filterNot { it == timer }
        )
    }

    fun updateRecipe(updatedRecipe: TeaRecipe) {
        repository.updateRecipe(updatedRecipe)
        teaRecipes.postValue(repository.getRecipes())
    }
}