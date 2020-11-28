package io.github.sunshinewzy.sunstcore

import io.github.sunshinewzy.sunstcore.listeners.PlayerListener
import io.github.sunshinewzy.sunstcore.modules.task.ItemTask
import io.github.sunshinewzy.sunstcore.modules.task.TaskStage
import io.github.sunshinewzy.sunstcore.objects.orderWith
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class SunSTCore : JavaPlugin() {
    
    val pluginManager = Bukkit.getServer().pluginManager
    
    
    override fun onEnable() {
        
        logger.info("SunSTCore 加载成功！")
        
        
        registerListeners()
        
        
        
    }

    override fun onDisable() {
        
    }
    
    
    private fun registerListeners() {
        pluginManager.registerEvents(PlayerListener, this)
    }
}