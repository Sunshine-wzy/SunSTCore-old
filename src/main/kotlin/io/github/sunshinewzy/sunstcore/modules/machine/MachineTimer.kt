package io.github.sunshinewzy.sunstcore.modules.machine

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

abstract class MachineTimer(val plugin: JavaPlugin, private val period: Long, structure: MachineStructure) : SMachine(
    structure
) {
    
    init {
        Bukkit.getScheduler().runTaskTimer(plugin, {
            runMachine()
        }, period, period)
        
    }
    
}