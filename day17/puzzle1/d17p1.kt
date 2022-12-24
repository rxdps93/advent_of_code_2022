package day17.puzzle1

import java.io.File

private class Row(var row: String = ".......") {

    fun isEmpty(): Boolean {
        return !row.contains('@') && !row.contains('#')
    }
    override fun toString(): String {
        return this.row
    }
}

private class Rock(private val rows: Array<Row>) {

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

    fun mergeDown(): Boolean {
        return true
    }

    fun mergeShift(dir: Char) {

    }

    fun restRows() {
        for (r in rows.indices) {
            rows[r].row = rows[r].row.replace('@', '#')
        }
    }

    fun addRow(vararg row: Row) {
        var rest = false
        row.forEach {
            rows.addFirst(it)
            if (it.row.contains('@')) {
                rest = true
            }
        }

        if (rest) {
            restRows()
        }
    }

    fun removeTopRow() {
        rows.removeFirst()
    }

    fun getTopRow(): Row {
        return rows.first()
    }

    fun getRows(): ArrayDeque<Row> {
        return rows
    }

}

private fun generateRock(type: Int): Rock {
    return when (type) {
        0 -> Rock(arrayOf(Row("..@@@@.")))
        1 -> Rock(arrayOf(Row("...@..."), Row("..@@@.."), Row("...@...")))
        2 -> Rock(arrayOf(Row("....@.."), Row("....@.."), Row("..@@@..")))
        3 -> Rock(arrayOf(Row("..@...."), Row("..@...."), Row("..@...."), Row("..@....")))
        4 -> Rock(arrayOf(Row("..@@..."), Row("..@@...")))
        else -> Rock(arrayOf(Row("")))
    }
}

fun main() {
    val input = File("day17/testInput.txt").readLines()[0].toCharArray()
//            .joinToString("") { "${it}v" }

//    Chamber.addRow(Row(), Row(), Row()) // add floor
//    Chamber.addRow(*generateRock(2).getRows())
//    var test = generateRock(2)
//    test.shift('<')
//    test.shift('<')
//    test.shift('<')
//    Chamber.addRow(*test.getRows())
//    Chamber.getRows().forEach { println(it) } // iterates top to bottom

    var shiftIndex = 0
    for (rockNum in 1 until 4) {
        Chamber.addRow(Row(), Row(), Row())
        var rock = generateRock((rockNum - 1) % 5)

        var atRest = false
        while (!atRest) {
            rock.shift(input[shiftIndex])

            shiftIndex = if (shiftIndex == input.lastIndex) 0 else shiftIndex + 1
            // rock drop step
            // if no rows below, it comes to rest
            // check if row below is empty, if so remove that row
            // if not check if the rock will rest on it (i.e. collision)
            // if so it comes to rest, if not merge them somehow
            if (Chamber.getRows().isEmpty()) { // if at floor
                Chamber.addRow(*rock.getRows())
                atRest = true
            } else if (Chamber.getTopRow().isEmpty()) { // empty rows to "drop into"
                Chamber.removeTopRow()
            } else {
                for (i in 0 until 7) { // check if it will immediately come to rest
                    if (rock.getBottomRow().row[i] == '@' && Chamber.getTopRow().row[i] == '#') {
                        Chamber.addRow(*rock.getRows())
                        atRest = true
                        break
                    }
                }

                if (!atRest) {
                    // 1. do an initial merge down
                    // 2. check if we are able to rest
                    // 3. if yes, set flag and convert
                    // 4. if no, loop through the following procedure:
                    //    a. merge shift
                    //    b. merge down
                    //    c. check if we are able to rest
                    //    d. if yes, set flag and convert; break loop(s)
                    //    e. if no the we repeat the procedure
                }
            }
        }
    }

//    Chamber.getRows().forEach { println(it) } // iterates top to bottom
//    println("The tower of rocks is ${Chamber.getRows().sumOf { if (it.row.contains('#')) 1L else 0L }} units tall.")
}