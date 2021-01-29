package io.github.sunshinewzy.sunstcore.utils

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.PlayerDeathEvent

object SEventSubscribe {
    
    private val subscribes = HashMap<String, ArrayList<(Event) -> Unit>>()
//    private val subscribes = HashMap<Class<out Event>, ArrayList<(Event) -> Unit>>()
    
    
    fun <E: Event> subscribeEvent(eventClass: Class<out E>, block: (event: E) -> Unit) {
        val name = eventClass.name
        val eventBlock = block as (Event) -> Unit
        val list = subscribes[name]
        if(list == null){
            subscribes[name] = arrayListOf(eventBlock)
        }
        else list.add(eventBlock)
    }
    
    fun <E: Event> callSubscribeEvent(event: E) {
        val list = subscribes[event.javaClass.name]
        list?.forEach { 
            val block = it as (E) -> Unit
            block(event)
        }
    }
    
        
    fun test() {
        subscribeEvent<PlayerDeathEvent> { 
            deathMessage = "你原地升天了"
        }
        
        subscribeEvent<BlockBreakEvent> {
            player.sendMessage("你破坏了 ${block.type.name}")
        }
        
    }
}


inline fun <reified E: Event> subscribeEvent(noinline block: E.() -> Unit) = subscribeEvent(E::class.java, block)

fun <E: Event> subscribeEvent(
    eventClass: Class<out E>,
    block: E.() -> Unit
) {
    SEventSubscribe.subscribeEvent(eventClass, block)
}
