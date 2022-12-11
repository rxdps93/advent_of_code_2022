package day10.puzzle2

import java.io.File

fun main(args : Array<String>) {
    val fileName = "day10/input.txt"

    var x = 1
    var step = 0
    var pixel = 0
    var img = CharArray(240) { '.' }
    File(fileName).forEachLine {
        var instr = it.split(" ")
        step++;
        pixel = (step - 1) % 40
        if (pixel == x || pixel == x + 1 || pixel == x - 1) {
            img[step - 1] = '#'
        }

        if (instr[0] == "addx") {

            step++;
            pixel = (step - 1) % 40
            if (pixel == x || pixel == x + 1 || pixel == x - 1) {
                img[step - 1] = '#'
            }

            x += instr[1].toInt()
        }
    }

    pixel = 0
    for (row in 0..5) {
        for (col in 0..39) {
            print(img[pixel++])
        }
        println()
    }
}