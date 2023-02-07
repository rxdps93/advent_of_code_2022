package day25.puzzle1

import java.io.File
import kotlin.math.pow

fun multiplier(sd: Char): Double {
    return when (sd) {
        '2' -> 2.0
        '1' -> 1.0
        '0' -> 0.0
        '-' -> -1.0
        '=' -> -2.0
        else -> 0.0
    }
}

fun main() {
    val values = File("day25/test.txt").readLines().map { snafu ->
        var dec = 0.0
        snafu.reversed().forEachIndexed { i, c ->
            dec += multiplier(c) * 5.0.pow(i)
        }
        dec
    }

    println(values)
}