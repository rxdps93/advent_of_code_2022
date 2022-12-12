package day11.util

class Monkey(val id: Int) {

    private val items: ArrayDeque<Int> = ArrayDeque()
    private var numInspected = 0
    private var operator: Char = '+'
    private var amnt: String = ""
    private var div: Int = 0
    private var ifTrue: Int = 0
    private var ifFalse: Int = 0

    fun getInspected(): Int {
        return numInspected
    }

    fun addItem(item: Int) {
        items.addLast(item)
    }

    fun getItems(): ArrayDeque<Int> {
        return this.items
    }

    fun setOperation(operator: Char, amnt: String) {
        this.operator = operator
        this.amnt = if (amnt == "") "old" else amnt
    }

    fun setTest(div: Int) {
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

    fun inspectItem(trueMonkey: Monkey, falseMonkey: Monkey) {

        numInspected++
        var amntInt = if (amnt == "old") {
            items[0]
        } else {
            amnt.toInt()
        }
        when (operator) {
            '*' -> {
                items[0] *= amntInt
            }
            '+' -> {
                items[0] += amntInt
            }
        }

        items[0] /= 3

        if (items[0] % div == 0) {
            trueMonkey.items.addLast(items.removeFirst())
        } else {
            falseMonkey.items.addLast(items.removeFirst())
        }
    }
}