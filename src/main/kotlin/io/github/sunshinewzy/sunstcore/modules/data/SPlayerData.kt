package io.github.sunshinewzy.sunstcore.modules.data

import io.github.sunshinewzy.sunstcore.modules.task.TaskBase
import io.github.sunshinewzy.sunstcore.utils.castMap
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

class SPlayerData(
    val uuid: UUID,
    path: String = "",
    saveTime: Long = 600_000,
) : SAutoSaveData(uuid.toString(), path, saveTime) {
    
    val taskProgress = HashMap<String, MutableMap<String, MutableMap<String, Boolean>>>()
    
    
    constructor(player: Player, path: String = "", saveTime: Long = 600_000): this(player.uniqueId, path, saveTime)
    
    
    override fun modifyConfig(config: YamlConfiguration) {
        config.set("taskProgress", taskProgress)
    }

    override fun loadConfig(config: YamlConfiguration) {
        if(config.contains("taskProgress")){
            val mapTaskProject = config["taskProgress"].castMap(String::class.java, MutableMap::class.java) ?: return
            mapTaskProject.forEach forEach1@{ (tProjectName, tProject) -> 
                val resMapTaskStage = HashMap<String, MutableMap<String, Boolean>>()
                val mapTaskStage = tProject.castMap(String::class.java, MutableMap::class.java) ?: return@forEach1
                mapTaskStage.forEach forEach2@{ (tStageName, tStage) ->
                    val mapTaskBase = tStage.castMap(String::class.java, Boolean::class.java) ?: return@forEach2
                    resMapTaskStage[tStageName] = mapTaskBase
                }
                taskProgress[tProjectName] = resMapTaskStage
            }
        }
    }


    fun Player.hasCompleteTask(task: TaskBase): Boolean {
        val projectName = task.taskStage.taskProject.projectName
        if(!taskProgress.containsKey(projectName))
            return false

        val projects = taskProgress[projectName] ?: return false
        val stageName = task.taskStage.stageName
        if(!projects.containsKey(stageName))
            return false

        val stages = projects[stageName] ?: return false
        if(!stages.containsKey(task.taskName))
            return false

        return stages[task.taskName] ?: return false
    }
}
