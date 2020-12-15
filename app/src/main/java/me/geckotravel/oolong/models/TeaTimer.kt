package me.geckotravel.oolong.models

data class TeaTimer(
        val id: Int,
        val recipe: TeaRecipe,
        val remainingTime: Duration,
        val isFinished: Boolean = false
)