package day18.puzzle2

import java.io.File

enum class Type {
    LAVA,
    AIR,
    WATER
}

fun surfaceArea(shape: Array<Array<Array<Type>>>, xr: IntRange, yr: IntRange, zr: IntRange): Int {
    var surfaceArea = 0
    for (x in xr) {
        for (y in yr) {
            for (z in zr) {

                if (shape[x][y][z] != Type.LAVA) {
                    continue
                }

                // right (x + 1)
                surfaceArea += if (x == xr.last || shape[x + 1][y][z] != Type.LAVA) 1 else 0
                // left (x - 1)
                surfaceArea += if (x == 0 || shape[x - 1][y][z] != Type.LAVA) 1 else 0
                // up (y + 1)
                surfaceArea += if (y == yr.last || shape[x][y + 1][z] != Type.LAVA) 1 else 0
                // down (y - 1)
                surfaceArea += if (y == 0 || shape[x][y - 1][z] != Type.LAVA) 1 else 0
                // front (z + 1)
                surfaceArea += if (z == zr.last || shape[x][y][z + 1] != Type.LAVA) 1 else 0
                // back (z - 1)
                surfaceArea += if (z == 0 || shape[x][y][z - 1] != Type.LAVA) 1 else 0
            }
        }
    }
    return surfaceArea
}

