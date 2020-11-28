package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.objects.SInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.createEdge
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack

class TaskStage(
    val taskProject: TaskProject,
    var stageName: String,
    var edgeItem: ItemStack,
    var openSound: Sound = Sound.ENTITY_HORSE_ARMOR,
    var volume: Float = 1f,
    var pitch: Float = 1.2f,
    val invSize: Int = 5
) {
    val tasks = ArrayList<TaskBase>()
    private val holder = SInventoryHolder(
        Pair(taskProject.projectName, stageName)
    )
    private val inventory = Bukkit.createInventory(holder, invSize, stageName)
    
    
    init {
        taskProject.stageMap[stageName] = this
        
        updateInventory()
    }
    
    
    private fun updateInventory() {
        inventory.clear()
        inventory.createEdge(invSize, edgeItem)
        tasks.forEach { 
            inventory.setItem(it.order, it.symbol)
        }
    }
    
}