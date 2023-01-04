package day18.puzzle1

import java.io.File

fun main() {
    var xm = 0
    var ym = 0
    var zm = 0
    val input = File("day18/input.txt").readLines().map { line->
        var split = line.split(",").map { it.toInt() }
        xm = maxOf(xm, split[0])
        ym = maxOf(ym, split[1])
        zm = maxOf(zm, split[2])
        Triple(split[0], split[1], split[2])
    }

    val cubes = Array(xm + 1) { Array(ym + 1) { BooleanArray(zm + 1) } }
    input.forEach { cubes[it.first][it.second][it.third] = true }

    var surfaceArea = 0
    for (x in 0..xm) {
        for (y in 0..ym) {
            for (z in 0..zm) {

                if (!cubes[x][y][z]) {
                    continue
                }

                // right (x + 1)
                surfaceArea += if (x == xm || !cubes[x + 1][y][z]) 1 else 0
                // left (x - 1)
                surfaceArea += if (x == 0 || !cubes[x - 1][y][z]) 1 else 0
                // up (y + 1)
                surfaceArea += if (y == ym || !cubes[x][y + 1][z]) 1 else 0
                // down (y - 1)
                surfaceArea += if (y == 0 || !cubes[x][y - 1][z]) 1 else 0
                // front (z + 1)
                surfaceArea += if (z == zm || !cubes[x][y][z + 1]) 1 else 0
                // back (z - 1)
                surfaceArea += if (z == 0 || !cubes[x][y][z - 1]) 1 else 0
            }
        }
    }

    println("The total surface area is $surfaceArea")
}