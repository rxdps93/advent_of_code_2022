package puzzle2

import java.io.File

fun outcome(round: Pair<String, String>): Int {
    var score = 3 * (round.second.first().code - 88)

    when (round.second.first()) {
        'X' -> {
            when (round.first.first()) {
                'A' -> score += 3
                'B' -> score += 1
                'C' -> score += 2
            }
        }
        'Y' -> {
            score += round.first.first().code - 64
        }
        'Z' -> {
            when (round.first.first()) {
                'A' -> score += 2
                'B' -> score += 3
                'C' -> score += 1
            }
        }
    }

    return score
}

fun main(args: Array<String>) {
    val fileName = "day02/input.txt"
    val lines: List<String> = File(fileName).readLines()

    val rounds: ArrayList<Pair<String, String>> = arrayListOf()
    lines.forEach {
        rounds.add(it.split(" ").zipWithNext()[0])
    }

    var score = 0
    rounds.forEach {
        score += outcome(it)
    }

    println("Final Score: $score")
}