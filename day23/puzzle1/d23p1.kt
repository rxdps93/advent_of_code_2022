package day23.puzzle1

import java.io.File

fun printGrove(grove: MutableList<MutableList<Char>>) {
    grove.forEach { r->
        r.forEach { c->
            print(c)
        }
        println()
    }
}

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

fun tryDirection(grove: MutableList<MutableList<Char>>, dir: Char) {
    // TODO: rotate direction consideration
}

fun main() {
    var grove = File("day23/testSmall.txt").readLines().map { it.toMutableList() }.toMutableList()
    println("== Initial State ==")
    printGrove(grove)
    println()

    var toMove: HashMap<Pair<Int, Int>, Pair<Int, Int>>
    for (round in 1..10) {
        toMove = hashMapOf()
        // pad
        padGrove(grove)

        // propose moves
        grove.forEachIndexed { r, row ->
            row.forEachIndexed { c, spot ->
                if (spot == '#') {
                    val n = grove[r - 1][c] == '.'
                    val s = grove[r + 1][c] == '.'
                    val e = grove[r][c + 1] == '.'
                    val w = grove[r][c - 1] == '.'
                    val ne = grove[r - 1][c - 1] == '.'
                    val nw = grove[r - 1][c + 1] == '.'
                    val se = grove[r + 1][c - 1] == '.'
                    val sw = grove[r + 1][c + 1] == '.'

                    // if not isolated
                    if (!n || !s || !e || !w || !ne || !nw || !se || !sw) {
                        val from = Pair(r, c)
                        if (n && ne && nw) { // try north
                            toMove[from] = Pair(r - 1, c)
                        } else if (s && se && sw) { // try south
                            toMove[from] = Pair(r + 1, c)
                        } else if (w && nw && sw) { // try west
                            toMove[from] = Pair(r, c - 1)
                        } else if (e && ne && se) { // try east
                            toMove[from] = Pair(r, c + 1)
                        }
                    }
                }
            }
        }

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
        println("== End of Round $round ==")
        printGrove(grove)
        println()
    }
    trimGrove(grove)
    printGrove(grove)

    println("The number of empty ground tiles is ${grove.sumOf { it.sumOf { c -> if (c == '.') 1L else 0L } }}")
}