fun main() {
    var xmax = Int.MIN_VALUE
    var ymax = Int.MIN_VALUE
    var zmax = Int.MIN_VALUE
    var xmin = Int.MAX_VALUE
    var ymin = Int.MAX_VALUE
    var zmin = Int.MAX_VALUE
    val input = File("day18/inputTest2.txt").readLines().map { line->
        var split = line.split(",").map { it.toInt() }

        xmax = maxOf(xmax, split[0])
        xmin = minOf(xmin, split[0])

        ymax = maxOf(ymax, split[1])
        ymin = minOf(ymin, split[1])

        zmax = maxOf(zmax, split[2])
        zmin = minOf(zmin, split[2])
        Triple(split[0], split[1], split[2])
    }

    println("xmin: $xmin, ymin: $ymin, zmin: $zmin")
    println("xmax: $xmax, ymax: $ymax, zmax: $zmax")

//    val cubes = Array(xmax + 1) { Array(ymax + 1) { Array(zmax + 1) { Type.AIR } } }
    val cubes = Array(xmax - xmin + 1) { Array(ymax - ymin + 1) { Array(zmax - zmin + 1) { Type.AIR } } }
    input.forEach { cubes[it.first][it.second][it.third] = Type.LAVA }
//    val airCoords = mutableListOf<Triple<Int, Int, Int>>()
//
//    for (x in 0..xm) {
//        for (y in 0..ym) {
//            for (z in 0..zm) {
//
//                var hit = 0
//
//                // cast ray right (x+)
//                for (rx in x + 1 .. xm) {
//                    if (cubes[rx][y][z] == Type.LAVA) {
//                        hit++
//                        break
//                    }
//                }
//
//                // cast ray left (x-)
//                for (rx in x - 1 downTo 0) {
//                    if (cubes[rx][y][z] == Type.LAVA) {
//                        hit++
//                        break
//                    }
//                }
//
//                // cast ray up (y+)
//                for (ry in y + 1 .. ym) {
//                    if (cubes[x][ry][z] == Type.LAVA) {
//                        hit++
//                        break
//                    }
//                }
//
//                // cast ray down (y-)
//                for (ry in y - 1 downTo 0) {
//                    if (cubes[x][ry][z] == Type.LAVA) {
//                        hit++
//                        break
//                    }
//                }
//
//                // cast ray front (z+)
//                for (rz in z + 1 .. zm) {
//                    if (cubes[x][y][rz] == Type.LAVA) {
//                        hit++
//                        break
//                    }
//                }
//
//                // cast ray back (z-)
//                for (rz in z - 1 downTo 0) {
//                    if (cubes[x][y][rz] == Type.LAVA) {
//                        hit++
//                        break
//                    }
//                }
//
//                if (hit == 6) {
//                    println("Air: ($x, $y, $z), In input: ${cubes[x][y][z]}")
//                    airCoords.add(Triple(x, y, z))
//                }
//            }
//        }
//    }

//    airCoords.forEach { cubes[it.first][it.second][it.third] = true }

    // do a search in cubes from 0,0,0 and mark all the coords in airCoords that are visitable
    val stack = ArrayDeque<Triple<Int, Int, Int>>()
    val airCoords = hashSetOf<Triple<Int, Int, Int>>()
    val visited = Array(xmax + 1) { Array(ymax + 1) { BooleanArray(zmax + 1) } }
    stack.addFirst(Triple(0, 0, 0))

    var temp = 0
    var coord: Triple<Int, Int, Int>
    while (stack.isNotEmpty()) {
        coord = stack.removeFirst()
        var cx = coord.first
        var cy = coord.second
        var cz = coord.third

        println("coord: $coord -> ${cubes[cx][cy][cz]}: $temp")

        if (cubes[cx][cy][cz] == Type.AIR) {
            cubes[cx][cy][cz] = Type.WATER
            visited[cx][cy][cz] = true

            // right (+x)
            if (cx < xmax && !visited[cx + 1][cy][cz] && !airCoords.contains(Triple(cx + 1, cy, cz))) {
                if (cubes[cx + 1][cy][cz] == Type.AIR) {
                    airCoords.add(Triple(cx + 1, cy, cz))
                }
                stack.addFirst(Triple(cx + 1, cy, cz))
            }
            // left (-x)
            if (cx > 0 && !visited[cx - 1][cy][cz] && !airCoords.contains(Triple(cx - 1, cy, cz))) {
                if (cubes[cx - 1][cy][cz] == Type.AIR) {
                    airCoords.add(Triple(cx - 1, cy, cz))
                }
                stack.addFirst(Triple(cx - 1, cy, cz))
            }
            // up (+y)
            if (cy < ymax && !visited[cx][cy + 1][cz] && !airCoords.contains(Triple(cx, cy + 1, cz))) {
                if (cubes[cx][cy + 1][cz] == Type.AIR) {
                    airCoords.add(Triple(cx, cy + 1, cz))
                }
                stack.addFirst(Triple(cx, cy + 1, cz))
            }
            // down (-y)
            if (cy > 0 && !visited[cx][cy - 1][cz] && !airCoords.contains(Triple(cx, cy - 1, cz))) {
                if (cubes[cx][cy - 1][cz] == Type.AIR) {
                    airCoords.add(Triple(cx, cy - 1, cz))
                }
                stack.addFirst(Triple(cx, cy - 1, cz))
            }
            // front (+z)
            if (cz < zmax && !visited[cx][cy][cz + 1] && !airCoords.contains(Triple(cx, cy, cz + 1))) {
                if (cubes[cx][cy][cz + 1] == Type.AIR) {
                    airCoords.add(Triple(cx, cy, cz + 1))
                }
                stack.addFirst(Triple(cx, cy, cz + 1))
            }
            // back (-z)
            if (cz > 0 && !visited[cx][cy][cz - 1] && !airCoords.contains(Triple(cx, cy, cz - 1))) {
                if (cubes[cx][cy][cz - 1] == Type.AIR) {
                    airCoords.add(Triple(cx, cy, cz - 1))
                }
                stack.addFirst(Triple(cx, cy, cz - 1))
            }
        } else if (cubes[cx][cy][cz] == Type.LAVA) {
            temp++
        }
    }

    println("ext surface area is: $temp")

    for (x in 0..xmax) {
        for (y in 0..ymax) {
            for (z in 0..zmax) {
                println("($x, $y, $z) -> ${cubes[x][y][z]}")
            }
        }
    }

    println("sa: ${surfaceArea(cubes, 0..xmax, 0..ymax, 0..zmax)}")

//    for (x in 0..xm) {
//        for (y in 0..ym) {
//            for (z in 0..zm) {
//                if (visited[x][y][z]) {
//                    println("($x, $y, $z) -> ${visited[x][y][z]}")
//                }
//
//                if (airCoords.contains(Triple(x, y, z))) {
//                    println("\tis in airCoords!")
//                }
//                if (airCoords.contains(Triple(x, y, z))) {
//                    println("($x, $y, $z) -> ${visited[x][y][z]}")
//                    if (visited[x][y][z]) {
//                        println("\tvisited and in aircoords, lets add it to cubes")
////                        cubes[x][y][z] = true
//                    }
//                }
//            }
//        }
//    }
//
//    println("The total surface area is ${surfaceArea(cubes, 0..xm, 0..ym, 0..zm)}")

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