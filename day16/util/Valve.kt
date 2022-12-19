package day16.util

class Valve(val name: String, val flowRate: Int, val adjacent: List<String>) {
    val tunnels = mutableListOf<Valve>()

    override fun toString(): String {
        return this.name
    }
}