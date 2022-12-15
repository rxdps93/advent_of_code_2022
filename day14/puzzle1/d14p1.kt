package day14.puzzle1

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun printGrid(grid: Array<CharArray>) {
    for (row in grid) {
        for (col in row) {
            print(col)
        }
        println()
    }
}
fun dropSand(grid: Array<CharArray>, xrange: IntRange, ymax: Int): Int {

    var sand = 0
    var sandCoords = Pair(500 - xrange.first, 0)
    while (true) {

        try {
            if (grid[sandCoords.second + 1][sandCoords.first] == '.') { // move down
                sandCoords = sandCoords.copy(second = sandCoords.second + 1)
            } else if (grid[sandCoords.second + 1][sandCoords.first - 1] == '.') { // move down/left
                sandCoords = sandCoords.copy(second = sandCoords.second + 1, first = sandCoords.first - 1)
            } else if (grid[sandCoords.second + 1][sandCoords.first + 1] == '.') { // move down/right
                sandCoords = sandCoords.copy(second = sandCoords.second + 1, first = sandCoords.first + 1)
            } else { // come to rest
                grid[sandCoords.second][sandCoords.first] = 'o'
                sand++
                sandCoords = Pair(500 - xrange.first, 0)
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            break
        }

        if (sandCoords.second > ymax) {
            break
        }
    }

    return sand
}

fun main() {

    var pairs = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
    File("day14/input.txt").forEachLine {
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
    for (pair in pairs) {
        for (y in min(pair.first.second, pair.second.second).. max(pair.first.second, pair.second.second)) {
            for (x in (min(pair.first.first, pair.second.first) - xmin)..(max(pair.first.first, pair.second.first) - xmin)) {
                grid[y][x] = '#'
            }
        }
    }

    grid[0][500 - xmin] = '+'

    println("A total of ${dropSand(grid, xmin..xmax, ymax)} units of sand can come to rest.")
//    printGrid(grid)
}