package day08.puzzle2

import java.io.File

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

fun main() {
    val lines = File("day08/input.txt").readLines()

    val rows = lines.size
    val cols = lines[0].length

    var max = 0;
    for (row in 1 until rows - 1) {
        for (col in 1 until cols - 1) {
            var view = (
                            getView(lines[row].substring(0, col).reversed(), lines[row][col].digitToInt()) * // left
                            getView(lines[row].substring(col + 1, cols), lines[row][col].digitToInt()) * // right
                            getView(getColumn(lines, col).substring(0, row).reversed(), lines[row][col].digitToInt()) * // up
                            getView(getColumn(lines, col).substring(row + 1, rows), lines[row][col].digitToInt())) // down

            if (view > max) {
                max = view
            }
        }
    }

    println("The highest scenic score possible is $max")

}