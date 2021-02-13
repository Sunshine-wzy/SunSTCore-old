package io.github.sunshinewzy.sunstcore.modules.task.tasks

import io.github.sunshinewzy.sunstcore.modules.task.TaskBase
import io.github.sunshinewzy.sunstcore.modules.task.TaskStage
import io.github.sunshinewzy.sunstcore.objects.item.TaskGuideItem
import io.github.sunshinewzy.sunstcore.objects.orderWith
import io.github.sunshinewzy.sunstcore.utils.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class ItemTask(
    taskStage: TaskStage,
    taskName: String,
    order: Int,
    predecessor: TaskBase?,
    symbol: ItemStack,
    reward: Array<ItemStack>,
    val requireItems: Array<ItemStack>,
    vararg descriptionLore: String,
) : TaskBase(taskStage, taskName, order, predecessor, symbol, reward, 5, *descriptionLore) {
    
    init {
        setSlotItem(5, 2, submitItem)
        setSlotItem(5, 4, TaskGuideItem.BACK)

        subscribeEvent<InventoryClickEvent> {
            val player = view.getSPlayer()
            if(inventory.holder == this@ItemTask.holder){
                when(slot) {
                    5 orderWith 2 ->
                        if(!player.hasCompleteTask(this@ItemTask)){
                            if(player.inventory.containsItem(requireItems))
                                completeTask(player)
                            else requireNotEnough(player)
                        } else againSubmitTask(player)
                    5 orderWith 4 ->
                        taskStage.openTaskInv(player)
                }
            }
        }
    }


    override fun clickInventory(e: InventoryClickEvent) {
        
    }

    override fun submit(player: Player) {
        
    }
}