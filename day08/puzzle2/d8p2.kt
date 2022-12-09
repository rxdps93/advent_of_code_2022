package day08.puzzle2

import java.io.File

fun printGrid(grid: Array<IntArray>) {

    for (row in grid) {
        for (tree in row) {
            print(tree)
        }
        println()
    }
    println()
}

fun viewLeft(view: String, tree: Int): Int {

    println("$tree > ${view.reversed()}")
    var visible = 0
    for (t in view.reversed()) {
        visible++;

        if (t.digitToInt() >= tree) {
            break
        }
    }

    println("left: $visible")
    return visible
}

fun viewRight(view: String, tree: Int): Int {
    println("$tree > $view")
    var visible = 0
    for (t in view) {
        visible++

        if (t.digitToInt() >= tree) {
            break
        }
    }

    println("right: $visible")
    return visible
}

fun viewUp(view: String, tree: Int): Int {
    println("$tree ^ ${view.reversed()}")
    var visible = 0
    for (t in view.reversed()) {
        visible++

        if (t.digitToInt() >= tree) {
            break
        }
    }

    println("up: $visible")
    return visible
}

fun viewDown(view: String, tree: Int): Int {
    println("$tree v $view")
    var visible = 0
    for (t in view) {
        visible++

        if (t.digitToInt() >= tree) {
            break;
        }
    }

    println("down: $visible")
    return visible
}

fun getView(view: String, tree: Int): Int {
    var visible = 0
    for (t in view) {
        visible++

        if (t.digitToInt() >= tree) {
            break;
        }
    }

    return visible;
}

fun getColumn(grid: List<String>, col: Int): String {
    val column = CharArray(grid.size)

    for (i in grid.indices) {
        column[i] = grid[i][col]
    }

    return column.joinToString("")
}

fun main(args : Array<String>) {
    val fileName = "day08/testInput.txt"
    val lines: List<String> = File(fileName).readLines()

    val rows = lines.size
    val cols = lines[0].length

    val grid = Array(rows) { IntArray(cols) { 0 } }

    // Which trees to consider
    for (row in 1 until rows - 1) {
        for (col in 1 until cols - 1) {
            grid[row][col] += (
                            viewLeft(lines[row].substring(0, col), lines[row][col].digitToInt()) *
                            viewRight(lines[row].substring(col + 1, cols), lines[row][col].digitToInt()) *
                            viewUp(getColumn(lines, col).substring(0, row), lines[row][col].digitToInt()) *
                            viewDown(getColumn(lines, col).substring(row + 1, rows), lines[row][col].digitToInt()))
            println()
        }
    }

    printGrid(grid)

}