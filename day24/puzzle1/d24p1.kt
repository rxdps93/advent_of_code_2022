package day24.puzzle1

import java.io.File
import kotlin.math.min

data class Blizzard(val id: Int, val type: Char, var coords: Pair<Int, Int>) {
    fun move(size: Pair<Int, Int>): Boolean {
        coords = when (type) {
            '^' -> {
                if (coords.first - 1 == 0) {
                    Pair(size.first - 2, coords.second)
                } else {
                    Pair(coords.first - 1, coords.second)
                }
            }
            'v' -> {
                if (coords.first + 1 == size.first - 1) {
                    Pair(1, coords.second)
                } else {
                    Pair(coords.first + 1, coords.second)
                }
            }
            '<' -> {
                if (coords.second - 1 == 0) {
                    Pair(coords.first, size.second - 2)
                } else {
                    Pair(coords.first, coords.second - 1)
                }
            }
            '>' -> {
                if (coords.second + 1 == size.second - 1) {
                    Pair(coords.first, 1)
                } else {
                    Pair(coords.first, coords.second + 1)
                }
            }
            else -> coords
        }

        return true
    }

    fun getSymbol(): Char {
        return type
    }
}

data class Expedition(var coords: Pair<Int, Int>) {

    fun move(dir: Char, size: Pair<Int, Int>, end: Pair<Int, Int>): Boolean {
        return when (dir) {
            '^' -> {
                if (coords.first <= 1) {
                    false
                } else {
                    coords = Pair(coords.first - 1, coords.second)
                    true
                }
            }
            'v' -> {
                if (coords.first >= size.first - 2 && coords.second != end.second) {
                    false
                } else {
                    coords = Pair(coords.first + 1, coords.second)
                    true
                }
            }
            '<' -> {
                if (coords.second <= 1) {
                    false
                } else {
                    coords = Pair(coords.first, coords.second - 1)
                    true
                }
            }
            '>' -> {
                if (coords.second >= size.second - 2 || coords.first == 0) {
                    false
                } else {
                    coords = Pair(coords.first, coords.second + 1)
                    true
                }
            }
            else -> {
                false
            }
        }
    }

    fun getSymbol(): Char {
        return 'E'
    }
}

class Valley(var steps: Int = 0) {

    var rows = 0
    var cols = 0

    var start = Pair(-1, -1)
    var end = Pair(-1, -1)

    val blizzards = mutableListOf<Blizzard>()
    var expedition = Expedition(start)

    fun hasBlizzardAt(coords: Pair<Int, Int>): Boolean {
        this.blizzards.forEach { if (it.coords == coords) { return true } }
        return false
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
                    if (spot < 9) (spot + 1).digitToChar() else 'X'
                }
            }
        }
        out[expedition.coords.first][expedition.coords.second] = expedition.getSymbol()

        out.forEach { println(String(it)) }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Valley) {
            var eq = (this.steps == other.steps) && (this.expedition.coords == other.expedition.coords)

            val tbm = this.blizzards.associateBy { it.id }
            val obm = other.blizzards.associateBy { it.id }

            eq = eq && (tbm.size == obm.size) && (tbm.keys == obm.keys)

            if (eq) {
                for (i in tbm.keys) {
                    eq = eq && (tbm[i]!!.coords == obm[i]!!.coords) && (tbm[i]!!.getSymbol() == tbm[i]!!.getSymbol())
                }
            }

            eq
        } else {
            false
        }
    }

    companion object {

        fun nextPossibleStates(current: Valley): List<Valley> {
            val next = from(current)
            next.steps++
            val moves = mutableListOf<Valley>()

            // move blizzards
            next.blizzards.forEach { blizzard ->
                blizzard.move(size = Pair(next.rows, next.cols))
            }

            // handle expedition
            val expCoord = next.expedition.coords
            var expNext: Valley
            // wait
            if (!next.hasBlizzardAt(expCoord)) {
//                println("\twait")
                moves.add(from(next))
            }

            // up
            if (!next.hasBlizzardAt(expCoord.copy(first = expCoord.first - 1))) {
//                println("\tno blizzard up")
                expNext = from(next)
                if (expNext.expedition.move('^', Pair(expNext.rows, expNext.cols), expNext.end)) {
//                    println("\t\tmove up")
                    moves.add(expNext)
                }
            }

            // down
            if (!next.hasBlizzardAt(expCoord.copy(first = expCoord.first + 1))) {
//                println("\tno blizzard down")
                expNext = from(next)
                if (expNext.expedition.move('v', Pair(expNext.rows, expNext.cols), expNext.end)) {
//                    println("\t\tmove down")
                    moves.add(expNext)
                }
            }

            // left
            if (!next.hasBlizzardAt(expCoord.copy(second = expCoord.second - 1))) {
//                println("\tno blizzard left")
                expNext = from(next)
                if (expNext.expedition.move('<', Pair(expNext.rows, expNext.cols), expNext.end)) {
//                    println("\t\tmove left")
                    moves.add(expNext)
                }
            }

            // right
            if (!next.hasBlizzardAt(expCoord.copy(second = expCoord.second + 1))) {
//                println("\tno blizzard right")
                expNext = from(next)
                if (expNext.expedition.move('>', Pair(expNext.rows, expNext.cols), expNext.end)) {
//                    println("\t\tmove right")
                    moves.add(expNext)
                }
            }

            return moves
        }

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
    var b = 0
    input.forEachIndexed { row, str ->
        str.forEachIndexed { col, c ->
            if (c == '^' || c == 'v' || c == '<' || c == '>') {
                valley.blizzards.add(Blizzard(b, c, Pair(row, col)))
                b++
            }
        }
    }

    valley.print()
    println()
//    val nextStates = Valley.nextPossibleStates(valley)
//    println(nextStates.size)
//    nextStates.forEach { it.print() }
    for (i in 1 .. 18) {
        println("Minute $i:")
        Valley.nextPossibleStates(valley)[0].print()
        println()
    }

//    val queue = ArrayDeque<Valley>()
//    val visited = mutableListOf<Valley>()
//    queue.addLast(valley)
//    var steps = 0
//    while (queue.isNotEmpty()) {
//        val current = queue.removeFirst()
//
//        if (visited.contains(current)) {
//            continue
//        } else {
//            visited.add(current)
//        }
//
//        println("Minute: ${current.steps}; Expedition is at ${current.expedition.coords}")
//        if (current.steps == 4)
//            current.print()
//
//        if (current.expedition.coords == valley.end) {
//            steps = current.steps
//            break
//        }
//
//        val nextStates = Valley.nextPossibleStates(current)
//        if (nextStates.isNotEmpty()) {
//            nextStates.forEach {
//                if (!visited.contains(it)) {
//                    queue.addLast(it)
//                }
//            }
//        }
//    }
//
//    println("The shortest path to the exit takes $steps minutes.")
}