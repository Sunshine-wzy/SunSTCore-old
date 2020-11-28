package io.github.sunshinewzy.sunstcore.objects

infix fun Int.orderWith(y: Int): Int = (y - 1) * 9 + (this - 1)

fun Int.toCoordinate(): Pair<Int, Int> = (this%9 + 1) to (this/9 + 1)