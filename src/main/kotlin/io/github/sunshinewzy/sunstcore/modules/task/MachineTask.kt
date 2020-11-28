package io.github.sunshinewzy.sunstcore.modules.task

import org.bukkit.Sound
import org.bukkit.inventory.ItemStack

class MachineTask(
    taskStage: TaskStage,
    taskName: String,
    order: Int,
    predecessor: TaskBase?,
    symbol: ItemStack,
    reward: Array<ItemStack>,
    openSound: Sound = taskStage.openSound,
    volume: Float = taskStage.volume,
    pitch: Float = taskStage.pitch,
    invSize: Int = 5
) : TaskBase(taskStage, taskName, order, predecessor, symbol, reward, openSound, volume, pitch, invSize) {
    
}