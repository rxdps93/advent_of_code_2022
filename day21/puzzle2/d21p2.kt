package day21.puzzle2

import java.io.File
import kotlin.math.abs
import kotlin.math.pow

interface Monkey {
    val name: String

    fun yell(): Long
}

class NumberMonkey(override val name: String = "", var number: Long = 0L) : Monkey {
    override fun yell(): Long {
        return this.number
    }

}

class OperationMonkey(override val name: String = "",
                      var leftName: String = "left",
                      var operator: Char = '+',
                      var rightName: String = "right") : Monkey {

    var left: Monkey = NumberMonkey()
    var right: Monkey = NumberMonkey()
    var leftVal = 0L
    var rightVal = 0L

    override fun yell(): Long {
        return when (operator) {
            '+' -> {
                left.yell() + right.yell()
            }
            '-' -> {
                left.yell() - right.yell()
            }
            '*' -> {
                left.yell() * right.yell()
            }
            '/' -> {
                left.yell() / right.yell()
            }
            '=' -> {
                leftVal = left.yell()
                rightVal = right.yell()
                leftVal - rightVal
            }
            else -> -1L
        }
    }

}

fun main() {
    val monkeys = hashMapOf<String, Monkey>()
    File("day21/input.txt").forEachLine { line ->
        val tokens = line.replace(":", "").split(" ")
        if (tokens.size == 2) {
            monkeys[tokens[0]] = NumberMonkey(tokens[0], tokens[1].toLong())
        } else {
            monkeys[tokens[0]] = OperationMonkey(tokens[0], tokens[1],
                    if (tokens[0] == "root") '=' else tokens[2][0], tokens[3])
        }
    }

    monkeys.values.forEach {
        if (it is OperationMonkey) {
            it.left = monkeys[it.leftName]!!
            it.right = monkeys[it.rightName]!!
        }
    }
    val root = monkeys["root"]!! as OperationMonkey
    val humn = monkeys["humn"]!! as NumberMonkey
    root.yell()

    var step = 10.0.pow(10).toLong()
    while (root.leftVal != root.rightVal) {
        val diff = root.leftVal - root.rightVal
        humn.number += step
        root.yell()

        if (abs(root.leftVal - root.rightVal) > abs(diff)) {
            step /= 10L
            step = -step
        }
    }

    println("${root.leftVal} <--> ${root.rightVal}")

    println("The number you must yell is ${humn.yell()}")
}