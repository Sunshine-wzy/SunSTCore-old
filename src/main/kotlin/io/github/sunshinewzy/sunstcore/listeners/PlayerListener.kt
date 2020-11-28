package io.github.sunshinewzy.sunstcore.listeners

import io.github.sunshinewzy.sunstcore.utils.SEventSubscribe
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent

object PlayerListener : Listener {
    
    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        SEventSubscribe.callSubscribeEvent(e)
    }
    
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        SEventSubscribe.callSubscribeEvent(e)
    }
    
    
    
}