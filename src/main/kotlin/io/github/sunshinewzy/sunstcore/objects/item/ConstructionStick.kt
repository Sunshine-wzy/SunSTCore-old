package io.github.sunshinewzy.sunstcore.objects.item

import io.github.sunshinewzy.sunstcore.objects.SBlock
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.utils.getFaceLocation
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

/**
 * @param item 作为建筑手杖的物品
 * @param radius 建筑手杖作用半径
 */
class ConstructionStick(item: ItemStack, val radius: Int) : SItem(item) {
    
    init {
        addAction {
            if(clickedBlock != null && hand == EquipmentSlot.HAND){
                val loc = clickedBlock.location
                val listLoc = ArrayList<Location>()

                when(blockFace) {
                    BlockFace.EAST, BlockFace.WEST -> {
                        judge(loc, BlockFace.NORTH, radius, listLoc)
                        judge(loc, BlockFace.SOUTH, radius, listLoc)
                    }
                    
                    BlockFace.NORTH, BlockFace.SOUTH -> {
                        judge(loc, BlockFace.EAST, radius, listLoc)
                        judge(loc, BlockFace.WEST, radius, listLoc)
                    }
                    
                    BlockFace.UP, BlockFace.DOWN -> {
                        
                    } 
                    
                    else -> {
                        
                    }
                    
                }

                player.spawnSelectParticle(listLoc, blockFace)
            }
        }
    }
    
    
    companion object {
        private fun judge(loc: Location, face: BlockFace, distance: Int, listLoc: MutableList<Location>) {
            if(distance <= 0) return
            val nextLoc = loc.getFaceLocation(face)
            
            if(SBlock(loc).isSimilar(nextLoc)){
                listLoc.add(nextLoc)
                judge(nextLoc, face, distance - 1, listLoc)
            }
        }
        
        
        private fun Player.spawnSelectParticle(listLoc: List<Location>, face: BlockFace) {
            listLoc.forEach { 
                val loc = it.getFaceLocation(face)
                spawnParticle(Particle.VILLAGER_HAPPY, loc, 3)
            }
        }
    }
    
}