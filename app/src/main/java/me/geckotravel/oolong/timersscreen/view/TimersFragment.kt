package me.geckotravel.oolong.timersscreen.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import me.geckotravel.oolong.R
import me.geckotravel.oolong.databinding.TimersFragmentBinding
import me.geckotravel.oolong.timersscreen.TimerService
import me.geckotravel.oolong.timersscreen.viewmodel.TimersViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class TimersFragment : Fragment() {

    companion object {
        fun newInstance() = TimersFragment()
    }

    private val viewModel: TimersViewModel by viewModel()
    private lateinit var binding: TimersFragmentBinding

    private var timerService: TimerService? = null
    private var isBound = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.LocalBinder
            timerService = binder.getService()
            timerService?.stopRingtone() // stop ringtone when the app goes in foreground
            isBound = true
            observeTimerService() // subscribe to running timers
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()

        Intent(context, TimerService::class.java).also { intent ->
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.unbindService(connection)
        isBound = false
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        with (TimersFragmentBinding.inflate(inflater, container, false)) {
            binding = this
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.teaRecipesList.adapter = TeaRecipesAdapter(
                teaRecipes = emptyList(),
                onTimerStarted = {
                    timerService?.startTimer(it)
                    // we start the service manually to keep it alive
                    // even if the app is closed
                    Intent(context, TimerService::class.java).also { intent ->
                        activity?.startService(intent)
                    }
                },
                onRecipeIntensityChanged = {
                    viewModel.updateRecipe(it)
                }
        )

        binding.runningTimersList.adapter = RunningTimersAdapter(
                runningTimers = emptyList(),
                onTimerDismissed = {
                    viewModel.removeTimer(it)
                    timerService?.dismissTimer(it)
                }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.teaRecipes.observe(viewLifecycleOwner, { teaTimers ->
            with (binding.teaRecipesList.adapter as TeaRecipesAdapter) {
                this.teaRecipes = teaTimers
                notifyDataSetChanged()
            }
        })

        viewModel.runningTimers.observe(viewLifecycleOwner, { runningTimers ->
            with (binding.runningTimersList.adapter as RunningTimersAdapter) {
                this.runningTimers = runningTimers
                notifyDataSetChanged()
            }

            if (runningTimers.isEmpty()) {
                binding.motionLayout.transitionToState(R.id.no_timers_running)
            } else {
                binding.motionLayout.transitionToState(R.id.timers_running)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,requireView().findNavController())
                || super.onOptionsItemSelected(item)

    }

    private fun observeTimerService() {
        timerService?.runningTimers?.observe(viewLifecycleOwner, {
            viewModel.setRunningTimers(it)
        })
    }
}