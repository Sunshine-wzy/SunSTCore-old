package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import io.github.sunshinewzy.sunstcore.modules.data.STaskData

class TaskProject(val projectName: String) {
    val stageMap = HashMap<String, TaskStage>()
    
    init {
        DataManager.sTaskData[projectName] = STaskData(this)
    }
}