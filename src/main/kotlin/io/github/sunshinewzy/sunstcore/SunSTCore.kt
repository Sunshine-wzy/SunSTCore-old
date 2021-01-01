package io.github.sunshinewzy.sunstcore

import io.github.sunshinewzy.sunstcore.listeners.PlayerListener
import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.bukkit.Bukkit
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

class SunSTCore : JavaPlugin() {
    
    companion object {
        private var pluginSunSTCore: JavaPlugin? = null
        val sunstScope = CoroutineScope(SupervisorJob())
        
        fun getPlugin(): JavaPlugin {
            return pluginSunSTCore!!
        }
    }
    
    val pluginManager: PluginManager = Bukkit.getServer().pluginManager
    
    
    override fun onEnable() {
        pluginSunSTCore = this
        
        logger.info("SunSTCore 加载成功！")
        
        DataManager.init()
        registerListeners()
        
    }

    override fun onDisable() {
        
    }
    
    
    private fun registerListeners() {
        pluginManager.registerEvents(PlayerListener, this)
    }
    
}