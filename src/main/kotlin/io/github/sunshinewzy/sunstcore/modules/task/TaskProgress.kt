package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.utils.castMap
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Player

/**
 * 任务进度
 */
class TaskProgress() : ConfigurationSerializable {
    private val progress = HashMap<String, MutableMap<String, Boolean>>()

    
    init {
        
    }
    
    constructor(map: Map<String, Any>) : this() {
        map.forEach { (key, value) ->
            val mapCast = value.castMap(String::class.java, Boolean::class.java) ?: return@forEach
            progress[key] = mapCast
        }
    }
    
    
    override fun serialize(): MutableMap<String, Any> = mutableMapOf("progress" to progress)
    
    
    fun hasCompleteTask(p: Player, task: TaskBase): Boolean {
        val taskStage = task.taskStage
        
        if(progress.containsKey(taskStage.stageName)){
            val stagePro = progress[taskStage.stageName]
            if(stagePro?.containsKey(task.taskName) == true){
                val taskPro = stagePro[task.taskName]
                if(taskPro == true){
                    return true
                }
            }
        }
        
        return false
    }
    
    fun hasCompleteStage(p: Player, taskStage: TaskStage): Boolean {
        val finalTask = taskStage.finalTask ?: return true

        if(progress.containsKey(taskStage.stageName)){
            val stagePro = progress[taskStage.stageName]
            if(stagePro?.containsKey(finalTask.taskName) == true){
                val taskPro = stagePro[finalTask.taskName]
                if(taskPro == true){
                    return true
                }
            }
        }
        
        return false
    }
}