package day15.puzzle1

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private class Sensor(val location: Pair<Int, Int>, val beacon: Pair<Int, Int>) {
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

fun main() {
    val sensors = File("day15/input.txt").readLines().map { line->
        Sensor(line.replace("[^0-9\\-,:]".toRegex(), "").split(":", ",").map { it.toInt() })
    }

    val row = if (sensors.size == 14) 10 else 2000000

    val xmin = sensors.minOf { min(it.location.first - it.distanceToBeacon(), it.beacon.first) }
    val xmax = sensors.maxOf { max(it.location.first + it.distanceToBeacon(), it.beacon.first) }
//    val ymin = sensors.minOf { min(it.location.second, it.beacon.second) }
//    val ymax = sensors.maxOf { max(it.location.second, it.beacon.second) }

//    println("xmin: $xmin, xmax: $xmax, ymin: $ymin, ymax: $ymax")

    val taken = mutableSetOf<Pair<Int, Int>>()
    val squares = mutableSetOf<Pair<Int, Int>>()
    for (sensor in sensors) {

        if (sensor.location.second == row)
            taken.add(sensor.location)

        if (sensor.beacon.second == row)
            taken.add(sensor.beacon)

        for (x in xmin..xmax) {
            val square = Pair(x, row)
            val dist = abs(x - sensor.location.first) + abs(row - sensor.location.second)
            if (dist <= sensor.distanceToBeacon() && !taken.contains(square)) {
                squares.add(square)
            }
        }
    }

    println("There are ${squares.size} squares where beacons cannot be present on row $row.")
}