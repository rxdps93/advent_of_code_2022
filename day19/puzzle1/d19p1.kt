package day19.puzzle1

import java.io.File
import kotlin.system.measureNanoTime

private data class State(
        var minutes: Int = 24,
        var oreBots: Int = 1, var clayBots: Int = 0, var obsBots: Int = 0, var geoBots: Int = 0,
        var ore: Int = 0, var clay: Int = 0, var obsidian: Int = 0, var geodes: Int = 0)

private class Blueprint(val id: Int, var best: Int,
                val oreBotCost: Int, val clayBotCost: Int,
                val obsBotCost: Pair<Int, Int>, val geoBotCost: Pair<Int, Int>) {

    private val oreMax = maxOf(this.oreBotCost, this.clayBotCost, this.obsBotCost.first, this.geoBotCost.first)

    fun simulate() {

        val queue = ArrayDeque<State>()
        queue.addLast(State())
        val visited = hashSetOf<State>()

        while (queue.isNotEmpty()) {
            var state = queue.removeFirst()

            if (visited.contains(state)) {
                continue
            }

            visited.add(state)

            if (state.minutes <= 0) {
                this.best = maxOf(this.best, state.geodes)
                continue
            }

            // just collect resources
            queue.addLast(state.copy(
                    minutes = state.minutes - 1,
                    ore = state.ore + state.oreBots,
                    clay = state.clay + state.clayBots,
                    obsidian = state.obsidian + state.obsBots,
                    geodes = state.geodes + state.geoBots
            ))

            // buy orebot
            if (state.ore >= this.oreBotCost && state.oreBots < this.oreMax) {
                queue.addLast(state.copy(
                        minutes = state.minutes - 1,
                        ore = state.ore - this.oreBotCost + state.oreBots,
                        clay = state.clay + state.clayBots,
                        obsidian = state.obsidian + state.obsBots,
                        geodes = state.geodes + state.geoBots,
                        oreBots = state.oreBots + 1
                ))
            }

            // buy claybot
            if (state.ore >= this.clayBotCost && state.clayBots < this.obsBotCost.second) {
                queue.addLast(state.copy(
                        minutes = state.minutes - 1,
                        ore = state.ore - this.clayBotCost + state.oreBots,
                        clay = state.clay + state.clayBots,
                        obsidian = state.obsidian + state.obsBots,
                        geodes = state.geodes + state.geoBots,
                        clayBots = state.clayBots + 1
                ))
            }

            // buy obsbot
            if (state.ore >= this.obsBotCost.first && state.clay >= this.obsBotCost.second && state.obsBots < this.geoBotCost.second) {
                queue.addLast(state.copy(
                        minutes = state.minutes - 1,
                        ore = state.ore - this.obsBotCost.first + state.oreBots,
                        clay = state.clay - this.obsBotCost.second + state.clayBots,
                        obsidian = state.obsidian + state.obsBots,
                        geodes = state.geodes + state.geoBots,
                        obsBots = state.obsBots + 1
                ))
            }

            // buy geobot
            if (state.ore >= this.geoBotCost.first && state.obsidian >= this.geoBotCost.second) {
                queue.addLast(state.copy(
                        minutes = state.minutes - 1,
                        ore = state.ore - this.geoBotCost.first + state.oreBots,
                        clay = state.clay + state.clayBots,
                        obsidian = state.obsidian - this.geoBotCost.second + state.obsBots,
                        geodes = state.geodes + state.geoBots,
                        geoBots = state.geoBots + 1
                ))
            }
        }
    }

    fun qualityLevel(): Int {
        return id * best
    }

    override fun toString(): String {
        return "Blueprint: $id\n" +
                "\tEach ore robot costs $oreBotCost ore.\n" +
                "\tEach clay robot costs $clayBotCost ore.\n" +
                "\tEach obsidian robot costs ${obsBotCost.first} ore and ${obsBotCost.second} clay.\n" +
                "\tEach geode robot costs ${geoBotCost.first} ore and ${geoBotCost.second} obsidian.\n"
    }
}

fun main() {
    val input = File("day19/input.txt").readLines().map {  line ->
        val tokens = Regex("Blueprint ([0-9]*): Each ore robot costs ([0-9]*) ore. Each clay robot costs ([0-9]*) ore. Each obsidian robot costs ([0-9]*) ore and ([0-9]*) clay. Each geode robot costs ([0-9]*) ore and ([0-9]*) obsidian.").find(line)!!.groupValues
        Blueprint(tokens[1].toInt(), Int.MIN_VALUE,
                tokens[2].toInt(), tokens[3].toInt(),
                Pair(tokens[4].toInt(), tokens[5].toInt()), Pair(tokens[6].toInt(), tokens[7].toInt()))
    }

    input.forEach {
        it.simulate()
    }

    println("The sum of the quality levels is ${input.sumOf { it.qualityLevel() }}")
}