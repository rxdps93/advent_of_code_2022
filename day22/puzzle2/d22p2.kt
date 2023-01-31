package day22.puzzle2

import java.io.File
import kotlin.system.exitProcess

enum class Direction(val vector: Pair<Int, Int>) {
    RIGHT(Pair(0, 1)),
    DOWN(Pair(1, 0)),
    LEFT(Pair(0, -1)),
    UP(Pair(-1, 0))
}

enum class FaceType {
    FRONT,
    BACK,
    TOP,
    BOTTOM,
    LEFT,
    RIGHT
}

object Cube {
    var faces = Array<Face?>(6) { null }

    fun byType(face: FaceType): Face? {
        faces.forEach { if (it != null && it.faceType == face) return it }
        return null
    }
}

class Face(var faceType: FaceType? = null, val size: Int, var rotation: Direction? = null) {

    var tiles = Array(size) { Array(size) { Tile() } }
}

data class Tile(val face: Int = -1, val row: Int = 0, val col: Int = 0,
                val type: Char = '.') {

    var adjacent: Array<Pair<Tile, Direction>?> = Array(4) { null }

    fun getMapCoords(): Pair<Int, Int> {
        return when (this.face) {
            0 -> Pair(row, col + 50)
            1 -> Pair(row, col + 100)
            2 -> Pair(row + 50, col + 50)
            3 -> Pair(row + 100, col)
            4 -> Pair(row + 100, col + 50)
            5 -> Pair(row + 150, col)
            else -> Pair(-1, -1)
        }
    }
}

