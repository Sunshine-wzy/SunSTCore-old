package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder

class TaskInventoryHolder(task: TaskBase, var value: Int = 0) : SProtectInventoryHolder<Triple<String, String, String>>(
    Triple(
        task.taskStage.taskProject.projectName,
        task.taskStage.stageName,
        task.taskName,
    )
)