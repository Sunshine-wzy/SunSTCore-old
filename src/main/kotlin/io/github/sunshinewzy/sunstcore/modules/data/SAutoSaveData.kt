package io.github.sunshinewzy.sunstcore.modules.data

import io.github.sunshinewzy.sunstcore.SunSTCore
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

abstract class SAutoSaveData(name: String, path: String = "", saveTime: Long = 600_000) {
    val file = File(SunSTCore.getPlugin().dataFolder, path)
    
    init {
        Bukkit.getScheduler().runTaskTimer(SunSTCore.getPlugin(), {
            val config = YamlConfiguration.loadConfiguration(file)
            
        }, saveTime, saveTime)
    }
}