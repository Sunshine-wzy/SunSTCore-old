package io.github.sunshinewzy.sunstcore.modules.machine

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.events.SMachineAddEvent
import io.github.sunshinewzy.sunstcore.events.SMachineRemoveEvent
import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.modules.data.SMachineData
import io.github.sunshinewzy.sunstcore.objects.SLocation
import io.github.sunshinewzy.sunstcore.utils.SunSTTestApi
import io.github.sunshinewzy.sunstcore.utils.removeClone
import io.github.sunshinewzy.sunstcore.utils.sendMsg
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player

/**
 * 多方块机器
 * 
 * @param wrench 构建该多方块机器的扳手
 * @param structure 该多方块机器的结构
 */
abstract class SMachine(
    val name: String,
    val wrench: SMachineWrench,
    val structure: SMachineStructure
) : Initable {
    val machineSLocations = HashSet<String>()
    
    
    init {
        wrench.addMachine(this)
        
    }
    
    
    abstract fun runMachine(event: SMachineRunEvent)
    
    
    fun judgeStructure(loc: Location): Boolean {
        if(structure.judge(loc.removeClone(structure.center)))
            return specialJudge()
        
        return false
    }
    
    open fun specialJudge(): Boolean = true
    
    
    fun addMachine(loc: Location, player: Player) {
        machines[SLocation(loc)] = this
        SunSTCore.pluginManager.callEvent(SMachineAddEvent(this, loc, player))
    }
    
    fun addMachine(sLocation: SLocation) {
        machines[sLocation] = this
    }
    
    fun removeMachine(loc: Location) {
        val sLoc = SLocation(loc)
        
        if(machines.containsKey(sLoc)){
            val machine = machines[sLoc] ?: kotlin.run { 
                machines.remove(sLoc)
                return
            }
            if(machine.name == name) {
                machines.remove(sLoc)
                SunSTCore.pluginManager.callEvent(SMachineRemoveEvent(machine, loc))
            }
        }
    }

    override fun init() {
        SMachineData(this)
    }

    @SunSTTestApi
    fun buildMachine(loc: Location) {
        var theLoc: Location
        structure.structure.forEach { (coord, sBlock) -> 
            theLoc = loc.clone()
            theLoc.add(coord.first.toDouble(), coord.second.toDouble(), coord.third.toDouble())
            
            sBlock.setLocation(theLoc)
        }
    }
    
    
    companion object {
        private val machines = HashMap<SLocation, SMachine>()


        fun Location.hasSMachine(): Boolean {
            val sLoc = SLocation(this)
            
            if(machines.containsKey(sLoc)){
                machines[sLoc] ?: kotlin.run { 
                    machines.remove(sLoc)
                    return false
                }
                return true
            }
            
            return false
        }
        
        fun Location.getSMachine(): SMachine? {
            val sLoc = SLocation(this)

            if(machines.containsKey(sLoc)){
                return machines[sLoc] ?: kotlin.run {
                    machines.remove(sLoc)
                    return null
                }
            }

            return null
        }
        
        fun Location.judgeSMachineStructure(player: Player): Boolean {
            val sLoc = SLocation(this)

            if(machines.containsKey(sLoc)){
                val machine = machines[sLoc] ?: kotlin.run {
                    machines.remove(sLoc)
                    return true
                }
                
                if(!machine.judgeStructure(this)){
                    player.sendMsg(machine.wrench.msgDestroy)
                    machines.remove(sLoc)
                    world.playSound(this, Sound.ENTITY_ITEM_BREAK, 1f, 0.2f)

                    SunSTCore.pluginManager.callEvent(SMachineRemoveEvent(machine, this))
                    return false
                }
            }

            return true
        }
    }
    
}