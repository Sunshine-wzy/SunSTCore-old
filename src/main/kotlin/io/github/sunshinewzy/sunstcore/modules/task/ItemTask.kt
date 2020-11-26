package io.github.sunshinewzy.sunstcore.modules.task

import org.bukkit.inventory.ItemStack

class ItemTask(
        taskStage: TaskStage,
        taskName: String,
        symbol: ItemStack,
        predecessor: TaskBase?,
        order: Int,
        reward: Array<ItemStack>
) : TaskBase(
        taskStage,
        taskName,
        symbol,
        predecessor,
        order,
        reward
) {
    
}