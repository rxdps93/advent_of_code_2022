package day09.puzzle1

import day09.util.Knot
import java.io.File

fun main(args : Array<String>) {
    val fileName = "day09/input.txt"
    val lines: List<String> = File(fileName).readLines()

    val head = Knot()
    val tail = Knot()
    for (instr in lines) {

        val dir = instr.split(" ")[0][0];
        val num = instr.split(" ")[1].toInt()
        for (step in 1..num) {
            head.updatePosition(dir)
            tail.moveTo(head)
        }
    }

    println("There are ${tail.getVisited().size} positions the tail visited at least once.")
}