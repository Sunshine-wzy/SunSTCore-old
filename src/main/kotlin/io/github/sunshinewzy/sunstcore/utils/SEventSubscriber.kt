package io.github.sunshinewzy.sunstcore.utils

import org.bukkit.event.Event

object SEventSubscriber {
    
    private val subscribers = HashMap<String, ArrayList<(Event) -> Unit>>()
    
    
    @Suppress("UNCHECKED_CAST")
    fun <E: Event> subscribeEvent(eventClass: Class<out E>, block: (event: E) -> Unit) {
        val name = eventClass.name
        val eventBlock = block as (Event) -> Unit
        val list = subscribers[name]
        if(list == null){
            subscribers[name] = arrayListOf(eventBlock)
        }
        else list.add(eventBlock)
    }
    
    fun <E: Event> callSubscribeEvent(event: E) {
        val list = subscribers[event.javaClass.name]
        list?.forEach { 
            val block = it as (E) -> Unit
            block(event)
        }
    }
    
}


inline fun <reified E: Event> subscribeEvent(noinline block: E.() -> Unit) = subscribeEvent(E::class.java, block)

fun <E: Event> subscribeEvent(
    eventClass: Class<out E>,
    block: E.() -> Unit
) {
    SEventSubscriber.subscribeEvent(eventClass, block)
}
