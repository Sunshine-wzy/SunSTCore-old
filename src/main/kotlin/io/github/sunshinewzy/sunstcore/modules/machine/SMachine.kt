package io.github.sunshinewzy.sunstcore.modules.machine

abstract class SMachine(val structure: MachineStructure) {
    
    abstract fun runMachine()
    
    
    fun judgeStructure(): Boolean {
        
        
        return false
    }
    
    open fun specialJudge(): Boolean {
        
        return true
    }
    
}