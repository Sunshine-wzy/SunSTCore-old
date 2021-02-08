package io.github.sunshinewzy.sunstcore.objects.item

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.objects.SBlock
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.utils.*
import org.bukkit.*
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.ArrayList

/**
 * 建筑手杖
 * @param item 作为建筑手杖的物品
 * @param radius 建筑手杖作用半径
 */
class ConstructionStick(item: ItemStack, val radius: Int) : SItem(item) {
    
    init {
        constructionSticks.add(this)
        checkFirstRun()
        
        addAction {
            if(clickedBlock != null && hand == EquipmentSlot.HAND){
                if(player.location.add(0.0, 1.0, 0.0).block.type != Material.AIR) return@addAction

                val block = player.getTargetBlock(null, 5) ?: return@addAction
                if(block.type == Material.AIR) return@addAction

                val lastBlocks = player.getLastTwoTargetBlocks(null, 5) ?: return@addAction
                val face = lastBlocks[1].getFace(lastBlocks[0]) ?: return@addAction
                block.getRelative(face) ?: return@addAction
                val setLoc = hashSetOf(block.location)

                face.transform().forEach {
                    judgeCircle(block.location, it, radius, setLoc)
                }

                player.spawnSelectParticle(setLoc, face)
                setLoc.forEach { 
                    player.tryToPlaceBlock(it, block, block.state.data.toItemStack(1)) {
                        val itemBlock = block.state.data.toItemStack(1)
                        if(player.gameMode == GameMode.SURVIVAL || player.gameMode == GameMode.ADVENTURE){
                            val inv = player.inventory
                            if(inv.containsItem(itemBlock)){
                                inv.removeSItem(itemBlock)
                                return@tryToPlaceBlock true
                            }
                            
                            return@tryToPlaceBlock false
                        }
                        true
                    }
                }
                return@addAction
            }
        }
    }
    
    
    companion object {
        private val constructionSticks = ArrayList<ConstructionStick>()

        /**
         * 建筑手杖检测周期(默认: 10tick)
         */
        var period = 10L
        
        
        private fun checkFirstRun() {
            if(constructionSticks.isNotEmpty() && constructionSticks.size == 1){
                Bukkit.getScheduler().runTaskTimer(SunSTCore.getPlugin(), {
                    if(constructionSticks.isEmpty()) return@runTaskTimer
                    
                    Bukkit.getServer().onlinePlayers.forEach players@{ player ->
                        val handItem = player.inventory.itemInMainHand ?: return@players
                        if(handItem.type == Material.AIR || player.location.add(0.0, 1.0, 0.0).block.type != Material.AIR) return@players
                        
                        constructionSticks.forEach { stick ->
                            if(!handItem.isItemSimilar(stick)) return@forEach
                            
                            val block = player.getTargetBlock(null, 5) ?: return@forEach
                            if(block.type == Material.AIR) return@forEach
                            
                            val lastBlocks = player.getLastTwoTargetBlocks(null, 5) ?: return@forEach
                            val face = lastBlocks[1].getFace(lastBlocks[0]) ?: return@forEach
                            block.getRelative(face) ?: return@forEach
                            val setLoc = hashSetOf(block.location)

                            face.transform().forEach {
                                judgeCircle(block.location, it, stick.radius, setLoc)
                            }

                            player.spawnSelectParticle(setLoc, face)
                            return@players
                        }
                        
                    }
                }, period, period)
            }
        }
        
        
        private fun judgeLine(loc: Location, face: BlockFace, distance: Int, setLoc: MutableSet<Location>) {
            if(distance <= 0) return
            val nextLoc = loc.getFaceLocation(face)
            
            if(SBlock(loc).isSimilar(nextLoc)){
                setLoc.add(nextLoc)
                judgeLine(nextLoc, face, distance - 1, setLoc)
            }
        }
        
        private fun judgeCircle(loc: Location, face: BlockFace, distance: Int, setLoc: MutableSet<Location>) {
            if(distance <= 0) return
            val nextLoc = loc.getFaceLocation(face)

            if(SBlock(loc).isSimilar(nextLoc)){
                setLoc.add(nextLoc)
                
                face.transform().forEach { 
                    judgeLine(loc, it, distance, setLoc)
                }
                
                judgeCircle(nextLoc, face, distance - 1, setLoc)
            }
        }
        
        
        private fun Player.spawnSelectParticle(listLoc: Collection<Location>, face: BlockFace) {
            listLoc.forEach { 
                val loc = it.getFaceLocation(face).add(0.5, 0.5, 0.5)
                spawnParticle(Particle.VILLAGER_HAPPY, loc, 3)
            }
        }
    }
    
}