package day09.util

import kotlin.math.abs

public class Knot() {
    private var x = 0
    private var y = 0
    private var visited: MutableSet<Pair<Int, Int>> = mutableSetOf()

    fun getPosition(): Pair<Int, Int> {
        return Pair(x, y)
    }

    fun getVisited(): Set<Pair<Int, Int>> {
        return visited
    }

    fun updatePosition(dir: Char): Pair<Int, Int> {
        visited.add(Pair(x, y))
        when (dir) {
            'L' -> x--
            'R' -> x++
            'U' -> y++
            'D' -> y--
        }
        visited.add(Pair(x, y))
        return getPosition()
    }

    private fun moveBy(incX: Int, incY: Int) {
        visited.add(Pair(x, y))
        x += incX
        y += incY
        visited.add(Pair(x, y))
    }

    fun moveTo(knot: Knot) {
        val dist = Pair(knot.getPosition().first - this.x, knot.getPosition().second - this.y)

        if (dist.first == 0 && abs(dist.second) > 1) { // vertical move
            this.moveBy(0, dist.second / 2)
        }  else if (dist.second == 0 && abs(dist.first) > 1) { // horizontal move
            this.moveBy(dist.first / 2, 0)
        } else if ((dist.first != 0 && dist.second != 0) && (abs(dist.first) > 1 || abs(dist.second) > 1)) { // diagonal move
            this.moveBy(abs(dist.first) / dist.first, abs(dist.second) / dist.second)
        }
    }
}