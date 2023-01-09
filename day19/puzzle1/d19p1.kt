package day19.puzzle1

import java.io.File

enum class Robot(val max: Int) {
    ORE(2),
    CLAY(4),
    OBSIDIAN(4),
    GEODE(Int.MAX_VALUE)
}

class Blueprint(val id: Int, var best: Int,
                val oreBotCost: Int, val clayBotCost: Int,
                val obsBotCost: Pair<Int, Int>, val geoBotCost: Pair<Int, Int>) {

    fun canAfford(ore: Int, clay: Int, obsidian: Int, robot: Robot): Boolean {
        return when (robot) {
            Robot.ORE -> {
                ore >= oreBotCost
            }
            Robot.CLAY -> {
                ore >= clayBotCost
            }
            Robot.OBSIDIAN -> {
                (ore >= obsBotCost.first) && (clay >= obsBotCost.second)
            }
            Robot.GEODE -> {
                (ore >= geoBotCost.first) && (obsidian >= geoBotCost.second)
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

fun simulate(bp: Blueprint) {
    var minutes = 1
    var botCount = hashMapOf<Robot, Int>()
    botCount[Robot.ORE] = 1
    botCount[Robot.CLAY] = 0
    botCount[Robot.OBSIDIAN] = 0
    botCount[Robot.GEODE] = 0
    var ore = 0
    var clay = 0
    var obsidian = 0
    var geodes = 0
    val robots = arrayOf(Robot.GEODE, Robot.CLAY, Robot.OBSIDIAN, Robot.ORE)
    while (minutes <= 24) {
        // try to begin construction a robot
        for (robot in robots) {
            if (botCount[robot]!! < robot.max && bp.canAfford(ore, clay, obsidian, robot)) {
                botCount[robot] = botCount[robot]!! + 1
                when (robot) {
                    Robot.ORE -> { ore -= bp.oreBotCost }
                    Robot.CLAY -> { ore -= bp.clayBotCost }
                    Robot.OBSIDIAN -> { ore -= bp.obsBotCost.first; clay -= bp.obsBotCost.second }
                    Robot.GEODE -> { ore -= bp.geoBotCost.first; obsidian -= bp.geoBotCost.second }
                }

                println("Built a robot: $robot")
                break
            }
        }


        ore += botCount[Robot.ORE]!!
        println("${botCount[Robot.ORE]} ore-collecting robots collects ${botCount[Robot.ORE]}; you now have $ore ore.")

        minutes++
    }
}

fun main() {
    val input = File("day19/testInput.txt").readLines().map {  line ->
        val tokens = Regex("Blueprint ([0-9]*): Each ore robot costs ([0-9]*) ore. Each clay robot costs ([0-9]*) ore. Each obsidian robot costs ([0-9]*) ore and ([0-9]*) clay. Each geode robot costs ([0-9]*) ore and ([0-9]*) obsidian.").find(line)!!.groupValues
        Blueprint(tokens[1].toInt(), Int.MIN_VALUE,
                tokens[2].toInt(), tokens[3].toInt(),
                Pair(tokens[4].toInt(), tokens[5].toInt()), Pair(tokens[6].toInt(), tokens[7].toInt()))
    }

    input.forEach { println(it) }

    input.forEach { simulate(it) }

    println("The sum of the quality levels is ${input.sumOf { it.qualityLevel() }}")

//    val oreBotMetrics = Triple(input.minOf { it.oreBotCost }, input.sumOf { it.oreBotCost } / input.size, input.maxOf { it.oreBotCost })
//    val clayBotMetrics = Triple(input.minOf { it.clayBotCost }, input.sumOf { it.clayBotCost } / input.size, input.maxOf { it.clayBotCost })
//    val obsBotMetrics = Triple(
//            Pair(input.minOf { it.obsBotCost.first }, input.minOf { it.obsBotCost.second }),
//            Pair(input.sumOf { it.obsBotCost.first } / input.size, input.sumOf { it.obsBotCost.second } / input.size),
//            Pair(input.maxOf { it.obsBotCost.first }, input.maxOf { it.obsBotCost.second }))
//    val geoBotMetrics = Triple(
//            Pair(input.minOf { it.geoBotCost.first }, input.minOf { it.geoBotCost.second }),
//            Pair(input.sumOf { it.geoBotCost.first } / input.size, input.sumOf { it.geoBotCost.second } / input.size),
//            Pair(input.maxOf { it.geoBotCost.first }, input.maxOf { it.geoBotCost.second }))
//    val oreMetrics = Triple(
//            input.minOf { minOf(it.oreBotCost, it.clayBotCost, it.obsBotCost.first, it.geoBotCost.first) },
//            input.sumOf { (it.oreBotCost + it.clayBotCost + it.obsBotCost.first + it.geoBotCost.first) / 4 } / input.size,
//            input.maxOf { maxOf(it.oreBotCost, it.clayBotCost, it.obsBotCost.first, it.geoBotCost.first) })
//    val clayMetrics = Triple(
//            input.minOf { it.obsBotCost.second },
//            input.sumOf { it.obsBotCost.second } / input.size,
//            input.maxOf { it.obsBotCost.second })
//    val obsMetrics = Triple(
//            input.minOf { it.geoBotCost.second },
//            input.sumOf { it.geoBotCost.second } / input.size,
//            input.maxOf { it.geoBotCost.second })
//
//    println("+----------+----------------+----------------+----------------+")
//    println("|  Metric  |       Min      |       Avg      |       Max      |")
//    println("+----------+----------------+----------------+----------------+")
//    println("| OreBot   | %2d ore         | %2d ore         | %2d ore         |"
//            .format(oreBotMetrics.first, oreBotMetrics.second, oreBotMetrics.third))
//    println("| ClayBot  | %2d ore         | %2d ore         | %2d ore         |"
//            .format(clayBotMetrics.first, clayBotMetrics.second, clayBotMetrics.third))
//    println("| ObsBot   | %2d ore %2d clay | %2d ore %2d clay | %2d ore %2d clay |"
//            .format(obsBotMetrics.first.first, obsBotMetrics.first.second,
//                    obsBotMetrics.second.first, obsBotMetrics.second.second,
//                    obsBotMetrics.third.first, obsBotMetrics.third.second))
//    println("| GeoBot   | %2d ore %2d obs  | %2d ore %2d obs  | %2d ore %2d obs  |"
//            .format(geoBotMetrics.first.first, geoBotMetrics.first.second,
//                    geoBotMetrics.second.first, geoBotMetrics.second.second,
//                    geoBotMetrics.third.first, geoBotMetrics.third.second))
//    println("| Ore      | %2d             | %2d             | %2d             |"
//            .format(oreMetrics.first, oreMetrics.second, oreMetrics.third))
//    println("| Clay     | %2d             | %2d             | %2d             |"
//            .format(clayMetrics.first, clayMetrics.second, clayMetrics.third))
//    println("| Obs      | %2d             | %2d             | %2d             |"
//            .format(obsMetrics.first, obsMetrics.second, obsMetrics.third))
//    println("+----------+----------------+----------------+----------------+")
}