package day12.puzzle2

import day12.util.Square
import java.io.File
import kotlin.math.min

fun findPath(grid: Array<Array<Square>>, start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
    // neighbors and h-score
    for (row in grid.indices) {
        for (col in grid[0].indices) {

            grid[row][col].distanceFromEnd = grid[row][col].distanceTo(grid[end.first][end.second])
            grid[row][col].distanceFromStart = grid[row][col].distanceTo(grid[start.first][start.second])

            // up
            if (row > 0 && grid[row - 1][col].height <= grid[row][col].height + 1) {
                grid[row][col].addNeighbor(grid[row - 1][col])
            }

            // down
            if (row < grid.lastIndex && grid[row + 1][col].height <= grid[row][col].height + 1) {
                grid[row][col].addNeighbor(grid[row + 1][col])
            }

            // left
            if (col > 0 && grid[row][col - 1].height <= grid[row][col].height + 1) {
                grid[row][col].addNeighbor(grid[row][col - 1])
            }

            // right
            if (col < grid[0].lastIndex && grid[row][col + 1].height <= grid[row][col].height + 1) {
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

    if (visited.contains(grid[end.first][end.second])) {
        var path: ArrayDeque<Square> = ArrayDeque()
        path.addFirst(current)
        while (parent.containsKey(current)) {
            current = parent[current]!!
            path.addFirst(current)
        }

        return path.size - 1
    } else {
        return -1
    }
}

fun main(args : Array<String>) {
    val lines: List<String> = File("day12/input.txt").readLines()

    var grid: Array<Array<Square>> = Array(lines.size) { Array(lines[0].length) { Square() } }

    var startList: ArrayList<Pair<Int, Int>> = arrayListOf()
    var end: Pair<Int, Int> = Pair(0, 0)

    for (row in lines.indices) {
        for (col in lines[0].indices) {

            var h = lines[row][col].code - 97
            when (lines[row][col]) {
                'a' -> {
                    startList.add(Pair(row, col))
                }
                'S' -> {
                    startList.add(Pair(row, col))
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

    var min = Int.MAX_VALUE
    for (start in startList) {
        val dist = findPath(grid, start, end)
        if (dist != -1) {
            min = min(dist, min)
        }
    }

    println("The shortest possible path from an 'a' square to the end is $min steps.")
}