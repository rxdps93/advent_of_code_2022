package day15.puzzle2

import day15.util.Sensor
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
import kotlin.random.nextInt

fun main() {
    val taken = mutableSetOf<Pair<Int, Int>>()
    val sensors = File("day15/input.txt").readLines().map { line->
        val coords = line.replace("[^0-9\\-,:]".toRegex(), "").split(":", ",").map { it.toInt() }
        taken.add(Pair(coords[0], coords[1]))
        taken.add(Pair(coords[2], coords[3]))
        Sensor(coords)
    }

    val bounds = if (sensors.size == 14) 20 else 4000000
    loop@for (y in 0..bounds) {
        var x = 0
        while (x <= bounds) {

            if (!taken.contains(Pair(x, y))) {

                var beacon = true
                for (sensor in sensors) {
                    val dist = abs(x - sensor.location.first) + abs(y - sensor.location.second)
                    if (dist <= sensor.distanceToBeacon()) {
                        beacon = false
                        x = sensor.location.first + sensor.distanceToBeacon() - abs(sensor.location.second - y)
                        break
                    }
                }

                if (beacon) {
                    println("The signal strength is ${(4000000L * x.toLong()) + y.toLong()}")
                    break@loop
                }
            }
            x++
        }
    }
}