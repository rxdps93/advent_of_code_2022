package day10.puzzle2

import java.io.File

fun main() {

    var x = 1
    var step = 0
    var pixel: Int
    var img = CharArray(240) { '.' }
    File("day10/input.txt").forEachLine {
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