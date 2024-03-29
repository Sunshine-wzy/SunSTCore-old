package io.github.sunshinewzy.sunstcore

import io.github.sunshinewzy.sunstcore.bstats.Metrics
import io.github.sunshinewzy.sunstcore.commands.SunSTCommand
import io.github.sunshinewzy.sunstcore.listeners.*
import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import io.github.sunshinewzy.sunstcore.modules.machine.SMachineWrench
import io.github.sunshinewzy.sunstcore.modules.task.TaskProgress
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.item.SunSTItem
import io.github.sunshinewzy.sunstcore.objects.item.constructionstick.LineStick
import io.github.sunshinewzy.sunstcore.objects.item.constructionstick.RangeStick
import io.github.sunshinewzy.sunstcore.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.bukkit.Bukkit
import org.bukkit.configuration.serialization.ConfigurationSerialization
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
    

    override fun onEnable() {
        plugin = this

        registerSerialization()
        registerListeners()
        init()
        
        val metrics = Metrics(this, 10212)
        
        logger.info("SunSTCore 加载成功！")
        
        test()
    }

    override fun onDisable() {
        DataManager.saveData()
    }


    private fun init() {
        SItem.initAction()
        DataManager.init()
        SReflect.init()
        SunSTItem.init()
        SMachineWrench.init()
        SunSTCommand.init()
        
    }
    
    private fun registerListeners() {
        pluginManager.apply {
            registerEvents(SEventSubscriberListener1, this@SunSTCore)
            registerEvents(SEventSubscriberListener2, this@SunSTCore)
            registerEvents(SEventSubscriberListener3, this@SunSTCore)
            registerEvents(SEventSubscriberListener4, this@SunSTCore)
            
            registerEvents(BlockListener, this@SunSTCore)
        }
        
        SunSTSubscriber.init()
    }
    
    private fun registerSerialization() {
        ConfigurationSerialization.registerClass(TaskProgress::class.java)
        
        ConfigurationSerialization.registerClass(LineStick::class.java)
        ConfigurationSerialization.registerClass(RangeStick::class.java)
        
    }
    
    
    @SunSTTestApi
    private fun test() {
        
    }
    
}