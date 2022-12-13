package day12.puzzle1

import java.io.File
import kotlin.math.abs

class Square(var x: Int = 0, var y: Int = 0, var height: Int = 0): Comparable<Square> {

    private val validNeighbors: MutableSet<Square> = mutableSetOf()
    var distanceFromStart: Int = 0
    var distanceFromEnd: Int = 0

    fun getHeuristic(): Int {
        return distanceFromEnd + distanceFromStart
    }

    fun getNeighbors(): MutableSet<Square> {
        return this.validNeighbors
    }

    fun addNeighbor(neighbor: Square) {
        this.validNeighbors.add(neighbor)
    }

    fun getCoords(): Pair<Int, Int> {
        return Pair(x, y)
    }

    fun setCoords(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun distanceTo(node: Square): Int {
        var h = abs(node.x - this.x)
        var v = abs(this.y - node.y)
        return h + v
    }

    override fun compareTo(other: Square): Int {
        return this.getHeuristic() - other.getHeuristic()
    }

    override fun toString(): String {
        return "${(height + 97).toChar()}($height): ($x, $y)"
    }

}

fun main(args : Array<String>) {
    val lines: List<String> = File("day12/input.txt").readLines()

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

    // neighbors and h-score
    for (row in lines.indices) {
        for (col in lines[0].indices) {

            grid[row][col].distanceFromEnd = grid[row][col].distanceTo(grid[end.first][end.second])
            grid[row][col].distanceFromStart = grid[row][col].distanceTo(grid[start.first][start.second])

            // up
            if (row > 0 && grid[row - 1][col].height <= grid[row][col].height + 1) {
                grid[row][col].addNeighbor(grid[row - 1][col])
            }

            // down
            if (row < lines.lastIndex && grid[row + 1][col].height <= grid[row][col].height + 1) {
                grid[row][col].addNeighbor(grid[row + 1][col])
            }

            // left
            if (col > 0 && grid[row][col - 1].height <= grid[row][col].height + 1) {
                grid[row][col].addNeighbor(grid[row][col - 1])
            }

            // right
            if (col < lines[0].lastIndex && grid[row][col + 1].height <= grid[row][col].height + 1) {
                grid[row][col].addNeighbor(grid[row][col + 1])
            }
        }
    }


    var queue: MutableSet<Square> = mutableSetOf(grid[start.first][start.second])
    var visited: MutableSet<Square> = mutableSetOf()

    var parent: HashMap<Square, Square> = HashMap()

    var current: Square = queue.first()
    while (queue.isNotEmpty()) {
        current = queue.minByOrNull { it.getHeuristic() }!!
        queue.remove(current)
        visited.add(current)

        if (current.getCoords() == end) {
            break // found the path
        }

        for (neighbor in current.getNeighbors()) {

            if (visited.contains(neighbor)) {
                continue
            }

            var dist = current.distanceFromStart + current.distanceTo(neighbor)
            if (dist < neighbor.distanceFromStart || !queue.contains(neighbor)) {
                parent[neighbor] = current
                neighbor.distanceFromStart = dist

                if (!queue.contains(neighbor)) {
                    queue.add(neighbor)
                }
            }
        }

    }

    var path: ArrayDeque<Square> = ArrayDeque()
    path.addFirst(current)
    while (parent.containsKey(current)) {
        current = parent[current]!!
        path.addFirst(current)
    }

    println("The path includes ${path.size} squares, which means the goal can be reached in ${path.size - 1} steps.")
}