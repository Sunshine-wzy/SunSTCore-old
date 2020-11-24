package io.github.sunshinewzy.sunstcore.modules.task

import org.bukkit.inventory.ItemStack

abstract class TaskBase(
        val taskName: String,
        val symbol: ItemStack,
        val predecessor: TaskBase?,
        val order: Int,
        val reward: Array<ItemStack>
) {
    
}