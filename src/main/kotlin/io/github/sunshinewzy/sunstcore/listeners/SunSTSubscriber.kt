package io.github.sunshinewzy.sunstcore.listeners

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.isItemSimilar
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.objects.item.SunSTItem
import io.github.sunshinewzy.sunstcore.utils.SReflect
import io.github.sunshinewzy.sunstcore.utils.getFaceLocation
import io.github.sunshinewzy.sunstcore.utils.sendMsg
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.EquipmentSlot

object SunSTSubscriber : Initable {
    override fun init() {
        subscribeEvent<InventoryClickEvent> { 
            val holder = inventory.holder
            if(holder is SProtectInventoryHolder<*>){
                isCancelled = true
            }
        }
        
        subscribeEvent<PlayerInteractEvent> { 
            if(action == Action.RIGHT_CLICK_BLOCK){
                if(clickedBlock == null) return@subscribeEvent
                
                if(hand == EquipmentSlot.HAND){
                    if(item != null){
                        if(player.inventory.itemInOffHand.isItemSimilar(SunSTItem.CONSTRUCTIONSTICK_IRON.item)){
                            var loc = clickedBlock.location
                                
                            for(i in 1..3){
                                loc = loc.getFaceLocation(blockFace)
                                if(loc.block.type != Material.AIR) continue
                                
                                val event = BlockPlaceEvent(
                                    loc.block,
                                    loc.block.state,
                                    clickedBlock,
                                    item,
                                    player,
                                    SReflect.canBuild(player.world, player, loc.blockX, loc.blockZ),
                                    EquipmentSlot.HAND
                                )
                                BlockListener.blockPlacedByConstructionStick.add(loc.clone())
                                SunSTCore.pluginManager.callEvent(event)
                            }
                        }
                    }
                }
            }
        }
        
        
    }
}