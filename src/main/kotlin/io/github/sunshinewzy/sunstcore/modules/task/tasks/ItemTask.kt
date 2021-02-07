package io.github.sunshinewzy.sunstcore.modules.task.tasks

import io.github.sunshinewzy.sunstcore.modules.task.TaskBase
import io.github.sunshinewzy.sunstcore.modules.task.TaskStage
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.setLore
import io.github.sunshinewzy.sunstcore.objects.item.TaskGuideItem
import io.github.sunshinewzy.sunstcore.objects.orderWith
import io.github.sunshinewzy.sunstcore.utils.*
import org.bukkit.Sound
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class ItemTask(
    taskStage: TaskStage,
    taskName: String,
    order: Int,
    predecessor: TaskBase?,
    symbol: ItemStack,
    reward: Array<ItemStack>,
    var requireItems: Array<ItemStack>,
    vararg descriptionLore: String,
    openSound: Sound = taskStage.openSound,
    volume: Float = taskStage.volume,
    pitch: Float = taskStage.pitch
) : TaskBase(taskStage, taskName, order, predecessor, symbol, reward, openSound, volume, pitch, 5) {
    
    init {
        val item = TaskGuideItem.SUBMIT.item
        item.setLore(descriptionLore.asList())
        setSlotItem(5, 2, item)
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
}