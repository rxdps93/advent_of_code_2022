package day16.puzzle1

import day16.util.Path
import day16.util.Valve
import java.io.File

fun openValves(remaining: Int,
               curValve: Valve,
               valves: HashMap<String, Valve>,
               targetValves: Set<Valve>,
               distMap: Map<String, MutableMap<String, Int>>,
               paths: HashMap<Path, Int> = hashMapOf()
): Int {

    val pressureReleased = remaining * curValve.flowRate
    val path = Path(curValve, targetValves, remaining)

    return pressureReleased + paths.getOrPut(path) {
        val pressure = targetValves.filter { target->
            distMap.getValue(path.curValve.name).getValue(target.name) < remaining
        }.takeIf { it.isNotEmpty() }?.maxOf { target->
            val time = remaining - 1 - distMap.getValue(path.curValve.name).getValue(target.name)
            openValves(time, target, valves, targetValves - target, distMap, paths)
        }?: 0
        maxOf(pressure, 0)
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

    val distMap = valves.keys.map { valve->
        val dists = mutableMapOf<String, Int>().withDefault { Int.MAX_VALUE }.apply { put(valve, 0) }
        val queue = mutableListOf(valve)
        while (queue.isNotEmpty()) {
            val curValve = queue.removeFirst()
            valves.getValue(curValve).adjacent.forEach { adj->
                val dist = dists.getValue(curValve) + 1
                if (dist < dists.getValue(adj)) {
                    dists[adj] = dist
                    queue.add(adj)
                }
            }
        }
        dists
    }.associateBy { it.keys.first() }

    println("Maximum pressure released in 30 minutes is: " +
            "${openValves(30, valves.getValue("AA"), valves, workingValves, distMap)}")
}