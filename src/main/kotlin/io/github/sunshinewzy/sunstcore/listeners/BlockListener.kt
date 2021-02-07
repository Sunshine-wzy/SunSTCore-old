package io.github.sunshinewzy.sunstcore.listeners

import io.github.sunshinewzy.sunstcore.objects.SBlock
import io.github.sunshinewzy.sunstcore.utils.SReflect.damage
import io.github.sunshinewzy.sunstcore.utils.sendMsg
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

object BlockListener : Listener {
    
    val blockPlacedByConstructionStick = ArrayList<Location>()
    
    
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockPlace(e: BlockPlaceEvent) {
        if(blockPlacedByConstructionStick.isNotEmpty()){
            val block = e.blockPlaced
            val loc = block.location
            val p = e.player
            val item = e.itemInHand
            val offHandItem = p.inventory.itemInOffHand
            
            if(blockPlacedByConstructionStick.contains(loc)){
                if(!e.isCancelled && item != null && item.type != Material.AIR && item.amount > 0){
                    SBlock(item).setLocation(loc)
//                    p.sendMsg("${item.type} §a放置成功！")
                    
                    if(p.gameMode == GameMode.SURVIVAL || p.gameMode == GameMode.ADVENTURE){
                        item.amount = item.amount - 1
                        p.inventory.itemInOffHand = offHandItem.damage(1, p)
                    }
                }
//                else{
//                    p.sendMsg("放置失败！")
//                }
                
                blockPlacedByConstructionStick.remove(loc)
            }
        }
    }
    
}