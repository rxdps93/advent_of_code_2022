package day18.puzzle1

import java.io.File

fun main() {
    val input = File("day18/input.txt").readLines().map { line->
        var split = line.split(",").map { it.toInt() }
        Triple(split[0], split[1], split[2])
    }
}