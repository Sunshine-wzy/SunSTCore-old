package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.modules.data.DataManager.getSunSTData
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.setLore
import io.github.sunshinewzy.sunstcore.objects.item.TaskGuideItem
import io.github.sunshinewzy.sunstcore.objects.orderWith
import io.github.sunshinewzy.sunstcore.utils.containsItem
import io.github.sunshinewzy.sunstcore.utils.getSPlayer
import io.github.sunshinewzy.sunstcore.utils.hasCompleteTask
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.Material
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
    vararg val descriptionLore: String,
    openSound: Sound = taskStage.openSound,
    volume: Float = taskStage.volume,
    pitch: Float = taskStage.pitch
) : TaskBase(taskStage, taskName, order, predecessor, symbol, reward, openSound, volume, pitch, 5) {
    private val descriptionItem = SItem(Material.SLIME_BALL, "§a点击以提交任务")
    
    init {
        descriptionItem.setLore(descriptionLore.asList())
        setSlotItem(5, 2, descriptionItem)
        setSlotItem(5, 4, TaskGuideItem.BACK)

        subscribeEvent<InventoryClickEvent> {
            val player = view.getSPlayer()
            if(inventory.holder == this@ItemTask.holder){
                when(slot) {
                    5 orderWith 2 ->
                        if(!player.hasCompleteTask(this@ItemTask)){
                            if(player.inventory.containsItem(requireItems)){
                                
                            }
                            else{
                                
                            }
                        } else againSubmitTask(player)
                    5 orderWith 4 ->
                        taskStage.openTaskInv(player)
                }
            }
        }
    }
}