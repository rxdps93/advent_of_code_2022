package day18.puzzle2

import java.io.File

enum class Type {
    LAVA,
    AIR,
    WATER
}

fun main() {
    var xmax = Int.MIN_VALUE
    var ymax = Int.MIN_VALUE
    var zmax = Int.MIN_VALUE
    var xmin = Int.MAX_VALUE
    var ymin = Int.MAX_VALUE
    var zmin = Int.MAX_VALUE
    val input = File("day18/input.txt").readLines().map { line->
        var split = line.split(",").map { it.toInt() }

        xmax = maxOf(xmax, split[0])
        xmin = minOf(xmin, split[0])

        ymax = maxOf(ymax, split[1])
        ymin = minOf(ymin, split[1])

        zmax = maxOf(zmax, split[2])
        zmin = minOf(zmin, split[2])
        Triple(split[0], split[1], split[2])
    }

    var xbound = xmax - xmin + 3
    var ybound = ymax - ymin + 3
    var zbound = zmax - zmin + 3
    val cubes = Array(xbound) { Array(ybound) { Array(zbound) { Type.AIR } } }
    input.forEach { cubes[it.first - xmin + 1][it.second - ymin + 1][it.third - zmin + 1] = Type.LAVA }

    // dfs
    val stack = ArrayDeque<Triple<Int, Int, Int>>()
    val airCoords = hashSetOf<Triple<Int, Int, Int>>()
    val visited = Array(xbound) { Array(ybound) { BooleanArray(zbound) } }
    stack.addFirst(Triple(0, 0, 0))

    var temp = 0
    var coord: Triple<Int, Int, Int>
    while (stack.isNotEmpty()) {
        coord = stack.removeFirst()
        var cx = coord.first
        var cy = coord.second
        var cz = coord.third

        if (cubes[cx][cy][cz] == Type.AIR) {
            cubes[cx][cy][cz] = Type.WATER
            visited[cx][cy][cz] = true

            arrayOf(
                    Triple(cx, cy, cz + 1), // front
                    Triple(cx, cy, cz - 1), // back
                    Triple(cx, cy + 1, cz), // up
                    Triple(cx, cy - 1, cz), // down
                    Triple(cx + 1, cy, cz), // right
                    Triple(cx - 1, cy, cz), // left
            ).forEach { adj ->

                if (adj.first >= 0 && adj.second >= 0 && adj.third >= 0 &&
                        adj.first < xbound && adj.second < ybound && adj.third < zbound &&
                        !visited[adj.first][adj.second][adj.third] && !airCoords.contains(adj)) {

                    if (cubes[adj.first][adj.second][adj.third] == Type.AIR) {
                        airCoords.add(adj)
                    }
                    stack.addFirst(adj)
                }
            }

        } else if (cubes[cx][cy][cz] == Type.LAVA) {
            temp++
        }
    }

    println(temp)
}