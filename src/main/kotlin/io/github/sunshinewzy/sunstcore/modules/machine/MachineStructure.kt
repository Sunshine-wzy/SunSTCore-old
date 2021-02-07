package io.github.sunshinewzy.sunstcore.modules.machine

import io.github.sunshinewzy.sunstcore.objects.SBlock

sealed class MachineStructure {
    /**
     * 中心对称
     */
    class CentralSymmetry(ingredient: Map<Char, SBlock>, ) : MachineStructure()

    /**
     * 特定朝向
     */
    class Orientation() : MachineStructure()
}