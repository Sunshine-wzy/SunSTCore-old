package io.github.sunshinewzy.sunstcore.modules.task

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
        setSlotItem(2, 3, descriptionItem)
        setSlotItem(5, 3, requireItem)
        
    }
}