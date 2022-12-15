package day01.puzzle2

import java.io.File

fun main() {
    val lines = File("day01/input.txt").readLines()

    var elfCals: ArrayList<Int> = arrayListOf()
    var sum: Int = 0
    lines.forEach {
        var cal = it.toIntOrNull()
        if (cal != null) {
            sum += cal
        } else {
            elfCals.add(sum)
            sum = 0
        }
    }

    elfCals.sortDescending()
    println(elfCals[0] + elfCals[1] + elfCals[2])
}