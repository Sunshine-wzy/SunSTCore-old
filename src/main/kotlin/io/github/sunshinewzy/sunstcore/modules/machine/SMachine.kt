package io.github.sunshinewzy.sunstcore.modules.machine

import org.bukkit.Location

abstract class SMachine(val structure: MachineStructure) {
    
    abstract fun runMachine()
    
    
    fun judgeStructure(): Boolean {
        
        
        return false
    }
    
    open fun specialJudge(): Boolean {
        
        return true
    }
    
    fun buildMachine(loc: Location) {
        var theLoc: Location
        structure.structure.forEach { (coord, sBlock) -> 
            theLoc = loc.clone()
            theLoc.add(coord.first.toDouble(), coord.second.toDouble(), coord.third.toDouble())
            
            sBlock.setLocation(theLoc)
        }
    }
    
}