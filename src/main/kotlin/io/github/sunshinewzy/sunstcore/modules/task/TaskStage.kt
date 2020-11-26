package io.github.sunshinewzy.sunstcore.modules.task

class TaskStage(private val taskProject: TaskProject, var stageName: String) {
    val tasks = ArrayList<TaskBase>()
    
    
    init {
        taskProject.stageMap[stageName] = this
    }
    
}