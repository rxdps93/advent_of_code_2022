package day04.puzzle1

import java.io.File

fun main() {
    val lines = File("day04/input.txt").readLines()

    var containedPairs = 0
    lines.forEach {
        val elfPair = it.split(",")
        val first = elfPair[0].split("-")
        val second = elfPair[1].split("-")

        if ((first[0].toInt() >= second[0].toInt() && first[1].toInt() <= second[1].toInt()) ||
                (second[0].toInt() >= first[0].toInt() && second[1].toInt() <= first[1].toInt())) {
            containedPairs++;
        }
    }

    println("Total: $containedPairs")
}