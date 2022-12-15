package day06.puzzle1

import java.io.File

fun main() {
    val lines = File("day06/input.txt").readLines()

    var marker = -1
    for (i in 0 until lines[0].length - 3) {
        if (lines[0].substring(i..i + 3).toCharArray().distinct().size == 4) {
            marker = i + 4
            break;
        }
    }
    
    println("First marker after character: $marker")
}