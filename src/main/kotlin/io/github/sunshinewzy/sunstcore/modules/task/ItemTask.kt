package io.github.sunshinewzy.sunstcore.modules.task

import org.bukkit.inventory.ItemStack

class ItemTask(
        taskName: String,
        symbol: ItemStack,
        predecessor: TaskBase?,
        order: Int,
        reward: Array<ItemStack>
) : TaskBase(
        taskName,
        symbol,
        predecessor,
        order,
        reward
) {
    
}