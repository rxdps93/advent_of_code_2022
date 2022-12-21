package day17.puzzle1

import java.io.File

private class Row(var row: String = ".......") {

    fun isEmpty(): Boolean {
        return !row.contains('#')
    }
    override fun toString(): String {
        return this.row
    }
}

private class Rock(private val rows: Array<Row>, val name: String) {

    fun shift(dir: Char): Boolean {
        return when (dir) {
            '<' -> {
                if (rows.sumOf { it.row.first() - '.' } == 0) {
                    for (i in rows.indices) {
                        rows[i].row = rows[i].row.drop(1) + '.'
                    }
                    true
                }

                false
            }
            '>' -> {
                if (rows.sumOf { it.row.last() - '.' } == 0) {
                    for (i in rows.indices) {
                        rows[i].row = '.' + rows[i].row.dropLast(1)
                    }
                    true
                }

                false
            }
            else -> {
                false
            }
        }
    }

    fun getRows(): Array<Row> {
        return rows.reversedArray()
    }

    fun getBottomRow(): Row {
        return rows.last()
    }
}

private object Chamber {
    private val rows: ArrayDeque<Row> = ArrayDeque()

    fun addRow(vararg row: Row) {
        row.forEach { rows.addFirst(it) }
    }

    fun getTopRow(): Row {
        return rows.first()
    }

    fun getRows(): ArrayDeque<Row> {
        return rows
    }

}

fun main() {
    val input = File("day17/testInput.txt").readLines()[0].toCharArray().joinToString("") { "${it}v" }

    val rocks = arrayOf(
            Rock(arrayOf(
                    Row("..####.")), "straight boi"),
            Rock(arrayOf(
                    Row("...#..."),
                    Row("..###.."),
                    Row("...#...")), "plus boi"),
            Rock(arrayOf(
                    Row("....#.."),
                    Row("....#.."),
                    Row("..###..")), "l boi"),
            Rock(arrayOf(
                    Row("..#...."),
                    Row("..#...."),
                    Row("..#...."),
                    Row("..#....")), "tall boi"),
            Rock(arrayOf(
                    Row("..##..."),
                    Row("..##...")), "square boi"))

    Chamber.addRow(Row(), Row(), Row()) // add floor
    Chamber.addRow(*Rock(rocks[2].getRows().reversedArray(), "").getRows())
    var test = Rock(rocks[2].getRows().reversedArray(), "")
    test.shift('<')
    Chamber.addRow(*test.getRows())
    Chamber.getRows().forEach { println(it) } // iterates top to bottom


//    for (rock in 1..2022) {
//        println(rocks[(rock - 1) % 5].name)
//    }
}