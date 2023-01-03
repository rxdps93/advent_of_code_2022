package day17.puzzle2

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

    fun getRows(): Array<Row> {
        return rows.reversedArray()
    }
}

private object Chamber {
    private val rows: ArrayDeque<Row> = ArrayDeque()
    private var mergeRows = intArrayOf()
    var skipRows = 0L

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

    fun getRows(): ArrayDeque<Row> {
        return rows
    }

    fun getHeight(): Long {
        return this.rows.sumOf { if (it.row.contains('#')) 1L else 0L }
    }

    fun getHeightWithSkips(): Long {
        return this.getHeight() + this.skipRows
    }

    fun print() {
        this.getRows().forEach { println("|${it}|") }
        println("+-------+\n")
    }
}

private fun generateRock(type: Long): Rock {
    return when (type) {
        0L -> Rock(arrayOf(Row("..@@@@.")))
        1L -> Rock(arrayOf(Row("...@..."), Row("..@@@.."), Row("...@...")))
        2L -> Rock(arrayOf(Row("....@.."), Row("....@.."), Row("..@@@..")))
        3L -> Rock(arrayOf(Row("..@...."), Row("..@...."), Row("..@...."), Row("..@....")))
        4L -> Rock(arrayOf(Row("..@@..."), Row("..@@...")))
        else -> Rock(arrayOf(Row("")))
    }
}

fun main() {
    val input = File("day17/input.txt").readLines()[0].toCharArray()

    var h: Long
    var lh = 0L
    var lr = 0L
    var shiftIndex = 0
    var rockLimit = 1000000000000L
    var cycles = hashSetOf<Triple<Long, Long, Long>>()

    var rockNum = 1L
    while (rockNum <= rockLimit) {
        Chamber.spawnNewRows()
        val rock = generateRock((rockNum - 1) % 5)
        Chamber.mergeRock(rock)

        var atRest = false
        while (!atRest) {
            Chamber.mergeShift(input[shiftIndex])

            shiftIndex = if (shiftIndex == input.lastIndex) {
                h = Chamber.getHeightWithSkips()
                var cycle = Triple(rockNum - lr, (rockNum - 1) % 5, h - lh)
                lh = h
                lr = rockNum

                if (!cycles.add(cycle)) {

                    var iter = (rockLimit - rockNum) / cycle.first
                    rockNum += (iter * cycle.first)
                    Chamber.skipRows += cycle.third * iter
                    lh = Chamber.getHeightWithSkips()
                    lr = rockNum
                }

                0
            } else {
                shiftIndex + 1
            }

            if (Chamber.mergeDown()) {
                atRest = true
                Chamber.restRows()
            }
        }
        rockNum++
    }

    println("The tower of rocks is ${Chamber.getHeightWithSkips()} units tall.")
}
