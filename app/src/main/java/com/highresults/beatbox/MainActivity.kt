package com.highresults.beatbox

import android.os.Bundle
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.highresults.beatbox.databinding.ActivityMainBinding
import com.highresults.beatbox.databinding.ListItemSoundBinding


class MainActivity : AppCompatActivity() {
    private val beatBoxViewModel: BeatBoxViewModel by lazy {
        ViewModelProvider(this).get(BeatBoxViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (beatBoxViewModel.beatBox == null)
            beatBoxViewModel.beatBox = BeatBox(assets)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = SoundAdapter(beatBoxViewModel.beatBox!!.sounds)
        }
        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val progressFloat = (progress.toFloat() / 20) + 0.5f
                beatBoxViewModel.beatBox!!.rate = progressFloat
                binding.seekBarText.text = "Speed rate = $progressFloat"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private inner class SoundHolder(private val binding: ListItemSoundBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        init {
            binding.viewModel = SoundViewModel(beatBoxViewModel.beatBox!!)
        }

        fun bind(sound: Sound) {
            binding.apply {
                viewModel?.sound = sound
                executePendingBindings()
            }
        }

    }

    private inner class SoundAdapter(private val sounds: List<Sound>) :
        RecyclerView.Adapter<SoundHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = ListItemSoundBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            binding.lifecycleOwner = this@MainActivity
            return SoundHolder(binding)
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = sounds[position]
            holder.bind(sound)
        }

        override fun getItemCount() = sounds.size
    }

    override fun onDestroy() {
        super.onDestroy()
        beatBoxViewModel.beatBox?.release()
    }
}


