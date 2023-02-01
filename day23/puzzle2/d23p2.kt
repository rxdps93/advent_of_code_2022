package day23.puzzle2

import java.io.File

fun trimGrove(grove: MutableList<MutableList<Char>>) {

    // trim top
    var trim = grove.indexOfFirst { it.contains('#') }
    for (i in 0 until trim) {
        grove.removeFirst()
    }

    // trim bottom
    trim = grove.lastIndex - grove.indexOfLast { it.contains('#') }
    for (i in 0 until trim) {
        grove.removeLast()
    }

    // trim left
    trim = grove.minOf { if (it.contains('#')) it.indexOfFirst { c -> c == '#' } else Int.MAX_VALUE }
    for (i in 0 until trim) {
        grove.forEach { it.removeFirst() }
    }

    // trim right
    trim = grove[0].lastIndex - grove.maxOf { it.indexOfLast { c -> c == '#' } }
    for (i in 0 until trim) {
        grove.forEach { it.removeLast() }
    }
}

fun padGrove(grove: MutableList<MutableList<Char>>) {
    // add left and right
    grove.forEach { it.add(0, '.'); it.add('.') }
    // add top and bottom
    grove.add(0, MutableList(grove[0].size) { '.' })
    grove.add(MutableList(grove[0].size) { '.' })
}

enum class Direction {
    NORTH {
        override fun propose(surround: HashMap<String, Boolean>, toMove: HashMap<Pair<Int, Int>, Pair<Int, Int>>,
                             from: Pair<Int, Int>): Boolean {
            if (surround["n"]!! && surround["ne"]!! && surround["nw"]!!) {
                toMove[from] = Pair(from.first - 1, from.second)
                return true
            }

            return false
        }
    },
    SOUTH {
        override fun propose(surround: HashMap<String, Boolean>, toMove: HashMap<Pair<Int, Int>, Pair<Int, Int>>,
                             from: Pair<Int, Int>): Boolean {
            if (surround["s"]!! && surround["se"]!! && surround["sw"]!!) {
                toMove[from] = Pair(from.first + 1, from.second)
                return true
            }

            return false
        }
    },
    WEST {
        override fun propose(surround: HashMap<String, Boolean>, toMove: HashMap<Pair<Int, Int>, Pair<Int, Int>>,
                             from: Pair<Int, Int>): Boolean {
            if (surround["w"]!! && surround["sw"]!! && surround["nw"]!!) {
                toMove[from] = Pair(from.first, from.second - 1)
                return true
            }

            return false
        }
    },
    EAST {
        override fun propose(surround: HashMap<String, Boolean>, toMove: HashMap<Pair<Int, Int>, Pair<Int, Int>>,
                             from: Pair<Int, Int>): Boolean {
            if (surround["e"]!! && surround["ne"]!! && surround["se"]!!) {
                toMove[from] = Pair(from.first, from.second + 1)
                return true
            }

            return false
        }
    };

    abstract fun propose(surround: HashMap<String, Boolean>, toMove: HashMap<Pair<Int, Int>, Pair<Int, Int>>,
                         from: Pair<Int, Int>): Boolean
}

fun main() {
    val grove = File("day23/input.txt").readLines().map { it.toMutableList() }.toMutableList()

    val order = mutableListOf(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST)
    var toMove: HashMap<Pair<Int, Int>, Pair<Int, Int>>
    var round = 0
    do {
        round++
        toMove = hashMapOf()
        // pad
        padGrove(grove)

        // propose moves
        grove.forEachIndexed { r, row ->
            row.forEachIndexed { c, spot ->
                if (spot == '#') {
                    val surround = hashMapOf(
                            Pair("n", grove[r - 1][c] == '.'),
                            Pair("s", grove[r + 1][c] == '.'),
                            Pair("e", grove[r][c + 1] == '.'),
                            Pair("w", grove[r][c - 1] == '.'),
                            Pair("ne", grove[r - 1][c + 1] == '.'),
                            Pair("nw", grove[r - 1][c - 1] == '.'),
                            Pair("se", grove[r + 1][c + 1] == '.'),
                            Pair("sw", grove[r + 1][c - 1] == '.'))

                    // if not isolated
                    if (surround.values.contains(false)) {
                        val from = Pair(r, c)
                        for (dir in order) {
                            if (dir.propose(surround, toMove, from)) {
                                break
                            }
                        }
                    }
                }
            }
        }

        // change proposal order
        order.add(order.removeFirst())

        // handle collision
        val collisions = toMove.values.groupingBy { it }.eachCount().filter { it.value > 1 }.keys
        val iter = toMove.entries.iterator()
        while (iter.hasNext()) {
            if (collisions.contains(iter.next().value)) {
                iter.remove()
            }
        }

        // move
        toMove.forEach { move ->
            grove[move.key.first][move.key.second] = '.'
            grove[move.value.first][move.value.second] = '#'
        }


        // trim
        trimGrove(grove)
    } while (toMove.isNotEmpty())

    println("The first round where no Elf moved was round $round")
}