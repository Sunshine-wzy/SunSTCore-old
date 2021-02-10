package io.github.sunshinewzy.sunstcore.events

import io.github.sunshinewzy.sunstcore.modules.machine.SMachine
import org.bukkit.Location
import org.bukkit.event.HandlerList

class SMachineRemoveEvent(sMachine: SMachine, val loc: Location) :SMachineEvent(sMachine) {
    
    override fun getHandlers(): HandlerList = sHandlers
    
    
    
    companion object {
        private val sHandlers = HandlerList()
    }
}