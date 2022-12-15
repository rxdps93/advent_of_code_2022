package day10.puzzle1

import java.io.File

fun main() {

    var x = 1
    var str = 0;
    var step = 0
    File("day10/input.txt").forEachLine {
        var instr = it.split(" ")
        step++;
        if (step % 40 == 20) {
            str += (x * step)
        }

        if (instr[0] == "addx") {
            step++;
            if (step % 40 == 20) {
                str += (x * step)
            }
            x += instr[1].toInt()
        }
    }

    println("The value in the X register is $x")
    println("The sum of signal strengths is $str")
}