package io.github.sunshinewzy.sunstcore.modules.task.tasks

import io.github.sunshinewzy.sunstcore.exceptions.NoRecipeException
import io.github.sunshinewzy.sunstcore.modules.task.TaskBase
import io.github.sunshinewzy.sunstcore.modules.task.TaskInventoryHolder
import io.github.sunshinewzy.sunstcore.modules.task.TaskStage
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.isItemSimilar
import io.github.sunshinewzy.sunstcore.objects.item.TaskGuideItem
import io.github.sunshinewzy.sunstcore.objects.orderWith
import io.github.sunshinewzy.sunstcore.objects.toX
import io.github.sunshinewzy.sunstcore.objects.toY
import io.github.sunshinewzy.sunstcore.utils.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe

class ItemCraftTask(
    taskStage: TaskStage,
    taskName: String,
    order: Int,
    predecessor: TaskBase?,
    symbol: ItemStack,
    reward: Array<ItemStack>,
    val craftItem: ItemStack,
    vararg descriptionLore: String,
) : TaskBase(taskStage, taskName, order, predecessor, symbol, reward, 5, *descriptionLore) {
    private var hasMultiPages = false
    private val pages = ArrayList<Array<ItemStack>>()
    
    
    init {
        setSlotItem(8, 2, submitItem)
        setSlotItem(8, 4, TaskGuideItem.BACK)
        setSlotItem(6, 3, craftItem)
        
        for(i in 2..4)
            setSlotItem(5, i, TaskGuideItem.WORKBENCH)
        
        val recipes = Bukkit.getServer().getRecipesFor(craftItem) ?: throw NoRecipeException(craftItem)
        if(recipes.isEmpty()) throw NoRecipeException(craftItem)
        
        val shapedRecipes = ArrayList<ShapedRecipe>()
        val shapelessRecipes = ArrayList<ShapelessRecipe>()
        recipes.forEach { 
            if(!it.result.isItemSimilar(craftItem)) return@forEach
            
            when(it) {
                is ShapedRecipe ->
                    shapedRecipes.add(it)
                is ShapelessRecipe ->
                    shapelessRecipes.add(it)
            }
        }
        if(shapedRecipes.isEmpty() && shapelessRecipes.isEmpty())
            throw NoRecipeException(craftItem)
        
        if(shapedRecipes.size == 1 && shapelessRecipes.isEmpty()){
            setCraftSlotItem(shapedRecipes.first().getRecipe())
        }
        else if(shapelessRecipes.size == 1 && shapedRecipes.isEmpty()){
            setCraftSlotItem(shapelessRecipes.first().ingredientList)
        }
        else{
            shapedRecipes.forEach { 
                pages.add(it.getRecipe())
            }
            shapelessRecipes.forEach { 
                shapedRecipe ->
                val list = shapedRecipe.ingredientList
                val shapelessArray = Array(9) {
                    if(it in list.indices){
                        list[it]
                    } else ItemStack(Material.AIR)
                }
                pages.add(shapelessArray)
            }
            
            hasMultiPages = true
            if(pages.isNotEmpty()){
                setCraftSlotItem(pages.first())
                setSlotItem(4, 5, TaskGuideItem.PAGE_NEXT)
            }
            holder.value = 1
        }
        

        subscribeEvent<InventoryClickEvent> {
            val player = view.getSPlayer()
            
            if(inventory.holder == this@ItemCraftTask.holder){
                val invHolder = inventory.holder as TaskInventoryHolder
                
                when(slot) {
                    8 orderWith 2 ->
                        if(!player.hasCompleteTask(this@ItemCraftTask)){
                            if(player.inventory.containsItem(craftItem))
                                completeTask(player)
                            else requireNotEnough(player)
                        } else againSubmitTask(player)
                    
                    8 orderWith 4 -> {
                        invHolder.value = 1
                        taskStage.openTaskInv(player)
                    }
                    
                    4 orderWith 5 ->
                        if(hasMultiPages){
                            val value = invHolder.value
                            val size = pages.size
                            
                            if(value in 1..size){
                                if(value < size){
                                    val inv = getTaskInv(player)
                                    if(value == size - 1)
                                        inv.setItem(4, 5, taskStage.edgeItem)
                                    inv.setItem(2, 5, TaskGuideItem.PAGE_PRE.item)
                                    
                                    inv.setCraftSlotItem(pages[value])
                                    invHolder.value = value + 1
                                    openTaskInv(player, inv)
                                }
                            }
                            else invHolder.value = 1
                        }
                    
                    2 orderWith 5 ->
                        if(hasMultiPages){
                            val value = invHolder.value
                            val size = pages.size

                            if(value in 1..size){
                                if(value > 1){
                                    val inv = getTaskInv(player)
                                    if(value > 2)
                                        inv.setItem(2, 5, TaskGuideItem.PAGE_PRE.item)
                                    inv.setItem(4, 5, TaskGuideItem.PAGE_NEXT.item)

                                    inv.setCraftSlotItem(pages[value - 2])
                                    invHolder.value = value - 1
                                    openTaskInv(player, inv)
                                }
                            }
                            else invHolder.value = 1
                        }
                }
            }
        }
    }


    override fun clickInventory(e: InventoryClickEvent) {
        
    }

    override fun submit(player: Player) {
        
    }

    private fun setCraftSlotItem(craftOrder: Int, item: ItemStack) {
        setSlotItem(1 + craftOrder.toX(3), 1 + craftOrder.toY(3), item)
    }
    
    private fun setCraftSlotItem(items: Array<ItemStack>) {
        items.forEachIndexed {
            i, itemStack ->
            setCraftSlotItem(i, itemStack)
        }
    }
    
    private fun setCraftSlotItem(items: List<ItemStack>) {
        items.forEachIndexed {
                i, itemStack ->
            setCraftSlotItem(i, itemStack)
        }
    }

    
    private fun Inventory.setCraftSlotItem(craftOrder: Int, item: ItemStack) {
        setItem(1 + craftOrder.toX(3), 1 + craftOrder.toY(3), item)
    }

    private fun Inventory.setCraftSlotItem(items: Array<ItemStack>) {
        items.forEachIndexed {
                i, itemStack ->
            setCraftSlotItem(i, itemStack)
        }
    }

    private fun Inventory.setCraftSlotItem(items: List<ItemStack>) {
        items.forEachIndexed {
                i, itemStack ->
            setCraftSlotItem(i, itemStack)
        }
    }

    private fun Inventory.clearCraftSlotItem() {
        for(i in 0..8)
            setCraftSlotItem(i, ItemStack(Material.AIR))
    }
}