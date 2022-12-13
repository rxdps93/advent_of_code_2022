package day12.util

import kotlin.math.abs

class Square(var x: Int = 0, var y: Int = 0, var height: Int = 0): Comparable<Square> {

    private val validNeighbors: MutableSet<Square> = mutableSetOf()
    var distanceFromStart: Int = 0
    var distanceFromEnd: Int = 0

    fun getHeuristic(): Int {
        return distanceFromEnd + distanceFromStart
    }

    fun getNeighbors(): MutableSet<Square> {
        return this.validNeighbors
    }

    fun addNeighbor(neighbor: Square) {
        this.validNeighbors.add(neighbor)
    }

    fun getCoords(): Pair<Int, Int> {
        return Pair(x, y)
    }

    fun setCoords(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun distanceTo(node: Square): Int {
        var h = abs(node.x - this.x)
        var v = abs(this.y - node.y)
        return h + v
    }

    override fun compareTo(other: Square): Int {
        return this.getHeuristic() - other.getHeuristic()
    }

    override fun toString(): String {
        return "${(height + 97).toChar()}($height): ($x, $y)"
    }

}