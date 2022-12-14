package day05.puzzle2

import java.io.File

fun main() {
    val lines = File("day05/input.txt").readLines()

    var inputSplit = 0
    var stackCount = 0
    var instructions: ArrayList<Triple<Int, Int, Int>> = arrayListOf()
    for (i in lines.indices) {

        if (lines[i] == "") {
            inputSplit = i
            stackCount = lines[i - 1].trim().last().digitToInt()
        } else if (lines[i].substring(0..3) == "move") {
            var instr = lines[i].filter { it.isDigit() || it.isWhitespace() }.trimStart().trimEnd().split("\\s+".toRegex())
            instructions.add(Triple(instr[0].toInt(), instr[1].toInt(), instr[2].toInt()))
        }
    }

    var stacks: Array<ArrayDeque<Char>> = Array(stackCount) { ArrayDeque() }
    for (i in 0 until inputSplit) {
        for (c in lines[i].indices) {
            if (lines[i][c].isLetter()) {
                stacks[((c - 1) / 4)].addFirst(lines[i][c])
            }
        }
    }

    var crane = ArrayDeque<Char>()
    instructions.forEach {
//        for (i in 0 until it.first) {
//            stacks[it.third - 1].addLast(stacks[it.second - 1].removeLast())
//        }
        for (i in 0 until it.first) {
            crane.addFirst(stacks[it.second - 1].removeLast())
        }

        while (!crane.isEmpty()) {
            stacks[it.third - 1].addLast(crane.removeFirst())
        }
    }

    stacks.forEach { print(it.lastOrNull()) }
}