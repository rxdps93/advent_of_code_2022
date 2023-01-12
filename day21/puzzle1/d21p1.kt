package day21.puzzle1

import java.io.File

interface Monkey {
    val name: String

    fun yell(): Long
}

class NumberMonkey(override val name: String = "", private val number: Long = 0L) : Monkey {
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

    override fun yell(): Long {
        return when (operator) {
            '+' -> left.yell() + right.yell()
            '-' -> left.yell() - right.yell()
            '*' -> left.yell() * right.yell()
            '/' -> left.yell() / right.yell()
            else -> 0L
        }
    }

}

fun main() {
    var monkeys = hashMapOf<String, Monkey>()
    File("day21/input.txt").forEachLine { line ->
        var tokens = line.replace(":", "").split(" ")
        if (tokens.size == 2) {
            monkeys[tokens[0]] = NumberMonkey(tokens[0], tokens[1].toLong())
        } else {
            monkeys[tokens[0]] = OperationMonkey(tokens[0], tokens[1], tokens[2][0], tokens[3])
        }
    }

    monkeys.values.forEach {
        if (it is OperationMonkey) {
            it.left = monkeys[it.leftName]!!
            it.right = monkeys[it.rightName]!!
        }
    }

    println("The root monkey will yell ${monkeys["root"]!!.yell()}.")
}