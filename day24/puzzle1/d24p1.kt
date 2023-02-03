package day24.puzzle1

import java.io.File

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

class Valley(var time: Int = 0) {

    val blizzards = mutableListOf<Blizzard>()
    var expedition = start

    fun canMoveTo(target: Pair<Int, Int>): Boolean {

        if (target == end) {
            return true
        } else if (target == start) {
            return false
        }

        this.blizzards.forEach { if (it.coords == target) { return false } }

        if (target.first <= 0 || target.first >= size.first - 1) {
            return false
        }

        if (target.second <= 0 || target.second >= size.second - 1) {
            return false
        }

        return true
    }

    fun print() {
        val out = MutableList(size.first) { CharArray(size.second) { '.' } }

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
        out[expedition.first][expedition.second] = 'E'

        out.forEach { println(String(it)) }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Valley) {
            var eq = (this.time == other.time) && (this.expedition == other.expedition)

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

        var size = Pair(-1, -1)
        var start = Pair(-1, -1)
        var end = Pair(-1, -1)
        var states = hashMapOf<Int, List<Blizzard>>()

        fun stateAtTime(time: Int): List<Blizzard>? {
            return states[time % states.size]
        }

        fun nextBlizzardState(old: List<Blizzard>): List<Blizzard> {
            val new = mutableListOf<Blizzard>()
            old.forEach { new.add(Blizzard(it.id, it.type, it.coords)) }
            new.forEach { it.move(size)  }
            return new.sortedBy { it.id }
        }

        fun printBlizzardState(state: List<Blizzard>) {
            val out = MutableList(size.first) { CharArray(size.second) { '.' } }

            out.first().fill('#')
            out.last().fill('#')
            out.forEach { it[0] = '#'; it[it.lastIndex] = '#' }
            out[start.first][start.second] = '.'
            out[end.first][end.second] = '.'

            state.forEach {
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

            out.forEach { println(String(it)) }
        }

        fun nextPossibleStates(current: Valley, blizzardState: MutableList<Blizzard>? = null): List<Valley> {
            val next = from(current)
            next.time++
            val moves = mutableListOf<Valley>()

            // move blizzards
            if (blizzardState == null) {
                next.blizzards.forEach { blizzard ->
                    blizzard.move(size)
                }
            } else {
//                println("found")
                next.blizzards.clear()
                blizzardState.forEach { next.blizzards.add(Blizzard(it.id, it.type, it.coords.copy())) }
            }

            // handle expedition
            // wait
            if (next.canMoveTo(next.expedition)) {
//                println("\twait")
                moves.add(from(next))
            }

            // up
            if (next.canMoveTo(Pair(next.expedition.first - 1, next.expedition.second))) {
//                println("\tmove up")
                val expNext = from(next)
                expNext.expedition = Pair(next.expedition.first - 1, next.expedition.second)
                moves.add(expNext)
            }

            // down
            if (next.canMoveTo(Pair(next.expedition.first + 1, next.expedition.second))) {
//                println("\tmove down")
                val expNext = from(next)
                expNext.expedition = Pair(next.expedition.first + 1, next.expedition.second)
                moves.add(expNext)
            }

            // left
            if (next.canMoveTo(Pair(next.expedition.first, next.expedition.second - 1))) {
//                println("\tmove left")
                val expNext = from(next)
                expNext.expedition = Pair(next.expedition.first, next.expedition.second - 1)
                moves.add(expNext)
            }

            // right
            if (next.canMoveTo(Pair(next.expedition.first, next.expedition.second + 1))) {
//                println("\tmove right")
                val expNext = from(next)
                expNext.expedition = Pair(next.expedition.first, next.expedition.second + 1)
                moves.add(expNext)
            }

            return moves
        }

        fun from(old: Valley): Valley {
            val new = Valley(old.time)
            new.expedition = old.expedition.copy()

            old.blizzards.forEach { new.blizzards.add(Blizzard(it.id, it.type, it.coords.copy())) }
            return new
        }
    }
}

fun main() {
    val input = File("day24/test.txt").readLines()
    val valley = Valley()
    Valley.start = Pair(0, input[0].indexOfFirst { it == '.' })
    Valley.end = Pair(input.lastIndex, input.last().indexOfFirst { it == '.' })
    Valley.size = Pair(input.size, input[0].length)

    valley.expedition = Valley.start
    var b = 0
    input.forEachIndexed { row, str ->
        str.forEachIndexed { col, c ->
            if (c == '^' || c == 'v' || c == '<' || c == '>') {
                valley.blizzards.add(Blizzard(b, c, Pair(row, col)))
                b++
            }
        }
    }

    var time = 0
    Valley.states[time] = (valley.blizzards.sortedBy { it.id })
    var current = valley.blizzards.sortedBy { it.id }
    do {
        val next = Valley.nextBlizzardState(current)
        current = next

        if (!Valley.states.containsValue(current)) {
            Valley.states[++time] = current
        } else {
            break
        }

    } while (true)


//    val queue = ArrayDeque<Valley>()
//    val visited = mutableListOf<Valley>()
//    val blizzardStates = hashMapOf(Pair(0, valley.blizzards))
//    queue.addLast(valley)
//    var time = 0
//    while (queue.isNotEmpty()) {
//        val current = queue.removeFirst()
//
//        println(current.expedition)
//
//        if (current.expedition == valley.end) {
//            time = current.time
//            break
//        }
//
//        val nextStates = Valley.nextPossibleStates(current, blizzardStates[current.time + 1])
//        if (nextStates.isNotEmpty()) {
//
//            blizzardStates.putIfAbsent(current.time + 1, nextStates[0].blizzards)
//            nextStates.forEach {
//                if (!visited.contains(it)) {
//                    visited.add(it)
//                    queue.addLast(it)
//                }
//            }
//        }
//    }
//
//    println("The shortest path to the exit takes $time minutes.")
}