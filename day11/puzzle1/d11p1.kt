package day11.puzzle1

import day11.util.Monkey
import java.io.File

fun main() {
    val lines = File("day11/input.txt").readLines()

    val monkeys: ArrayList<Monkey> = arrayListOf()
    var counter = 0;
    lines.forEach { line ->
        when (counter % 7) {
            0 -> {
                monkeys.add(Monkey(monkeys.size, 3L))
            }
            1 -> {
                line.filter { it.isDigit() || it == ',' }.split(",").forEach {
                    monkeys.last().addItem(it.toLong())
                }
            }
            2 -> {
                var filter = line.filter { it == '+' || it == '*' || it.isDigit() }
                monkeys.last().setOperation(filter[0], filter.substring(1))
            }
            3 -> {
                monkeys.last().setTest(line.filter { it.isDigit() }.toLong())
            }
            4 -> {
                monkeys.last().setTrueMonkey(line.last().digitToInt())
            }
            5 -> {
                monkeys.last().setFalseMonkey(line.last().digitToInt())
            }
        }

        counter++
    }

    for (round in 1..20) {
        for (monkey in monkeys) {
            while (monkey.getItems().isNotEmpty()) {
                monkey.inspectItem(monkeys[monkey.getTrueMonkey()], monkeys[monkey.getFalseMonkey()])
            }
        }
    }

    monkeys.forEach { println("Monkey ${it.id} inspected items ${it.getInspected()} times.") }
    monkeys.sortByDescending { it.getInspected() }
    println("Monkey business: ${monkeys[0].getInspected() * monkeys[1].getInspected()}")
}