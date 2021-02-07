package io.github.sunshinewzy.sunstcore.modules.data

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.utils.getDataPath
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

/**
 * @param plugin 插件实例
 * @param name 保存的文件名
 * @param path 保存的路径(保存到 plugins/插件名/路径 下)
 * @param saveTime 自动保存时间间隔
 */
abstract class SAutoSaveData(
    private val plugin: JavaPlugin,
    val name: String,
    val path: String = "",
    val saveTime: Long = 600_000
) {
    private val file = File(
        plugin.dataFolder,
        if (path == "") "data/$name.yml"
        else "${path.replace("\\", "/")}/$name.yml"
    )
    
    val dataMap = HashMap<String, Any>()
    
    
    constructor(plugin: JavaPlugin, name: String, file: File): this(
        plugin,
        name,
        file.getDataPath(plugin)
    )
    
    init {
        Bukkit.getScheduler().runTaskTimer(SunSTCore.getPlugin(), {
            save()
        }, saveTime, saveTime)
    }

    /**
     * 保存文件前调用
     */
    abstract fun YamlConfiguration.modifyConfig()

    /**
     * 加载文件后调用
     */
    abstract fun YamlConfiguration.loadConfig()
    
    
    fun save() {
        val config = getConfig()
        config.modifyConfig()
        
        dataMap.forEach { (key, value) ->
            config.set(key, value)
        }
        
        try {
            config.save(file)
        } catch (ex: IOException){
            ex.printStackTrace()
        }
    }

    /**
     * 加载配置文件
     */
    fun load() {
        getConfig().loadConfig()
    }
    
    
    private fun getConfig(): YamlConfiguration {
        return YamlConfiguration.loadConfiguration(file)
    }
}