package day03.puzzle1

import java.io.File

fun main() {
    val rucksacks = File("day03/input.txt").readLines()

    val priorities = ('a'..'z') + ('A' .. 'Z')
    var commonFlag: BooleanArray

    var totalPriority = 0;
    rucksacks.forEach {
        commonFlag = BooleanArray(52)

        for (i in 0 until (it.length / 2)) {
            commonFlag[priorities.indexOf(it[i])] = true;
        }

        for (i in it.length / 2 until it.length) {
            if (commonFlag[priorities.indexOf(it[i])]) {
                totalPriority += (priorities.indexOf(it[i]) + 1)
                break
            }
        }
    }

    println(totalPriority)
}