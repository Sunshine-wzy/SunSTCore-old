package io.github.sunshinewzy.sunstcore

import io.github.sunshinewzy.sunstcore.listeners.PlayerListener
import org.bukkit.Bukkit
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

class SunSTCore : JavaPlugin() {
    
    companion object {
        private var pluginSunSTCore: JavaPlugin? = null
        
        fun getPlugin(): JavaPlugin {
            return pluginSunSTCore!!
        }
    }
    
    val pluginManager: PluginManager = Bukkit.getServer().pluginManager
    
    
    override fun onEnable() {
        pluginSunSTCore = getPlugin()
        
        logger.info("SunSTCore 加载成功！")
        
        
        registerListeners()
        
        
        
    }

    override fun onDisable() {
        
    }
    
    
    private fun registerListeners() {
        pluginManager.registerEvents(PlayerListener, this)
    }
}