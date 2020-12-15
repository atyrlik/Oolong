package me.geckotravel.oolong.repositories.recipesrepository

import android.content.Context
import me.geckotravel.oolong.R
import me.geckotravel.oolong.models.BrewIntensity
import me.geckotravel.oolong.models.Duration
import me.geckotravel.oolong.models.TeaRecipe
import me.geckotravel.oolong.models.Temperature

interface RecipesRepository {
    fun getRecipes(): List<TeaRecipe>
    fun updateRecipe(recipe: TeaRecipe)
}

class RecipesRepositoryImpl(
        val context: Context
) : RecipesRepository {

    private var recipes = listOf(
            TeaRecipe(1, context.getString(R.string.recipe_blac_tea),
                    brewingDurations = mapOf(
                            BrewIntensity.LIGHT to Duration(3, ),
                            BrewIntensity.MEDIUM to Duration(4, 0),
                            BrewIntensity.STRONG to Duration(5, 0)
                    ),
                    brewingTemperatures = mapOf(
                            BrewIntensity.LIGHT to Temperature(90),
                            BrewIntensity.MEDIUM to Temperature(90),
                            BrewIntensity.STRONG to Temperature(95)
                    )
            ),
            TeaRecipe(2, context.getString(R.string.recipe_green_tea),
                    brewingDurations = mapOf(
                            BrewIntensity.LIGHT to Duration(3, 0),
                            BrewIntensity.MEDIUM to Duration(3, 30),
                            BrewIntensity.STRONG to Duration(4, 0)
                    ),
                    brewingTemperatures = mapOf(
                            BrewIntensity.LIGHT to Temperature(80),
                            BrewIntensity.MEDIUM to Temperature(80),
                            BrewIntensity.STRONG to Temperature(85)
                    )
            ),
            TeaRecipe(3, context.getString(R.string.recipe_white_tea),
                    brewingDurations = mapOf(
                        BrewIntensity.LIGHT to Duration(1, 0),
                        BrewIntensity.MEDIUM to Duration(2, 0),
                        BrewIntensity.STRONG to Duration(3, 0)
                    ),
                    brewingTemperatures = mapOf(
                            BrewIntensity.LIGHT to Temperature(80),
                            BrewIntensity.MEDIUM to Temperature(80),
                            BrewIntensity.STRONG to Temperature(85)
                    )
            ),
            TeaRecipe(4, context.getString(R.string.recipe_oolong),
                    brewingDurations = mapOf(
                            BrewIntensity.LIGHT to Duration(3, 0),
                            BrewIntensity.MEDIUM to Duration(4, 0),
                            BrewIntensity.STRONG to Duration(5, 0)
                    ),
                    brewingTemperatures = mapOf(
                            BrewIntensity.LIGHT to Temperature(85),
                            BrewIntensity.MEDIUM to Temperature(90),
                            BrewIntensity.STRONG to Temperature(95)
                    )
            ),
            TeaRecipe(5, context.getString(R.string.recipe_darjeeling),
                    brewingDurations = mapOf(
                            BrewIntensity.LIGHT to Duration(3, 0),
                            BrewIntensity.MEDIUM to Duration(3, 30),
                            BrewIntensity.STRONG to Duration(4, 0)
                    ),
                    brewingTemperatures = mapOf(
                            BrewIntensity.LIGHT to Temperature(85),
                            BrewIntensity.MEDIUM to Temperature(85),
                            BrewIntensity.STRONG to Temperature(90)
                    )
            ),
            TeaRecipe(6, context.getString(R.string.recipe_puerh),
                    brewingDurations = mapOf(
                            BrewIntensity.LIGHT to Duration(3, 0),
                            BrewIntensity.MEDIUM to Duration(4, 0),
                            BrewIntensity.STRONG to Duration(5, 0)
                    ),
                    brewingTemperatures = mapOf(
                            BrewIntensity.LIGHT to Temperature(90),
                            BrewIntensity.MEDIUM to Temperature(90),
                            BrewIntensity.STRONG to Temperature(95)
                    )
            ),
            TeaRecipe(7, context.getString(R.string.recipe_tisane),
                    brewingDurations = mapOf(
                            BrewIntensity.LIGHT to Duration(5, 0),
                            BrewIntensity.MEDIUM to Duration(7, 0),
                            BrewIntensity.STRONG to Duration(9, 0)
                    ),
                    brewingTemperatures = mapOf(
                            BrewIntensity.LIGHT to Temperature(90),
                            BrewIntensity.MEDIUM to Temperature(95),
                            BrewIntensity.STRONG to Temperature(95)
                    )
            ),
            TeaRecipe(8, context.getString(R.string.recipe_rooibos),
                    brewingDurations = mapOf(
                            BrewIntensity.LIGHT to Duration(4, 0),
                            BrewIntensity.MEDIUM to Duration(5, 0),
                            BrewIntensity.STRONG to Duration(6, 0)
                    ),
                    brewingTemperatures = mapOf(
                            BrewIntensity.LIGHT to Temperature(90),
                            BrewIntensity.MEDIUM to Temperature(95),
                            BrewIntensity.STRONG to Temperature(95)
                    )
            ),
    )

    override fun getRecipes(): List<TeaRecipe> {
        return recipes
    }

    override fun updateRecipe(recipe: TeaRecipe) {
        recipes = recipes.map {
            if (it.id == recipe.id) recipe else it
        }
    }
}