package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class TaskStage(
    val taskProject: TaskProject,
    val stageName: String,
    var order: Int,
    var predecessor: TaskStage?,
    var symbol: ItemStack,
    var edgeItem: ItemStack = SItem(Material.STAINED_GLASS_PANE),
    var openSound: Sound = Sound.ENTITY_HORSE_ARMOR,
    var volume: Float = 1f,
    var pitch: Float = 1.2f,
    var invSize: Int = 5
): TaskInventory {
    private val holder = SProtectInventoryHolder(
        Pair(taskProject.projectName, stageName)
    )
    val taskMap = HashMap<String, TaskBase>()
    var finalTask: TaskBase? = null
    
    
    init {
        taskProject.stageMap[stageName] = this

        subscribeEvent<InventoryClickEvent> {
            if(inventory.holder == this@TaskStage.holder){
                taskMap.values.forEach {
                    val player = view.getSPlayer()
                    if(slot == it.order && player.hasCompleteTask(it.predecessor)){
                        it.openTaskInv(player)
                    }
                }
            }
        }
    }

    override fun openTaskInv(p: Player) {
        p.world.playSound(p.location, openSound, volume, pitch)
        p.openInventory(getTaskInv(p))
    }

    override fun getTaskInv(p: Player): Inventory {
        val inv = Bukkit.createInventory(holder, invSize * 9, stageName)
        inv.createEdge(invSize, edgeItem)
        taskMap.values.forEach { 
            val pre = it.predecessor
            if(pre == null || p.hasCompleteTask(pre)){
                inv.setItem(it.order, it.symbol)
            }
        }
        
        return inv
    }
    
    
    fun hasPredecessor(): Boolean = predecessor != null
}