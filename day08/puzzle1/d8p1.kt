package day08.puzzle1

import java.io.File

fun main() {
    val lines = File("day08/input.txt").readLines()

    val rows = lines.size
    val cols = lines[0].length

    val grid = Array(rows) { BooleanArray(cols) }

    // Mark Outsides
    grid[0] = BooleanArray(cols) { true }
    grid[rows - 1] = BooleanArray(cols) { true }
    for (row in grid) {
        row[0] = true
        row[row.size - 1] = true
    }

    // Visible From Left
    for (row in 1 until rows - 1) {
        var max = lines[row][0].digitToInt()
        for (col in 1 until cols - 1) {
            if (lines[row][col].digitToInt() > max) {
                max = lines[row][col].digitToInt()
                grid[row][col] = true
            }
        }
    }

    // Visible From Right
    for (row in 1 until rows - 1) {
        var max = lines[row][cols - 1].digitToInt()
        for (col in cols - 1 downTo 1) {
            if (lines[row][col].digitToInt() > max) {
                max = lines[row][col].digitToInt()
                grid[row][col] = true
            }
        }
    }

    // Visible From Top
    for (col in 1 until cols - 1) {
        var max = lines[0][col].digitToInt()
        for (row in 1 until rows - 1) {
            if (lines[row][col].digitToInt() > max) {
                max = lines[row][col].digitToInt()
                grid[row][col] = true
            }
        }
    }

    // Visible From Bottom
    for (col in 1 until cols - 1) {
        var max = lines[rows - 1][col].digitToInt()
        for (row in cols - 1 downTo 1) {
            if (lines[row][col].digitToInt() > max) {
                max = lines[row][col].digitToInt()
                grid[row][col] = true
            }
        }
    }

    println("There are ${grid.sumOf { it -> it.count { it } }} trees visible");
}