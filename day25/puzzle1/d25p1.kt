package day25.puzzle1

import java.io.File
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow
import kotlin.text.StringBuilder

enum class Multiplier(val mul: Int, val char: Char) {

    MIN_TWO(-2, '='),
    MIN_ONE(-1, '-'),
    ZERO(0, '0'),
    ONE(1, '1'),
    TWO(2, '2');

    companion object {
        infix fun from(mul: Int): Multiplier? = Multiplier.values().firstOrNull { it.mul == mul }
        infix fun from(char: Char): Multiplier? = Multiplier.values().firstOrNull { it.char == char }
    }
}

fun snafuToDec(snafu: String): Long {
    var dec = 0.0
    snafu.reversed().forEachIndexed { i, c ->
        dec += (Multiplier from c)!!.mul * 5.0.pow(i)
    }
    return dec.toLong()
}

fun decToSnafu(dec: Long): String {
    val out = StringBuilder()
    var num = dec
    do {
        out.append(Multiplier.values()[((num + 2) % 5).toInt()].char)
        num = (num + 2) / 5
    } while (num != 0L)

    return out.toString().reversed()
}

fun main() {
    println(decToSnafu(File("day25/input.txt").readLines().map { snafuToDec(it) }.sum()))
}