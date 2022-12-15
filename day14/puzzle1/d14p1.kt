package day14.puzzle1

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {

    var pairs = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
    File("day14/testInput.txt").forEachLine {
        var leftCoord = Pair(-1, -1)
        for (coord in it.filterNot { it.isWhitespace() }.split("->")) {
            var pair = coord.split(",")
            leftCoord = if (leftCoord.first == -1) {
                Pair(pair[0].toInt(), pair[1].toInt())
            } else {
                pairs.add(Pair(leftCoord, Pair(pair[0].toInt(), pair[1].toInt())))
                Pair(pair[0].toInt(), pair[1].toInt())
            }
        }
    }

    // grid goes from x: (xmin -> xmax), y: (0 -> ymax)...indices should be x: (0 -> (xmax-xmin)), y: (0 -> ymax)
    var xmin = pairs.minOf { min(it.first.first, it.second.first) }
    var xmax = pairs.maxOf { max(it.first.first, it.second.first) }
    var ymax = pairs.maxOf { max(it.first.second, it.second.second) }

    var grid: Array<CharArray> = Array(ymax + 1) { CharArray((xmax - xmin) + 1) { '.' } }

//    for (row in grid) {
//        for (col in row) {
//            print(col)
//        }
//        println()
//    }

//    println("x_min=$xmin, x_max=$xmax, y_max=$ymax")
}