package day08.puzzle2

import java.io.File

fun printGrid(grid: Array<BooleanArray>) {

    for (row in grid) {
        for (tree in row) {
            print(if (tree) "T" else "F")
        }
        println()
    }
    println()
}

fun main(args : Array<String>) {
    val fileName = "day08/input.txt"
    val lines: List<String> = File(fileName).readLines()

    val rows = lines.size
    val cols = lines[0].length

    val grid = Array(rows) { IntArray(cols) }

    // Mark Outsides
    grid[0] = IntArray(cols) { 0 }
    grid[rows - 1] = IntArray(cols) { 0 }
    for (row in grid) {
        row[0] = 0
        row[row.size - 1] = 0
    }

    // Visible From Left
    for (row in 1 until rows - 1) {
        var max = lines[row][0].digitToInt()
        for (col in 1 until cols - 1) {
            if (lines[row][col].digitToInt() > max) {
                max = lines[row][col].digitToInt()
                grid[row][col] = 0
            }
        }
    }

    // Visible From Right
    for (row in 1 until rows - 1) {
        var max = lines[row][cols - 1].digitToInt()
        for (col in cols - 1 downTo 1) {
            if (lines[row][col].digitToInt() > max) {
                max = lines[row][col].digitToInt()
                grid[row][col] = 0
            }
        }
    }

    // Visible From Top
    for (col in 1 until cols - 1) {
        var max = lines[0][col].digitToInt()
        for (row in 1 until rows - 1) {
            if (lines[row][col].digitToInt() > max) {
                max = lines[row][col].digitToInt()
                grid[row][col] = 0
            }
        }
    }

    // Visible From Bottom
    for (col in 1 until cols - 1) {
        var max = lines[rows - 1][col].digitToInt()
        for (row in cols - 1 downTo 1) {
            if (lines[row][col].digitToInt() > max) {
                max = lines[row][col].digitToInt()
                grid[row][col] = 0
            }
        }
    }

//    println("There are ${grid.sumOf { it -> it.count { it } }} trees visible");
}