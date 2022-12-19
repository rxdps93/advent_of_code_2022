package day16.puzzle1

import day16.util.Path
import day16.util.Valve
import java.io.File

fun openValves(remaining: Int,
               curValve: Valve,
               valves: HashMap<String, Valve>,
               targetValves: Set<Valve>,
               paths: HashMap<Path, Int> = hashMapOf()
): Int {

    val pressureReleased = remaining * curValve.flowRate
    val path = Path(curValve, targetValves, remaining)

    paths.getOrPut(path) {
        targetValves.filter { target-> curValve.routes[target]!! < remaining }
            .takeIf { it.isNotEmpty() }?.maxOf { target->
                val time = remaining - 1 - curValve.routes[target]!!
                openValves(time, target, valves,targetValves - target, paths)
            }?: 0
    }

}

fun main() {

    val valves = hashMapOf<String, Valve>()
    File("day16/input.txt").forEachLine { line->
        val tokens = Regex("Valve ([A-Z]{2}) has flow rate=([0-9]+); tunnels? leads? to valves? ([A-Z, ]*)").find(line)!!
        valves[tokens.groupValues[1]] = Valve(tokens.groupValues[1], tokens.groupValues[2].toInt(), tokens.groupValues[3].split(", "))
    }

    for (valve in valves.values) {

        for (str in valve.adjacent) {
            valve.tunnels.add(valves[str]!!)
        }
    }

    val workingValves: HashSet<Valve> = valves.values.filter { it.flowRate > 0 }.toHashSet()

    for (valve in valves.values) {
        valve.generateRoutes(workingValves)
    }

    println("Maximum pressure released in 30 minutes is: ${openValves(30, valves["AA"]!!, valves, workingValves)}")


//    val queue: ArrayDeque<Path> = ArrayDeque()
//    queue.addLast(Path(valves["AA"]!!, arrayListOf(valves["AA"]!!), 30, 0))
//    var best = Int.MIN_VALUE
//    while (queue.isNotEmpty()) {
//        var path = queue.removeFirst()
//
//        if (path.pressureReleased > best) {
//            println(path.pressureReleased)
//            best = path.pressureReleased
//        }
//
//        for (route in path.curValve.routes.entries) {
//            var dist = route.value
//            var valve = route.key
//            if (path.remaining - dist >= 0 && !path.openValves.contains(valve)) {
//                var open = ArrayList(path.openValves)
//                open.add(valve)
//                var pressure = (path.remaining - dist) * valve.flowRate
//                queue.addLast(Path(valve, open, path.remaining - dist, path.pressureReleased + pressure))
//            }
//        }
//    }
//
//    println("Maximum pressure released in 30 minutes is: $best")
}