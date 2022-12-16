package day16.puzzle1

import day16.util.Valve
import java.io.File

fun main() {

    val valves = hashMapOf<String, Valve>()
    File("day16/testInput.txt").forEachLine { line->
        val tokens = Regex("Valve ([A-Z]{2}) has flow rate=([0-9]+); tunnels? leads? to valves? ([A-Z, ]*)").find(line)!!
        valves[tokens.groupValues[1]] = Valve(tokens.groupValues[1], tokens.groupValues[2].toInt(), tokens.groupValues[3].split(", "))
    }

    valves.values.forEach { println("${it.name}@${it.flowRate}: ${it.adjacent}") }
}