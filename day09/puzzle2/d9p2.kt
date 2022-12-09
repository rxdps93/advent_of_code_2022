package day09.puzzle2

import day09.util.Knot
import java.io.File

fun main(args : Array<String>) {
    val fileName = "day09/input.txt"
    val lines: List<String> = File(fileName).readLines()

    val rope = Array(10) { Knot() } // head = 0, tail = 9
    for (instr in lines) {
        val dir = instr.split(" ")[0][0];
        val num = instr.split(" ")[1].toInt()
        for (step in 1..num) {
            rope[0].updatePosition(dir)
            for (i in 0 until rope.lastIndex) {
                rope[i + 1].moveTo(rope[i])
            }
        }
    }

    println("There are ${rope.last().getVisited().size} positions the tail visited at least once.")
}