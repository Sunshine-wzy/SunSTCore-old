package io.github.sunshinewzy.sunstcore.modules.machine

import io.github.sunshinewzy.sunstcore.objects.SBlock
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

abstract class TimerMachine(structure: Array<Array<Array<SBlock>>>, private val plugin: JavaPlugin, val period: Long) : SMachine(structure) {
    
    init {
        Bukkit.getScheduler().runTaskTimer(plugin, {
            runMachine()
        }, period, period)
    }
    
}