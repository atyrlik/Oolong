package me.geckotravel.oolong.timersscreen.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.geckotravel.oolong.databinding.TeaRecipeItemBinding
import me.geckotravel.oolong.models.BrewIntensity
import me.geckotravel.oolong.models.TeaRecipe

class TeaRecipesAdapter(
        var teaRecipes: List<TeaRecipe>,
        val onTimerStarted: (tea: TeaRecipe) -> Unit,
        val onRecipeIntensityChanged: (tea: TeaRecipe) -> Unit,
) : RecyclerView.Adapter<TeaRecipesAdapter.TeaRecipeViewHolder>() {

    init {
        // allow keeping the focus on slider when reloading the recycler view
        setHasStableIds(true)
    }

    inner class TeaRecipeViewHolder(private val binding: TeaRecipeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: TeaRecipe) {
            binding.teaName.text = model.name
            binding.duration.text = model.brewingDuration.toString()
            binding.temperature.text = model.brewingTemperature.toString()
            binding.intensity.text = model.intensity.toLocalizedString(binding.root.context)
            binding.brewIntensitySlider.value = model.intensity.ordinal.toFloat()
            binding.brewIntensitySlider.setLabelFormatter {
                BrewIntensity.values()[it.toInt()].toLocalizedString(binding.root.context)
            }

            binding.root.setOnClickListener{ onTimerStarted(model) }
            binding.brewIntensitySlider.addOnChangeListener { _, value, _ ->
                onRecipeIntensityChanged(model.copy(intensity = BrewIntensity.values()[value.toInt()]))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeaRecipeViewHolder {
        val binding = TeaRecipeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return TeaRecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeaRecipeViewHolder, position: Int) = holder.bind(teaRecipes[position])

    override fun getItemCount() = teaRecipes.size

    override fun getItemId(position: Int) = position.toLong()

}