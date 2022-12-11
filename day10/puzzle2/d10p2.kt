package day10.puzzle1

import java.io.File

fun main(args : Array<String>) {
    val fileName = "day10/input.txt"

    var x = 1
    var str = 0;
    var step = 0
    File(fileName).forEachLine {
        var instr = it.split(" ")
        step++;
        if (step % 40 == 20) {
            println("At step $step the signal strength is ${x * step}")
            str += (x * step)
        }

        if (instr[0] == "addx") {
            step++;
            if (step % 40 == 20) {
                println("At step $step the signal strength is ${x * step}")
                str += (x * step)
            }
            x += instr[1].toInt()
        }
    }

    println("The value in the X register is $x")
    println("The sum of signal strengths is $str")
}