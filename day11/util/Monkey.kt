package day11.util

import java.math.BigInteger

class Monkey(val id: Int, private val relief: Long) {

    private val items: ArrayDeque<Long> = ArrayDeque()
    private var numInspected = 0L
    private var operator: Char = '+'
    private var amnt: String = ""
    private var div: Long = 0
    private var ifTrue: Int = 0
    private var ifFalse: Int = 0

    fun getInspected(): Long {
        return numInspected
    }

    fun addItem(item: Long) {
        items.addLast(item)
    }

    fun getItems(): ArrayDeque<Long> {
        return this.items
    }

    fun setOperation(operator: Char, amnt: String) {
        this.operator = operator
        this.amnt = if (amnt == "") "old" else amnt
    }

    fun getTest(): Long {
        return this.div
    }

    fun setTest(div: Long) {
        this.div = div
    }

    fun getTrueMonkey(): Int {
        return this.ifTrue
    }

    fun getFalseMonkey(): Int {
        return this.ifFalse
    }

    fun setTrueMonkey(monkey: Int) {
        this.ifTrue = monkey
    }

    fun setFalseMonkey(monkey: Int) {
        this.ifFalse = monkey
    }

    fun inspectItem(trueMonkey: Monkey, falseMonkey: Monkey, reducer: Long? = null) {

        numInspected++
        if (reducer != null) {
            items[0] %= reducer
        }

        var amntInt = if (amnt == "old") {
            items[0]
        } else {
            amnt.toLong()
        }
        when (operator) {
            '*' -> {
                items[0] *= amntInt
            }
            '+' -> {
                items[0] += amntInt
            }
        }

        items[0] /= this.relief

        if (items[0] % div == 0L) {
            trueMonkey.items.addLast(items.removeFirst())
        } else {
            falseMonkey.items.addLast(items.removeFirst())
        }
    }
}