package day16.puzzle1

import day16.util.Path
import day16.util.Valve
import java.io.File

fun main() {

    val valves = hashMapOf<String, Valve>()
    File("day16/testInput.txt").forEachLine { line->
        val tokens = Regex("Valve ([A-Z]{2}) has flow rate=([0-9]+); tunnels? leads? to valves? ([A-Z, ]*)").find(line)!!
        valves[tokens.groupValues[1]] = Valve(tokens.groupValues[1], tokens.groupValues[2].toInt(), tokens.groupValues[3].split(", "))
    }

    for (valve in valves.values) {
        for (str in valve.adjacent) {
            valve.tunnels.add(valves[str]!!)
        }
    }

    val stack: ArrayDeque<Path> = ArrayDeque()
    stack.addFirst(Path(valves["AA"]!!, arrayListOf(), 30, 0))
    var best = Int.MIN_VALUE
    while (stack.isNotEmpty()) {
        var path = stack.removeFirst()

        var end = true
        for (valve in path.curValve.tunnels) {
            if (path.remaining - 1 > 0 && !path.openValves.contains(valve)) {
                var open = ArrayList(path.openValves)
                open.add(valve)
                var pressure = (path.remaining - 1) * valve.flowRate
//                System.out.println("at " + state + " using way " + target + "/" + dist + " freshly releasing " + newlyReleased);
                println("at ${path.curValve.name}, ${path.remaining}, ${path.openValves}, ${path.pressureReleased}")
                stack.addFirst(Path(valve, open, path.remaining - 1, path.pressureReleased + pressure))
                end = false
            }
        }

        if (end) {
            if (path.pressureReleased > best) {
                best = path.pressureReleased
            }
        }
    }

    println("Maximum pressure released in 30 minutes is: $best")
}