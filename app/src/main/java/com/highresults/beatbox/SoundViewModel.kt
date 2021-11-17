package com.highresults.beatbox

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable


class SoundViewModel(private val beatBox: BeatBox): BaseObservable() {

    fun onButtonClicked() {
        sound?.let {
            beatBox.play(it)
        }
    }

    // val title: MutableLiveData<String?> = MutableLiveData()

    var sound: Sound? = null
        set(sound) {
            field = sound
            notifyChange()//
//            title.postValue(sound?.name)
        }

    @get:Bindable
    val title: String?
        get() = sound?.name
}


