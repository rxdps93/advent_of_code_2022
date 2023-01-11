package day20.puzzle1

import java.io.File

data class Number(val initPos: Int, val value: Int)
fun main() {
    val numbers = mutableListOf<Number>()
    File("day20/input.txt").readLines().forEachIndexed { index, s ->
        numbers.add(Number(index, s.toInt()))
    }

    for (init in numbers.indices) {
        val pos = numbers.indexOfFirst { it.initPos == init }
        val temp = numbers.removeAt(pos)
        val newPos = (pos + temp.value).mod(numbers.size)

        numbers.add(newPos, temp)
    }

    val zeroAt = numbers.indexOfFirst { it.value == 0 }
    var coords = Triple(
            numbers[(1000 + zeroAt) % numbers.size].value,
            numbers[(2000 + zeroAt) % numbers.size].value,
            numbers[(3000 + zeroAt) % numbers.size].value
    )
    println("Grove Coordinates: $coords. The sum is ${coords.toList().sum()}")
}