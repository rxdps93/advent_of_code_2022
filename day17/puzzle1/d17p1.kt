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
                    return true
                }

                false
            }
            '>' -> {
                if (rows.sumOf { it.row.last() - '.' } == 0) {
                    for (i in rows.indices) {
                        rows[i].row = '.' + rows[i].row.dropLast(1)
                    }
                    return true
                }

                false
            }
            else -> false
        }
    }

    fun getRows(): Array<Row> {
        return rows.reversedArray()
    }

    fun getBottomRow(): Row {
        return rows.last()
    }

    fun print() {
        rows.forEach { println("|${it}|") }
    }
}

private object Chamber {
    private val rows: ArrayDeque<Row> = ArrayDeque()

    private var mergeRows = intArrayOf()

    fun spawnNewRows() {
        if (rows.isNotEmpty()) {
            while (rows.first().isEmpty()) {
                rows.removeFirst()
            }
        }

        this.addRow(Row(), Row(), Row())
    }

    fun mergeRock(rock: Rock) {
        mergeRows = intArrayOf()
        rock.getRows().forEach { rows.addFirst(it) }

        for (i in rows.indices) {
            if (rows[i].row.contains('@')) {
                mergeRows += i
            }
        }
        mergeRows.sortDescending()
    }

    fun mergeDown(): Boolean {

        if (mergeRows[0] == rows.lastIndex) {
            return true
        }

        mergeRows.forEach { r->
            for (i in 0 until 7) {
                if (this.rows[r].row[i] == '@' && this.rows[r + 1].row[i] == '#') {
                    return true
                }
            }
        }

        mergeRows.forEach { r->
            for (i in 0 until 7) {
                if (this.rows[r].row[i] == '@') {
                    var str = this.rows[r].row.toCharArray()
                    str[i] = '.'
                    this.rows[r].row = String(str)

                    str = this.rows[r + 1].row.toCharArray()
                    str[i] = '@'
                    this.rows[r + 1].row = String(str)
                }
            }
        }

        mergeRows = intArrayOf()
        for (i in rows.indices) {
            if (rows[i].row.contains('@')) {
                mergeRows += i
            }
        }
        mergeRows.sortDescending()

        return false
    }

    fun mergeShift(dir: Char): Boolean {

        return when (dir) {
            '<' -> {
                // verify we can shift
                mergeRows.forEach { r->
                    for (i in 0 until 7) {
                        if (i == 0 && this.rows[r].row[i] == '@') {
                            return false
                        }

                        if (i > 0 && this.rows[r].row[i] == '@' && this.rows[r].row[i - 1] == '#') {
                            return false
                        }
                    }
                }

                // execute the shift
                mergeRows.forEach { r->
                    for (i in 0 until 7) {
                        if (this.rows[r].row[i] == '@') {
                            val str = this.rows[r].row.toCharArray()
                            str[i] = '.'
                            str[i - 1] = '@'
                            this.rows[r].row = String(str)
                        }
                    }
                }
                true
            }
            '>' -> {
                // verify we can shift
                mergeRows.forEach { r->
                    for (i in 6 downTo 0) {
                        if (i == 6 && this.rows[r].row[i] == '@') {
                            return false
                        }

                        if (i < 6 && this.rows[r].row[i] == '@' && this.rows[r].row[i + 1] == '#') {
                            return false
                        }
                    }
                }

                // execute the shift
                mergeRows.forEach { r->
                    for (i in 6 downTo 0) {
                        if (this.rows[r].row[i] == '@') {
                            val str = this.rows[r].row.toCharArray()
                            str[i] = '.'
                            str[i + 1] = '@'
                            this.rows[r].row = String(str)
                        }
                    }
                }
                true
            }
            else -> false
        }
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

    fun print() {
        this.getRows().forEach { println("|${it}|") }
        println("+-------+\n")
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
    val input = File("day17/input.txt").readLines()[0].toCharArray()

    var shiftIndex = 0
    for (rockNum in 1..2022) {
        Chamber.spawnNewRows()
        val rock = generateRock((rockNum - 1) % 5)

        var atRest = false
        while (!atRest) {

            rock.shift(input[shiftIndex])
            shiftIndex = if (shiftIndex == input.lastIndex) 0 else shiftIndex + 1

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
                    Chamber.mergeRock(rock)
                    if (Chamber.mergeDown()) {
                        atRest = true
                        Chamber.restRows()
                    }

                    while (!atRest) {
                        Chamber.mergeShift(input[shiftIndex])
                        shiftIndex = if (shiftIndex == input.lastIndex) 0 else shiftIndex + 1
                        if (Chamber.mergeDown()) {
                            atRest = true
                            Chamber.restRows()
                        }
                    }
                }
            }
        }
    }

//    Chamber.print()
    println("The tower of rocks is ${Chamber.getRows().sumOf { if (it.row.contains('#')) 1L else 0L }} units tall.")
}
