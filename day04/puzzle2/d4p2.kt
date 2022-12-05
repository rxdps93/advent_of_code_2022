package day04.puzzle2

import java.io.File

fun main(args : Array<String>) {
    val fileName = "day04/input.txt"
    val lines: List<String> = File(fileName).readLines()

    var overlap = 0
    lines.forEach {
        val elfPair = it.split(",")
        val first = elfPair[0].split("-")
        val second = elfPair[1].split("-")

        var left: List<String>
        var right: List<String>
        if (first[0].toInt() <= second[0].toInt()) {
            left = first
            right = second
        } else {
            left = second
            right = first
        }

        if (left[1].toInt() >= right[0].toInt()) {
            overlap++
        }

    }

    println("Total: $overlap")
}