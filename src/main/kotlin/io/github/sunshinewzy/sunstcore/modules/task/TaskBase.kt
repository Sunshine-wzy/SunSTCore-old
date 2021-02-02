package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.objects.item.TaskGuideItem
import io.github.sunshinewzy.sunstcore.objects.orderWith
import io.github.sunshinewzy.sunstcore.utils.createEdge
import io.github.sunshinewzy.sunstcore.utils.sendMsg
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

abstract class TaskBase(
    val taskStage: TaskStage,
    val taskName: String,
    var order: Int,
    var predecessor: TaskBase?,
    var symbol: ItemStack,
    var reward: Array<ItemStack>,
    var openSound: Sound = taskStage.openSound,
    var volume: Float = taskStage.volume,
    var pitch: Float = taskStage.pitch,
    var invSize: Int = 5
): ConfigurationSerializable, TaskInventory {
    protected val holder = SProtectInventoryHolder(
        Triple(taskStage.taskProject.projectName, taskStage.stageName, taskName)
    )
    
    private val slotItems = HashMap<Int, ItemStack>()
    
    init {
        taskStage.taskMap[taskName] = this
        
        
    }
    
    
    override fun serialize(): MutableMap<String, Any> {
        val map = HashMap<String, Any>()

        map["taskStage"] = taskStage.stageName
        map["taskName"] = taskName
        map["order"] = order
        map["predecessor"] = if(predecessor != null) predecessor!!.taskName else "null"
        map["symbol"] = symbol
        map["reward"] = reward
        map["openSound"] = openSound
        map["volume"] = volume
        map["pitch"] = pitch
        map["invSize"] = invSize
        map["slotItems"] = slotItems
        
        return map
    }
    
    
    override fun openTaskInv(p: Player) {
        p.world.playSound(p.location, openSound, volume, pitch)
        p.openInventory(getTaskInv(p))
    }
    
    override fun getTaskInv(p: Player): Inventory {
        val inv = Bukkit.createInventory(holder, invSize * 9, taskName)
        inv.createEdge(invSize, taskStage.edgeItem)
        
        slotItems.forEach { (slotOrder, item) -> 
            inv.setItem(slotOrder, item)
        }
        
        return inv
    }
    
    fun setSlotItem(order: Int, item: ItemStack): Boolean {
        if(order >= invSize * 9) return false
        
        slotItems[order] = item
        return true
    }
    
    fun setSlotItem(x: Int, y: Int, item: ItemStack): Boolean = setSlotItem(x orderWith y, item)

    fun setSlotItem(x: Int, y: Int, item: TaskGuideItem): Boolean = setSlotItem(x orderWith y, item.item)

    fun hasPredecessor(): Boolean = predecessor != null

    fun againSubmitTask(player: Player) {
        player.world.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1f, 0.8f)
        player.sendMsg("&c您已完成过任务 $taskName 了，不能重复提交！")
    }
    
    fun requireNotEnough(player: Player) {
        player.world.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1f, 1.2f)
        player.sendMsg("&c你的背包中没有所需物品！")
    }
}