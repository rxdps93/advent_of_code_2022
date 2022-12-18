package day16.util

class Valve(val name: String, val flowRate: Int, val adjacent: List<String>) {
    val tunnels = mutableListOf<Valve>()
    val routes = hashMapOf<Valve, Int>()

    fun generateRoutes(valves: HashSet<Valve>) {
        val queue = ArrayDeque<Valve>()
        val visited = hashSetOf<Valve>()
        val dists = hashMapOf<Valve, Int>()
        queue.addLast(this)
        dists[this] = 0
        while (queue.isNotEmpty() && routes.size < valves.size) {
            val valve = queue.removeFirst()
            visited.add(valve)
            val dist = dists[valve]!!

            if (valve != this && valves.contains(valve)) {
                routes[valve] = dist + 1
            }

            for (adj in valve.tunnels) {
                if (!visited.contains(adj)) {
                    queue.addLast(adj)
                    dists[adj] = dist + 1
                }
            }
        }
    }

    override fun toString(): String {
        return this.name
    }
}