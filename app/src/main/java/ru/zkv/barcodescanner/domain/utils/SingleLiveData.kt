package ru.zkv.barcodescanner.domain.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveData<T : Any>(initialValue: T? = null) : MutableLiveData<T>() {

    private val isAwaiting = AtomicBoolean(true)

    init {
        initialValue?.let { value = it }
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer {
            if (isAwaiting.compareAndSet(true, false))
                observer.onChanged(it)
        })
    }
}