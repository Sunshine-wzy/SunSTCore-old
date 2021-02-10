package io.github.sunshinewzy.sunstcore.modules.machine

import org.bukkit.Location
import org.bukkit.entity.Player

sealed class SMachineRunEvent(val loc: Location) {
    
    class Manual(loc: Location, val player: Player) : SMachineRunEvent(loc)
    
    class Timer(loc: Location) : SMachineRunEvent(loc)

}
