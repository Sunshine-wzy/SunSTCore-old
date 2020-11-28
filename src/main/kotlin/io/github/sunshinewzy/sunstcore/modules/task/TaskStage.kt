package io.github.sunshinewzy.sunstcore.modules.task

import org.bukkit.Sound
import org.bukkit.inventory.ItemStack

class TaskStage(
    val taskProject: TaskProject,
    var stageName: String,
    var edgeItem: ItemStack,
    var openSound: Sound = Sound.ENTITY_HORSE_ARMOR,
    var volume: Float = 1f,
    var pitch: Float = 1.2f
) {
    val tasks = ArrayList<TaskBase>()
    
    
    init {
        taskProject.stageMap[stageName] = this
    }
    
}