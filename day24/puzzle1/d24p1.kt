package day24.puzzle1

import java.io.File
import kotlin.math.exp

interface Entity {

    var coords: Pair<Int, Int>

    fun move()
    fun getSymbol(): Char
}

data class Blizzard(val type: Char, override var coords: Pair<Int, Int>): Entity {
    override fun move() {
        TODO("Not yet implemented")
    }

    override fun getSymbol(): Char {
        return type
    }
}

data class Expedition(override var coords: Pair<Int, Int>): Entity {

    override fun move() {
        TODO("Not yet implemented")
    }

    override fun getSymbol(): Char {
        return 'E'
    }
}

class Valley(var steps: Int = 0) {

    var rows = 0
    var cols = 0

    var start = Pair(-1, -1)
    var end = Pair(-1, -1)

    val blizzards = mutableListOf<Entity>()
    var expedition = Expedition(start)

    fun step() {
        blizzards.forEach {

        }
    }
    fun print() {
        val out = MutableList(rows) { CharArray(cols) { '.' } }

        out.first().fill('#')
        out.last().fill('#')
        out.forEach { it[0] = '#'; it[it.lastIndex] = '#' }
        out[start.first][start.second] = '.'
        out[end.first][end.second] = '.'

        blizzards.forEach {
            val r = it.coords.first
            val c = it.coords.second
            out[r][c] =
                    if (out[r][c] == '.') {
                it.getSymbol()
            } else {
                val spot = out[r][c].digitToIntOrNull()
                if (spot == null) {
                   '2'
                } else {
                    if (spot < 9) (spot + 1).toChar() else 'X'
                }
            }
        }
        out[expedition.coords.first][expedition.coords.second] = expedition.getSymbol()

        out.forEach { println(String(it)) }
    }

    companion object {
        fun from(old: Valley): Valley {
            val new = Valley(old.steps)
            new.rows = old.rows
            new.cols = old.cols
            new.start = old.start.copy()
            new.end = old.end.copy()
            new.expedition = old.expedition.copy()

            old.blizzards.forEach { new.blizzards.add(it) }
            return new
        }
    }
}

fun main() {
    val input = File("day24/test.txt").readLines()
    val valley = Valley()
    valley.start = Pair(0, input[0].indexOfFirst { it == '.' })
    valley.end = Pair(input.lastIndex, input.last().indexOfFirst { it == '.' })

    valley.rows = input.size
    valley.cols = input[0].length

    valley.expedition = Expedition(valley.start)
    input.forEachIndexed { row, str ->
        str.forEachIndexed { col, c ->
            if (c == '^' || c == 'v' || c == '<' || c == '>') {
                valley.blizzards.add(Blizzard(c, Pair(row, col)))
            }
        }
    }


}

//fun main() {
//    val valley = File("day24/test.txt").readLines().map { line->
//        val row = List(line.length) { Spot() }
//        line.forEachIndexed { i, c -> if (c != '.') { row[i].contains.add(c) } }
//        row
//    }
//
//    val start = Pair(0, valley[0].indexOfFirst { it.contains.isEmpty() })
//    val end = Pair(valley.lastIndex, valley.last().indexOfFirst { it.contains.isEmpty() })
//
//    val queue = ArrayDeque<State>()
//    queue.addLast(State(valley, start, 0))
//    while (queue.isNotEmpty()) {
//        val current = queue.removeFirst()
//    }
//}