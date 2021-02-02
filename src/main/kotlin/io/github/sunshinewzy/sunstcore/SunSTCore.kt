package io.github.sunshinewzy.sunstcore

import io.github.sunshinewzy.sunstcore.listeners.SEventSubscriberListener1
import io.github.sunshinewzy.sunstcore.listeners.SEventSubscriberListener2
import io.github.sunshinewzy.sunstcore.listeners.SEventSubscriberListener3
import io.github.sunshinewzy.sunstcore.listeners.SunSTSubscriber
import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.bukkit.Bukkit
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

class SunSTCore : JavaPlugin() {
    
    companion object {
        private var plugin: JavaPlugin? = null
        val sunstScope = CoroutineScope(SupervisorJob())
        
        val logger by lazy { 
            getPlugin().logger
        }
        val pluginManager = Bukkit.getServer().pluginManager
        
        fun getPlugin(): JavaPlugin {
            return plugin!!
        }
    }
    
    val pluginManager: PluginManager = Bukkit.getServer().pluginManager
    
    
    override fun onEnable() {
        plugin = this

        DataManager.init()
        registerListeners()
        
        logger.info("SunSTCore 加载成功！")
        
        test()
        
    }

    override fun onDisable() {
        DataManager.saveData()
    }
    
    
    private fun registerListeners() {
        pluginManager.registerEvents(SEventSubscriberListener1, this)
        pluginManager.registerEvents(SEventSubscriberListener2, this)
        pluginManager.registerEvents(SEventSubscriberListener3, this)
        
        SunSTSubscriber.init()
    }
    
    private fun test() {
        
    }
    
}