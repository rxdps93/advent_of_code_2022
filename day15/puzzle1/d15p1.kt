package day15.puzzle1

import day15.util.Sensor
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val sensors = File("day15/input.txt").readLines().map { line->
        Sensor(line.replace("[^0-9\\-,:]".toRegex(), "").split(":", ",").map { it.toInt() })
    }

    val row = if (sensors.size == 14) 10 else 2000000

    val xmin = sensors.minOf { min(it.location.first - it.distanceToBeacon(), it.beacon.first) }
    val xmax = sensors.maxOf { max(it.location.first + it.distanceToBeacon(), it.beacon.first) }

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