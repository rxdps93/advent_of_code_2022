package day22.puzzle1

import java.io.File

data class Tile(val row: Int, val col: Int, val type: Char)

fun main() {
    val tiles = hashMapOf<Pair<Int, Int>, Tile>()
    var row = 1
    val instructions = mutableListOf<String>()
    var currRow = 1
    var currCol = -1
    val rowBounds = hashMapOf<Int, Pair<Int, Int>>()
    val colBounds = hashMapOf<Int, Pair<Int, Int>>()

    File("day22/input.txt").forEachLine { line ->
        if (!line.contains('.') && !line.contains('#')) {
            Regex("([0-9]*)([A-Z]*)").findAll(line).forEach { match->

                if (match.groupValues[1] != "") {
                    instructions.add(match.groupValues[1])

                    if (match.groupValues[2] != "") {
                        instructions.add(match.groupValues[2])
                    }
                }
            }
        } else {
            line.forEachIndexed { index, c ->

                if (c != ' ') {
                    tiles[Pair(row, index + 1)] = Tile(row, index + 1, c)

                    if (c == '.' && currCol == -1) {
                        currCol = index + 1
                    }

                    if (colBounds[index + 1] == null) {
                        colBounds[index + 1] = Pair(row, -1)
                    } else {
                        colBounds[index + 1] = Pair(colBounds[index + 1]!!.first, row)
                    }
                }
            }
            rowBounds[row] = Pair(line.indexOfFirst { c -> c != ' ' } + 1, line.indexOfLast { c -> c != ' ' } + 1)
            row++
        }
    }

    var facing = 0
    var colStep = 1
    var rowStep = 0
    instructions.forEach { instr ->
        if (instr.toIntOrNull() == null) {
            // Turn
            facing = (if (instr == "R") facing + 1 else facing - 1).mod(4)
            when (facing) {
                0 -> { colStep = 1; rowStep = 0 } // right
                1 -> { colStep = 0; rowStep = 1 } // down
                2 -> { colStep = -1; rowStep = 0 } // left
                3 -> { colStep = 0; rowStep = -1 } // up
            }
        } else {
            // Walk
            for (step in 1..instr.toInt()) {
                var next = tiles[Pair(currRow + rowStep, currCol + colStep)]
                if (next == null) {
                    // wrap around
                    next = when (facing) {
                        0 -> { tiles[Pair(currRow, rowBounds[currRow]!!.first)] }
                        1 -> { tiles[Pair(colBounds[currCol]!!.first, currCol)] }
                        2 -> { tiles[Pair(currRow, rowBounds[currRow]!!.second)] }
                        3 -> { tiles[Pair(colBounds[currCol]!!.second, currCol)] }
                        else -> null
                    }

                    if (next == null) {
                        println("\toops: $facing: ($currRow, $currCol)")
                    } else {
                        if (next.type == '.') {
                            currRow = next.row
                            currCol = next.col
                        } else {
                            break
                        }
                    }
                } else if (next.type == '.') {
                    currRow = next.row
                    currCol = next.col
                } else {
                    // hit a wall
                    break
                }
            }

        }
    }

    println("You ended up on coordinates ($currRow, $currCol); the password is ${(1000 * currRow) + (4 * currCol) + facing}")
}