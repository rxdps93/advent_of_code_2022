package puzzle1

import java.io.File

fun outcome(round: Pair<String, String>): Int {
    var score = round.second.first().code - 87;

    when (round.second.first().code - round.first.first().code) {
        21 -> score += 6
        23 -> score += 3
        24 -> score += 6
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