package io.github.sunshinewzy.sunstcore.modules.data

import io.github.sunshinewzy.sunstcore.SunSTCore
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

abstract class SAutoSaveData(
    val name: String,
    val path: String = "",
    var saveTime: Long = 600_000
) {
    private val file = File(
        SunSTCore.getPlugin().dataFolder,
        if (path == "") "data/$name.yml"
        else "${path.replace("\\", "/")}/$name.yml"
    )
    
    val dataMap = HashMap<String, Any>()
    
    init {
        load(getConfig())
        
        Bukkit.getScheduler().runTaskTimer(SunSTCore.getPlugin(), {
            val config = getConfig()
            modifyConfig(config)
            save(config)
        }, saveTime, saveTime)
    }
    

    abstract fun modifyConfig(config: YamlConfiguration)
    
    abstract fun loadConfig(config: YamlConfiguration)
    
    
    fun save(config: YamlConfiguration) {
        dataMap.forEach { (key, value) ->
            config.set(key, value)
        }
        
        try {
            config.save(file)
        } catch (ex: IOException){
            ex.printStackTrace()
        }
    }
    
    fun load(config: YamlConfiguration) {
        loadConfig(config)
    }
    
    fun getConfig(): YamlConfiguration {
        return YamlConfiguration.loadConfiguration(file)
    }
}