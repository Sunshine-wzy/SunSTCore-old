package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.objects.SInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.createEdge
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class TaskBase(
        val taskStage: TaskStage,
        var taskName: String,
        var symbol: ItemStack,
        var predecessor: TaskBase?,
        var order: Int,
        var reward: Array<ItemStack>,
        val invSize: Int = 5
) {
    private val holder = SInventoryHolder(
        Triple(taskStage.taskProject.projectName, taskStage.stageName, taskName)
    )
    private val inventory = Bukkit.createInventory(holder, invSize * 9, taskName)
    
    init {
        taskStage.tasks.add(this)
        
        inventory.createEdge(taskStage.edgeItem)
    }
    
    
    fun openInventory(p: Player) {
        p.world.playSound(p.location, taskStage.openSound, taskStage.volume, taskStage.pitch)
        p.openInventory(inventory)
    }
    
}