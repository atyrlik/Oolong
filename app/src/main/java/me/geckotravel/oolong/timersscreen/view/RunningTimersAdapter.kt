package me.geckotravel.oolong.timersscreen.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import me.geckotravel.oolong.R
import me.geckotravel.oolong.databinding.TeaTimerItemBinding
import me.geckotravel.oolong.models.TeaTimer

class RunningTimersAdapter(
        var runningTimers: List<TeaTimer>,
        val onTimerDismissed: (timer: TeaTimer) -> Unit
) : RecyclerView.Adapter<RunningTimersAdapter.TeaTimerViewHolder>() {

    inner class TeaTimerViewHolder(private val binding: TeaTimerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: TeaTimer) {

            if (model.isFinished) {
                binding.teaName.text = binding.root.context.getString(R.string.running_timer_is_done_title, model.recipe.name)
                binding.remainingTime.isVisible = false
                binding.temperature.isVisible = false
            } else {
                binding.teaName.text = model.recipe.name
                binding.remainingTime.text = model.remainingTime.toString()
                binding.remainingTime.isVisible = true
                binding.temperature.isVisible = true
                binding.temperature.text = model.recipe.brewingTemperature.toString()
            }

            binding.actionDismiss.setOnClickListener { onTimerDismissed(model) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeaTimerViewHolder {
        val binding = TeaTimerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return TeaTimerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeaTimerViewHolder, position: Int) = holder.bind(runningTimers[position])

    override fun getItemCount() = runningTimers.size

}