fun main() {

    val input = File("day22/input.txt").readLines().toMutableList()

    // 1. parse instructions
    val instructions = mutableListOf<String>()
    Regex("([0-9]*)([A-Z]*)").findAll(input.removeLast()).forEach { match ->
        if (match.groupValues[1] != "") {
            instructions.add(match.groupValues[1])

            if (match.groupValues[2] != "") {
                instructions.add(match.groupValues[2])
            }
        }
    }
    input.removeLast()

    // 2. parse input into 6 faces
    val size = input.minOf { it.trim().length }
    var bcf = 0
    var row = 0
    input.forEachIndexed { li, line ->
        var col = 0
        var cf = bcf

        var loop: Boolean
        do {
            line.trim().subSequence(col, col + size).forEachIndexed { index, c ->
                if (Cube.faces[cf] == null) {
                    Cube.faces[cf] = Face(size = size)
                }
                Cube.faces[cf]!!.tiles[row][(col % size) + index] = Tile(cf, row, col + index, c)
            }
            col += size

            loop = if (col + size <= line.trim().length) { cf++; true } else false
        } while (loop)

        if ((li + 1) % size == 0) {
            bcf = cf + 1
        }

        row = (row + 1) % size
    }

    // 3. link faces together for wrapping logic
    // HARD CODING THIS FOR MAIN INPUT JUST TO GET THIS PUZZLE DONE FOR NOW...
    // TODO: REFACTOR IN THE FUTURE TO WORK FOR TEST INPUT AS WELL
    // handle jumps
    for (i in 0 until size) {
        // top row (0, i)
        Cube.byType(FaceType.FRONT)!!.tiles[0][i].adjacent[Direction.UP.ordinal] =
                Pair(Cube.byType(FaceType.TOP)!!.tiles[i][0], Direction.RIGHT)
        Cube.byType(FaceType.BACK)!!.tiles[0][i].adjacent[Direction.UP.ordinal] =
                Pair(Cube.byType(FaceType.BOTTOM)!!.tiles[size - 1][i], Direction.UP)
        Cube.byType(FaceType.LEFT)!!.tiles[0][i].adjacent[Direction.UP.ordinal] =
                Pair(Cube.byType(FaceType.BOTTOM)!!.tiles[i][0], Direction.RIGHT)
        Cube.byType(FaceType.RIGHT)!!.tiles[0][i].adjacent[Direction.UP.ordinal] =
                Pair(Cube.byType(FaceType.TOP)!!.tiles[size - 1][i], Direction.UP)
        Cube.byType(FaceType.TOP)!!.tiles[0][i].adjacent[Direction.UP.ordinal] =
                Pair(Cube.byType(FaceType.LEFT)!!.tiles[size - 1][i], Direction.UP)
        Cube.byType(FaceType.BOTTOM)!!.tiles[0][i].adjacent[Direction.UP.ordinal] =
                Pair(Cube.byType(FaceType.FRONT)!!.tiles[size - 1][i], Direction.UP)

        // bottom row (size-1, i)
        Cube.byType(FaceType.FRONT)!!.tiles[size - 1][i].adjacent[Direction.DOWN.ordinal] =
                Pair(Cube.byType(FaceType.BOTTOM)!!.tiles[0][i], Direction.DOWN)
        Cube.byType(FaceType.BACK)!!.tiles[size - 1][i].adjacent[Direction.DOWN.ordinal] =
                Pair(Cube.byType(FaceType.TOP)!!.tiles[i][size - 1], Direction.LEFT)
        Cube.byType(FaceType.LEFT)!!.tiles[size - 1][i].adjacent[Direction.DOWN.ordinal] =
                Pair(Cube.byType(FaceType.TOP)!!.tiles[0][i], Direction.DOWN)
        Cube.byType(FaceType.RIGHT)!!.tiles[size - 1][i].adjacent[Direction.DOWN.ordinal] =
                Pair(Cube.byType(FaceType.BOTTOM)!!.tiles[i][size - 1], Direction.LEFT)
        Cube.byType(FaceType.TOP)!!.tiles[size - 1][i].adjacent[Direction.DOWN.ordinal] =
                Pair(Cube.byType(FaceType.RIGHT)!!.tiles[0][i], Direction.DOWN)
        Cube.byType(FaceType.BOTTOM)!!.tiles[size - 1][i].adjacent[Direction.DOWN.ordinal] =
                Pair(Cube.byType(FaceType.BACK)!!.tiles[0][i], Direction.DOWN)

        // left side (i, 0)
        Cube.byType(FaceType.FRONT)!!.tiles[i][0].adjacent[Direction.LEFT.ordinal] =
                Pair(Cube.byType(FaceType.LEFT)!!.tiles[(size - 1) - i][0], Direction.RIGHT) // invert i
        Cube.byType(FaceType.BACK)!!.tiles[i][0].adjacent[Direction.LEFT.ordinal] =
                Pair(Cube.byType(FaceType.LEFT)!!.tiles[i][size - 1], Direction.LEFT)
        Cube.byType(FaceType.LEFT)!!.tiles[i][0].adjacent[Direction.LEFT.ordinal] =
                Pair(Cube.byType(FaceType.FRONT)!!.tiles[(size - 1) - i][0], Direction.RIGHT) // invert i
        Cube.byType(FaceType.RIGHT)!!.tiles[i][0].adjacent[Direction.LEFT.ordinal] =
                Pair(Cube.byType(FaceType.FRONT)!!.tiles[i][size - 1], Direction.LEFT)
        Cube.byType(FaceType.TOP)!!.tiles[i][0].adjacent[Direction.LEFT.ordinal] =
                Pair(Cube.byType(FaceType.FRONT)!!.tiles[0][i], Direction.DOWN)
        Cube.byType(FaceType.BOTTOM)!!.tiles[i][0].adjacent[Direction.LEFT.ordinal] =
                Pair(Cube.byType(FaceType.LEFT)!!.tiles[0][i], Direction.DOWN)

        // right side (i, size-1)
        Cube.byType(FaceType.FRONT)!!.tiles[i][size - 1].adjacent[Direction.RIGHT.ordinal] =
                Pair(Cube.byType(FaceType.RIGHT)!!.tiles[i][0], Direction.RIGHT)
        Cube.byType(FaceType.BACK)!!.tiles[i][size - 1].adjacent[Direction.RIGHT.ordinal] =
                Pair(Cube.byType(FaceType.RIGHT)!!.tiles[(size - 1) - i][size - 1], Direction.LEFT) // invert i
        Cube.byType(FaceType.LEFT)!!.tiles[i][size - 1].adjacent[Direction.RIGHT.ordinal] =
                Pair(Cube.byType(FaceType.BACK)!!.tiles[i][0], Direction.RIGHT)
        Cube.byType(FaceType.RIGHT)!!.tiles[i][size - 1].adjacent[Direction.RIGHT.ordinal] =
                Pair(Cube.byType(FaceType.BACK)!!.tiles[(size - 1) - i][size - 1], Direction.LEFT) // invert i
        Cube.byType(FaceType.TOP)!!.tiles[i][size - 1].adjacent[Direction.RIGHT.ordinal] =
                Pair(Cube.byType(FaceType.BACK)!!.tiles[size - 1][i], Direction.UP)
        Cube.byType(FaceType.BOTTOM)!!.tiles[i][size - 1].adjacent[Direction.RIGHT.ordinal] =
                Pair(Cube.byType(FaceType.RIGHT)!!.tiles[size - 1][i], Direction.UP)
    }

    // basic walking linkage
    for (r in 0 until size) {
        for (c in 0 until size) {
            for (face in 0 until 6) {
                for (dir in Direction.values()) {
                    if (Cube.faces[face]!!.tiles[r][c].adjacent[dir.ordinal] == null) {
                        Cube.faces[face]!!.tiles[r][c].adjacent[dir.ordinal] =
                                Pair(Cube.faces[face]!!.tiles[r + dir.vector.first][c + dir.vector.second], dir)
                    }
                }
            }
        }
    }


    // 4. trace the path
    var facing = 0
    var current = Cube.faces[0]!!.tiles[0][0]
    instructions.forEach {instr ->
        val steps = instr.toIntOrNull()
        if (steps == null) {
            // Turn
            facing = (if (instr == "R") facing + 1 else facing - 1).mod(4)
        } else {
            // Walk
            for (step in 1..steps) {
                val next = current.adjacent[facing]
                if (next == null) {
                    println("NULL: Current: (${current.row}, ${current.col}), Face: ${current.face}, Dir: ${Direction.values()[facing]}")
                } else if (next.first.type == '.') {
                    current = next.first
                    facing = next.second.ordinal
                } else if (next.first.type == '#') {
                    break
                }
            }
        }
    }

    println("You ended up on coordinates (${current.row}, ${current.col}) on face ${current.face}. You are facing ${Direction.values()[facing]}")
    println("This translates to coordinates ${current.getMapCoords()}.")
    println("The password is ${(1000 * (current.getMapCoords().first + 1)) + (4 * (current.getMapCoords().second + 1)) + facing}")
}