package io.github.sunshinewzy.sunstcore.objects

/**
 * x, y坐标 均从1开始
 * order 从0开始
 */
infix fun Int.orderWith(y: Int): Int = (y - 1) * 9 + (this - 1)


fun Int.toCoordinate(): Pair<Int, Int> = (this%9 + 1) to (this/9 + 1)


fun Int.toX(length: Int): Int = this % length + 1

fun Int.toY(length: Int): Int = this / length + 1