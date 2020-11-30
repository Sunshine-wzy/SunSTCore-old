package io.github.sunshinewzy.sunstcore.modules.data

import io.github.sunshinewzy.sunstcore.modules.task.TaskBase
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

class SPlayerData(
    val uuid: UUID,
    path: String = "",
    saveTime: Long = 600_000,
) : SAutoSaveData(uuid.toString(), path, saveTime) {
    
    val taskProgress = HashMap<String, MutableMap<String, MutableList<String>>>()
    
    
    constructor(player: Player, path: String = "", saveTime: Long = 600_000): this(player.uniqueId, path, saveTime)
    
    
    override fun modifyConfig(config: YamlConfiguration) {
        
    }

    override fun deserialize(config: YamlConfiguration) {
        
    }


    fun TaskBase.getProgress(): Boolean {
        val projectName = taskStage.taskProject.projectName
        if(!taskProgress.containsKey(projectName))
            return false

        val projects = taskProgress[projectName] ?: return false
        val stageName = taskStage.stageName
        if(!projects.containsKey(stageName))
            return false

        val stages = projects[stageName] ?: return false
        if(!stages.contains(taskName))
            return false
        
        return true
//        val state = stages[1] ?: return false
//        return 
    }
}