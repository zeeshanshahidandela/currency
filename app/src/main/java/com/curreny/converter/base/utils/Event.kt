package com.curreny.converter.base.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

open class Event<out T>(protected val content: T) {

    var hasBeenHandled = false
        protected set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    open fun getContentIfNotHandled(): T {
        return if (hasBeenHandled) {
            throw EventHandledException()
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    open fun peekContent(): T = content
}

class EventHandledException(message: String? = "Content already handled") : Exception(message)

class MutableEvent<T : Any?>(value: T? = null) :
    MutableLiveData<Event<T>>(if (value == null) null else Event(value)) {
    fun postEvent(value: T) {
        postValue(Event(value))
    }

    fun setEvent(value: T) {
        setValue(Event(value))
    }
}

class EventObserver<T : Any?>(private val onEventUnhandledContent: (T) -> Unit) :
    Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        try {
            if (event != null) {
                onEventUnhandledContent(event.getContentIfNotHandled())
            }
        } catch (ehe: EventHandledException) {
            // Do nothing as event is already handled
        }
    }
}