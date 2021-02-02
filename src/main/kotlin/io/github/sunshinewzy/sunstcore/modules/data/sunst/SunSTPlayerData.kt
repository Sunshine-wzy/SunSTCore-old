package io.github.sunshinewzy.sunstcore.modules.data.sunst

import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import io.github.sunshinewzy.sunstcore.modules.data.DataManager.getMap
import io.github.sunshinewzy.sunstcore.modules.data.SPlayerData
import io.github.sunshinewzy.sunstcore.modules.task.TaskProgress
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class SunSTPlayerData : SPlayerData {
    constructor(plugin: JavaPlugin, uuid: String, path: String = "SPlayer", saveTime: Long = 600_000): super(plugin, uuid, path, saveTime)
    constructor(plugin: JavaPlugin, uuid: UUID, path: String = "SPlayer", saveTime: Long = 600_000): this(plugin, uuid.toString(), path, saveTime)
    constructor(plugin: JavaPlugin, player: Player, path: String = "SPlayer", saveTime: Long = 600_000): this(plugin, player.uniqueId, path, saveTime)
    constructor(plugin: JavaPlugin, uuid: String, file: File): super(plugin, uuid, file)

    init {
        DataManager.sPlayerData[name] = this
    }
    
    val taskProgress = HashMap<String, TaskProgress>()
    val isFirstJoinGive = HashMap<String, Boolean>()
    
    
    override fun YamlConfiguration.modifyConfig() {
        set("taskProgress", taskProgress)
        set("isFirstJoinGive", isFirstJoinGive)
        
    }

    override fun YamlConfiguration.loadConfig() {
        getMap("taskProgress", taskProgress)
        getMap("isFirstJoinGive", isFirstJoinGive)
        
//        if(config.contains("taskProgress")){
//            val mapTaskProject = config["taskProgress"].castMap(String::class.java, MutableMap::class.java) ?: return
//            mapTaskProject.forEach forEach1@{ (tProjectName, tProject) ->
//                val resMapTaskStage = HashMap<String, MutableMap<String, Boolean>>()
//                val mapTaskStage = tProject.castMap(String::class.java, MutableMap::class.java) ?: return@forEach1
//                mapTaskStage.forEach forEach2@{ (tStageName, tStage) ->
//                    val mapTaskBase = tStage.castMap(String::class.java, Boolean::class.java) ?: return@forEach2
//                    resMapTaskStage[tStageName] = mapTaskBase
//                }
//                taskProgress[tProjectName] = resMapTaskStage
//            }
//        }
    }
}