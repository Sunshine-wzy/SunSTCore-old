package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.setName
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.setNameAndLore
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.objects.item.TaskGuideItem
import io.github.sunshinewzy.sunstcore.objects.orderWith
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
    val order: Int,
    val predecessor: TaskStage?,
    val symbol: ItemStack,
    val edgeItem: ItemStack = SItem(Material.STAINED_GLASS_PANE),
    val openSound: Sound = Sound.ENTITY_HORSE_ARMOR,
    val volume: Float = 1f,
    val pitch: Float = 1.2f,
    val invSize: Int = 5
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
                val player = view.getSPlayer()
                
                if(slot == 5 orderWith 5){
                    taskProject.openTaskInv(player)
                    return@subscribeEvent
                }
                
                taskMap.values.forEach {
                    if(slot == it.order && player.hasCompleteTask(it.predecessor)){
                        it.openTaskInv(player)
                    }
                }
            }
        }
    }

    override fun openTaskInv(p: Player, inv: Inventory) {
        TaskProject.lastTaskProject[p.uniqueId] = taskProject
        taskProject.lastTaskInv[p.uniqueId] = this
        
        p.playSound(p.location, openSound, volume, pitch)
        p.openInventory(inv)
    }

    override fun getTaskInv(p: Player): Inventory {
        val inv = Bukkit.createInventory(holder, invSize * 9, stageName)
        inv.createEdge(invSize, edgeItem)
        inv.setItem(5, 5, TaskGuideItem.HOME.item)
        
        taskMap.values.forEach { 
            val pre = it.predecessor
            var name = "§f[§"
            
            if(pre == null || p.hasCompleteTask(pre)){
                val symbol = it.getSymbol()
                
                name += if(p.hasCompleteTask(it)) "a" else "e"
                name += it.taskName + "§f]"
                
                inv.setItem(it.order, symbol.setName(name))
            }
            else{
                val pPre = pre.predecessor
                if(pPre == null || p.hasCompleteTask(pPre)){
                    val symbol = it.getSymbol()
                    name += "c" + it.taskName + "§f]"

                    inv.setItem(it.order, symbol.setNameAndLore(
                        name,
                        "§d此任务尚未解锁，无法查看",
                        "§c您需要解锁此任务的前置任务",
                        "§f[§e${pre.taskName}§f] §c来解锁此任务"
                    ))
                }
            }
        }
        
        return inv
    }
    
    
    fun hasPredecessor(): Boolean = predecessor != null
}