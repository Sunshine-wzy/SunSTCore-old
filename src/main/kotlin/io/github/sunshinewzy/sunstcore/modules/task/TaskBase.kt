package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.objects.SInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.createEdge
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class TaskBase(
    val taskStage: TaskStage,
    var taskName: String,
    var order: Int,
    var predecessor: TaskBase?,
    var symbol: ItemStack,
    var reward: Array<ItemStack>,
    var openSound: Sound = taskStage.openSound,
    var volume: Float = taskStage.volume,
    var pitch: Float = taskStage.pitch,
    val invSize: Int = 5
) {
    private val holder = SInventoryHolder(
        Triple(taskStage.taskProject.projectName, taskStage.stageName, taskName)
    )
    val inventory = Bukkit.createInventory(holder, invSize * 9, taskName)
    
    init {
        taskStage.tasks.add(this)
        
        inventory.createEdge(invSize, taskStage.edgeItem)
    }
    
    
    fun openInventory(p: Player) {
        p.world.playSound(p.location, openSound, volume, pitch)
        p.openInventory(inventory)
    }
    
}