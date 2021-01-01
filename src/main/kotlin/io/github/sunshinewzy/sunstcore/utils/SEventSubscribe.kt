package io.github.sunshinewzy.sunstcore.utils

import io.github.sunshinewzy.sunstcore.SunSTCore
import kotlinx.coroutines.launch
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent

object SEventSubscribe {
    
    private val subscribes = HashMap<Class<out Event>, ArrayList<(Event) -> Unit>>()
//    private val subscribes = HashMap<Class<out Event>, SEventCodeBlockList<out Event>>()
    
    
    fun <E: Event> subscribeEvent(eventClazz: Class<out E>, block: (event: E) -> Unit) {
        val eventBlock = block as (Event) -> Unit
        val list = subscribes[eventClazz]
        if(list == null){
            subscribes[eventClazz] = arrayListOf(eventBlock)
        }
        else list.add(eventBlock)
    }
    
    fun <E: Event> callSubscribeEvent(event: E) {
        val list = subscribes[event.javaClass]
        list?.forEach { 
            SunSTCore.sunstScope.launch {
                it(event)
            }
        }
    }
    
    fun test() {
        subscribeEvent<PlayerJoinEvent> { 
            
        }
    }
}


inline fun <reified E: Event> subscribeEvent(noinline block: (event: E) -> Unit) {
    SEventSubscribe.subscribeEvent(E::class.java, block)
}

//fun subscribePlayerInteract(runCode: () -> Unit) {
//    SEventSubscribe.subscribeEvent(PlayerInteractEvent::class.java, runCode)
//}
//
//fun subscribePlayerJoin(runCode: () -> Unit) {
//    SEventSubscribe.subscribeEvent(PlayerJoinEvent::class.java, runCode)
//}
