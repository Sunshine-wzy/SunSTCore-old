package io.github.sunshinewzy.sunstcore

import io.github.sunshinewzy.sunstcore.listeners.SEventSubscriberListener
import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import io.github.sunshinewzy.sunstcore.utils.SEventSubscribe
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

        DataManager.init()
        registerListeners()
        
        logger.info("SunSTCore 加载成功！")
        
        SEventSubscribe.test()
    }

    override fun onDisable() {
        
    }
    
    
    private fun registerListeners() {
        pluginManager.registerEvents(SEventSubscriberListener, this)
    }
    
}