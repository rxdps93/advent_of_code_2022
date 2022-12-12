package day11.puzzle1

import java.io.File

fun main(args: Array<String>) {
    val fileName = "day11/testInput.txt"
    val lines: List<String> = File(fileName).readLines()

    lines.forEach {
        val split = it.split(" ")
    }
}