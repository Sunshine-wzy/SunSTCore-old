package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import io.github.sunshinewzy.sunstcore.modules.data.STaskData
import io.github.sunshinewzy.sunstcore.modules.data.sunst.SunSTPlayerData
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.isItemSimilar
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.createEdge
import io.github.sunshinewzy.sunstcore.utils.getSPlayer
import io.github.sunshinewzy.sunstcore.utils.hasCompleteStage
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class TaskProject(
    val projectName: String,
    val openItem: ItemStack = SItem(Material.ENCHANTED_BOOK, "§e$projectName §a向导"),
    val isFirstJoinGive: Boolean = true,
    var title: String,
    var edgeItem: ItemStack = SItem(Material.STAINED_GLASS_PANE),
    var openSound: Sound = Sound.ENTITY_HORSE_ARMOR,
    var volume: Float = 1f,
    var pitch: Float = 1.2f,
    var invSize: Int = 5
) : TaskInventory {
    private val holder = SProtectInventoryHolder(projectName)
    val stageMap = HashMap<String, TaskStage>()
    
    init {
        DataManager.sTaskData[projectName] = STaskData(this)
        
        if(isFirstJoinGive)
            DataManager.firstJoinGiveOpenItems[projectName] = openItem

        subscribeEvent<PlayerInteractEvent> {
            if(item == null) return@subscribeEvent

            if(item.isItemSimilar(openItem)){
                isCancelled = true
                openTaskInv(player)
            }
        }
        
        subscribeEvent<InventoryClickEvent> { 
            if(inventory.holder == this@TaskProject.holder){
                stageMap.values.forEach { 
                    val player = view.getSPlayer()
                    if(slot == it.order && player.hasCompleteStage(it.predecessor)){
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
        val inv = Bukkit.createInventory(holder, invSize * 9, title)
        inv.createEdge(invSize, edgeItem)
        stageMap.values.forEach {
            val pre = it.predecessor
            if(pre == null || p.hasCompleteStage(pre)){
                inv.setItem(it.order, it.symbol)
            }
        }

        return inv
    }
    
    fun getProgress(p: Player): TaskProgress {
        val uid = p.uniqueId.toString()
        
        if(DataManager.sPlayerData.containsKey(uid)){
            val taskProgresses = DataManager.sPlayerData[uid]!!.taskProgress
            
            return if(taskProgresses.containsKey(projectName))
                taskProgresses[projectName]!!
            else{
                val progress = TaskProgress()
                taskProgresses[projectName] = progress
                progress
            }
        }
        else{
            val sPlayerData = SunSTPlayerData(SunSTCore.getPlugin(), uid)
            DataManager.sPlayerData[uid] = sPlayerData

            val progress = TaskProgress()
            sPlayerData.taskProgress[projectName] = progress
            return progress
        }
    }
}