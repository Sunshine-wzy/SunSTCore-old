package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.objects.orderWith
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack

class ItemTask(
    taskStage: TaskStage,
    taskName: String,
    order: Int,
    predecessor: TaskBase?,
    symbol: ItemStack,
    reward: Array<ItemStack>,
    var requireItem: ItemStack,
    var descriptionItem: ItemStack,
    openSound: Sound = taskStage.openSound,
    volume: Float = taskStage.volume,
    pitch: Float = taskStage.pitch,
) : TaskBase(taskStage, taskName, order, predecessor, symbol, reward, openSound, volume, pitch, 5) {
    init {
        inventory.setItem(2 orderWith 3, descriptionItem)
        inventory.setItem(5 orderWith 3, requireItem)
        
    }
}