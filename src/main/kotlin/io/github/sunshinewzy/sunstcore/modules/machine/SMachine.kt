package io.github.sunshinewzy.sunstcore.modules.machine

import io.github.sunshinewzy.sunstcore.objects.SBlock

abstract class SMachine(val structure: Array<Array<Array<SBlock>>>) {
    
    abstract fun runMachine()
    
    

}