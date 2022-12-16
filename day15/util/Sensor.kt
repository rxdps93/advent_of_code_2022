package day15.util

import kotlin.math.abs

class Sensor(val location: Pair<Int, Int>, val beacon: Pair<Int, Int>) {
    constructor(line: List<Int>): this(Pair(line[0], line[1]), Pair(line[2], line[3]))

    fun distanceToBeacon(): Int {
        return abs(location.first - beacon.first) + abs(location.second - beacon.second)
    }

    fun distanceTo(other: Sensor): Int {
        return abs(location.first - other.location.first) + abs(location.second - other.location.second)
    }

    override fun toString(): String {
        return "sensor(x=${location.first},y=${location.second}); beacon(x=${beacon.first},y=${beacon.second})"
    }
}