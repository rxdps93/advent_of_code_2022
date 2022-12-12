package day12.puzzle1

import java.io.File
import java.util.PriorityQueue

class Square(var x: Int = 0, var y: Int = 0, var height: Int = 0, var parent: Square? = null) {

    fun setCoords(x: Int, y: Int) {
        this.x = x
        this.y = y
    }
    fun getCoords(): Pair<Int, Int> {
        return Pair(x, y)
    }
}

fun main(args : Array<String>) {
    val lines: List<String> = File("day12/testInput.txt").readLines()

    var grid: Array<Array<Square>> = Array(lines.size) { Array(lines[0].length) { Square() } }

    var start: Pair<Int, Int> = Pair(0, 0)
    var end: Pair<Int, Int> = Pair(0, 0)

    for (row in lines.indices) {
        for (col in lines[0].indices) {

            var h = lines[row][col].code - 97
            when (lines[row][col]) {
                'S' -> {
                    start = Pair(row, col)
                    h = 0
                }
                'E' -> {
                    end = Pair(row, col)
                    h = 25
                }
            }

            grid[row][col] = Square(row, col, h)
        }
    }

    var open: ArrayList<Square> = arrayListOf(grid[end.first][end.second])
    var closed: ArrayList<Square> = arrayListOf()

    while (open.isNotEmpty()) {
        val current = open.removeFirst()
        println("(${current.x}, ${current.y}): ${current.height}")
    }



//    for (row in grid) {
//        row.forEach {
//            print("%02d ".format(it.height))
//        }
//        println()
//    }
}