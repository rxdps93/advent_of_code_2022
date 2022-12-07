package day06.puzzle2

import java.io.File

fun main(args : Array<String>) {
    val fileName = "day06/input.txt"
    val lines: List<String> = File(fileName).readLines()

    var marker = -1
    for (i in 0 until lines[0].length - 13) {
        if (lines[0].substring(i..i + 13).toCharArray().distinct().size == 14) {
            marker = i + 14
            break;
        }
    }

    println("First marker after character: $marker")
}