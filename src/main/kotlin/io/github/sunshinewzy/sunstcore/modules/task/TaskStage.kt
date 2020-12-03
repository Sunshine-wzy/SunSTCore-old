package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.objects.SInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.createEdge
import io.github.sunshinewzy.sunstcore.utils.hasCompleteTask
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
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
    
    init {
        taskProject.stageMap[stageName] = this
        
    }
    
    
    private fun openInventory(p: Player) {
        val inv = Bukkit.createInventory(holder, invSize, stageName)
        inv.createEdge(invSize, edgeItem)
        tasks.forEach { 
            val pre = it.predecessor
            if(pre == null || p.hasCompleteTask(pre)){
                inv.setItem(it.order, it.symbol)
            }
            
        }
    }
    
}