package io.github.sunshinewzy.sunstcore.events

import io.github.sunshinewzy.sunstcore.modules.machine.SMachine
import org.bukkit.Location
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList

class SMachineAddEvent(sMachine: SMachine, val loc: Location) : SMachineEvent(sMachine), Cancellable {
    private var cancelledFlag = false
    
    
    override fun getHandlers(): HandlerList = sHandlers

    override fun isCancelled(): Boolean = cancelledFlag
    override fun setCancelled(flag: Boolean) {
        cancelledFlag = flag
    }

    companion object {
        private val sHandlers = HandlerList()
    }
}