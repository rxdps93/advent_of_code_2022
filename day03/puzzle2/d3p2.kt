package puzzle2

import java.io.File

fun main(args: Array<String>) {
    val fileName = "day03/input.txt"
    val rucksacks: List<String> = File(fileName).readLines()

    val priorities = ('a'..'z') + ('A' .. 'Z')
    var charCount: IntArray

    var totalPriority = 0;
    rucksacks.chunked(3).forEach {
        charCount = IntArray(52)

        for (c in it[0].toCharArray().distinct() + it[1].toCharArray().distinct() + it[2].toCharArray().distinct()) {
            charCount[priorities.indexOf(c)]++

            if (charCount[priorities.indexOf(c)] == 3) {
                totalPriority += (priorities.indexOf(c) + 1)
                break;
            }
        }
    }

    println(totalPriority)
}