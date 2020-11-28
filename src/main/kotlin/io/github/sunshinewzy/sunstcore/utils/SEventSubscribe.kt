package io.github.sunshinewzy.sunstcore.utils

import org.bukkit.event.Event
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent

object SEventSubscribe {
    
    private val subscribes = HashMap<Class<out Event>, ArrayList<() -> Unit>>()
    
    fun <T: Event> subscribeEvent(eventClazz: Class<out T>, runCode: () -> Unit) {
        val list = subscribes[eventClazz]
        if(list == null)
            subscribes[eventClazz] = ArrayList()
        else list.add(runCode)
    }
    
    fun <T: Event> callSubscribeEvent(event: T) {
        val list = subscribes[event.javaClass]
        list?.forEach {
            run(it)
        }
    }
    
}


fun subscribePlayerInteract(runCode: () -> Unit) {
    SEventSubscribe.subscribeEvent(PlayerInteractEvent::class.java, runCode)
}

fun subscribePlayerJoin(runCode: () -> Unit) {
    SEventSubscribe.subscribeEvent(PlayerJoinEvent::class.java, runCode)
}