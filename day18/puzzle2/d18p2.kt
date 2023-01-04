package day18.puzzle2

import java.io.File

fun surfaceArea(shape: Array<Array<BooleanArray>>, xr: IntRange, yr: IntRange, zr: IntRange): Int {
    var surfaceArea = 0
    for (x in xr) {
        for (y in yr) {
            for (z in zr) {

                if (!shape[x][y][z]) {
                    continue
                }

                // right (x + 1)
                surfaceArea += if (x == xr.last || !shape[x + 1][y][z]) 1 else 0
                // left (x - 1)
                surfaceArea += if (x == 0 || !shape[x - 1][y][z]) 1 else 0
                // up (y + 1)
                surfaceArea += if (y == yr.last || !shape[x][y + 1][z]) 1 else 0
                // down (y - 1)
                surfaceArea += if (y == 0 || !shape[x][y - 1][z]) 1 else 0
                // front (z + 1)
                surfaceArea += if (z == zr.last || !shape[x][y][z + 1]) 1 else 0
                // back (z - 1)
                surfaceArea += if (z == 0 || !shape[x][y][z - 1]) 1 else 0
            }
        }
    }
    return surfaceArea
}

fun main() {
    var xm = 0
    var ym = 0
    var zm = 0
    val input = File("day18/test.txt").readLines().map { line->
        var split = line.split(",").map { it.toInt() }
        xm = maxOf(xm, split[0])
        ym = maxOf(ym, split[1])
        zm = maxOf(zm, split[2])
        Triple(split[0], split[1], split[2])
    }

    val cubes = Array(xm + 1) { Array(ym + 1) { BooleanArray(zm + 1) } }
    input.forEach { cubes[it.first][it.second][it.third] = true }
    val airCoords = mutableListOf<Triple<Int, Int, Int>>()

    for (x in 0..xm) {
        for (y in 0..ym) {
            for (z in 0..zm) {

                var hit = 0

                // cast ray right (x+)
                for (rx in x + 1 .. xm) {
                    if (cubes[rx][y][z]) {
                        hit++
                        break
                    }
                }

                // cast ray left (x-)
                for (rx in x - 1 downTo 0) {
                    if (cubes[rx][y][z]) {
                        hit++
                        break
                    }
                }

                // cast ray up (y+)
                for (ry in y + 1 .. ym) {
                    if (cubes[x][ry][z]) {
                        hit++
                        break
                    }
                }

                // cast ray down (y-)
                for (ry in y - 1 downTo 0) {
                    if (cubes[x][ry][z]) {
                        hit++
                        break
                    }
                }

                // cast ray front (z+)
                for (rz in z + 1 .. zm) {
                    if (cubes[x][y][rz]) {
                        hit++
                        break
                    }
                }

                // cast ray back (z-)
                for (rz in z - 1 downTo 0) {
                    if (cubes[x][y][rz]) {
                        hit++
                        break
                    }
                }

                if (hit == 6) {
                    println("Air: ($x, $y, $z), In input: ${cubes[x][y][z]}")
                    airCoords.add(Triple(x, y, z))
                }
            }
        }
    }

//    airCoords.forEach { cubes[it.first][it.second][it.third] = true }

    // do a search in cubes from 0,0,0 and mark all the coords in airCoords that are visitable
    val stack = ArrayDeque<Triple<Int, Int, Int>>()
    val visited = hashMapOf<Triple<Int, Int, Int>, Boolean>()
    airCoords.forEach { visited[it] = false }

    println("The total surface area is ${surfaceArea(cubes, 0..xm, 0..ym, 0..zm)}")

//    var initial = surfaceArea(cubes, 0..xm, 0..ym, 0..zm)
//
//    xm = 0
//    ym = 0
//    zm = 0
//    airCoords.forEach { xm = maxOf(xm, it.first); ym = maxOf(ym, it.second); zm = maxOf(zm, it.third) }
//    val airGrid = Array(xm + 1) { Array(ym + 1) { BooleanArray(zm + 1)} }
//    airCoords.forEach { airGrid[it.first][it.second][it.third] = true }
//
//    println("The total surface area is ${initial - surfaceArea(airGrid, 0..xm, 0..ym, 0..zm)}")
}