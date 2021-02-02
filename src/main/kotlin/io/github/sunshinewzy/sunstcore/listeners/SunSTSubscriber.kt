package io.github.sunshinewzy.sunstcore.listeners

import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.event.inventory.InventoryClickEvent

object SunSTSubscriber {
    fun init() {
        subscribeEvent<InventoryClickEvent> { 
            val holder = inventory.holder
            if(holder is SProtectInventoryHolder<*>){
                isCancelled = true
            }
        }
    }
}