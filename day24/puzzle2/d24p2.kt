package day24.puzzle2

import java.io.File

data class Blizzard(val type: Char, var coords: Pair<Int, Int>) {
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
}

class Valley(var time: Int = 0) {

    var state = 0
    var expedition = start

    fun canMoveTo(target: Pair<Int, Int>): Boolean {

        if (target == end) {
            return true
        } else if ((target == start) && (expedition != start)) {
            return false
        }

        stateAtTime(this.state)!!.forEach { if (it.coords == target) { return false } }

        if (target == expedition) { return true }

        if (target.first <= 0 || target.first >= size.first - 1) {
            return false
        }

        if (target.second <= 0 || target.second >= size.second - 1) {
            return false
        }

        return true
    }

    companion object {

        var size = Pair(-1, -1)
        var start = Pair(-1, -1)
        var end = Pair(-1, -1)
        var states = hashMapOf<Int, List<Blizzard>>()

        fun stateAtTime(state: Int): List<Blizzard>? {
            return states[state % states.size]
        }

        fun nextBlizzardState(old: List<Blizzard>): List<Blizzard> {
            val new = mutableListOf<Blizzard>()
            old.forEach { new.add(Blizzard(it.type, it.coords)) }
            new.forEach { it.move(size)  }
            return new
        }

        fun determineMoves(current: Valley): List<Valley> {

            val next = from(current)
            next.time++
            next.state++
            val moves = mutableListOf<Valley>()

            // Can Move Right
            if (next.canMoveTo(Pair(next.expedition.first, next.expedition.second + 1))) {
                moves.add(from(next, Pair(next.expedition.first, next.expedition.second + 1)))
            }

            // Can Move Down
            if (next.canMoveTo(Pair(next.expedition.first + 1, next.expedition.second))) {
                moves.add(from(next, Pair(next.expedition.first + 1, next.expedition.second)))
            }

            // Can Move Up
            if (next.canMoveTo(Pair(next.expedition.first - 1, next.expedition.second))) {
                moves.add(from(next, Pair(next.expedition.first - 1, next.expedition.second)))
            }

            // Can Move Left
            if (next.canMoveTo(Pair(next.expedition.first, next.expedition.second - 1))) {
                moves.add(from(next, Pair(next.expedition.first, next.expedition.second - 1)))
            }

            // Can Wait
            if (next.canMoveTo(next.expedition)) {
                moves.add(from(next))
            }

            return moves
        }

        private fun from(old: Valley, newExpedition: Pair<Int, Int>? = null): Valley {
            val new = Valley(old.time)
            new.state = old.state
            if (newExpedition == null) {
                new.expedition = old.expedition.copy()
            } else {
                new.expedition = newExpedition
            }

            return new
        }
    }
}

fun main() {
    val input = File("day24/input.txt").readLines()
    val valley = Valley()
    Valley.start = Pair(0, input[0].indexOfFirst { it == '.' })
    Valley.end = Pair(input.lastIndex, input.last().indexOfFirst { it == '.' })
    Valley.size = Pair(input.size, input[0].length)
    valley.expedition = Valley.start

    val initialState = mutableListOf<Blizzard>()
    input.forEachIndexed { row, str ->
        str.forEachIndexed { col, c ->
            if (c == '^' || c == 'v' || c == '<' || c == '>') {
                initialState.add(Blizzard(c, Pair(row, col)))
            }
        }
    }

    var time = 0
    Valley.states[time] = initialState.toList()
    var currentState = initialState.toList()
    do {
        val nextState = Valley.nextBlizzardState(currentState)
        currentState = nextState

        if (!Valley.states.containsValue(currentState)) {
            Valley.states[++time] = currentState
        } else {
            break
        }

    } while (true)

    time = 0
    for (trip in 1..3) {
        val queue = ArrayDeque<Valley>()
        val visited = Array(Valley.states.size) { Array(Valley.size.first) { BooleanArray(Valley.size.second) { false } } }
        queue.addLast(valley)
        visited[time % Valley.states.size][Valley.start.first][Valley.start.second] = true
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            if (current.expedition == Valley.end) {
                time += current.time
                break
            }

            Valley.determineMoves(current).forEach { move ->
                if (!visited[move.state % Valley.states.size][move.expedition.first][move.expedition.second]) {
                    visited[move.state % Valley.states.size][move.expedition.first][move.expedition.second] = true
                    queue.addLast(move)
                }
            }
        }

        val temp = Valley.start
        Valley.start = Valley.end
        Valley.end = temp
        valley.expedition = Valley.start
        valley.time = 0
        valley.state = time % Valley.states.size
    }

    println("The shortest path, including the snack rescue, takes $time minutes.")
}