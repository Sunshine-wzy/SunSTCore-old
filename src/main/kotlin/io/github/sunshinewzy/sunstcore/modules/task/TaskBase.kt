package io.github.sunshinewzy.sunstcore.modules.task

import org.bukkit.inventory.ItemStack

abstract class TaskBase(
        private val taskStage: TaskStage,
        val taskName: String,
        val symbol: ItemStack,
        val predecessor: TaskBase?,
        val order: Int,
        val reward: Array<ItemStack>
) {
    
    init {
        taskStage.tasks.add(this)
        
        
    }
    
}