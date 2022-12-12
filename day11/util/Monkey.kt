package day11.util

class Monkey(private val id: Int, private val operator: Char, private val amnt: Int,
             private val div: Int, private val ifTrue: Monkey, private val ifFalse: Monkey) {

    private val items: ArrayDeque<Int> = ArrayDeque()
    private var numInspected = 0

    fun inspectItem() {

        numInspected++
        println("Monkey $id:")
        println("\tMonkey inspects an item with a worry level of ${items.first()}.")
        when (operator) {
            '*' -> {
                items[0] *= amnt
                println("\t\tWorry level is multiplied by $amnt to ${items.first()}.")
            }
            '+' -> {
                items[0] += amnt
                println("\t\tWorry level increases by $amnt to ${items.first()}.")
            }
        }

        items[0] /= 3
        println("\t\tMonkey gets bored with item. Worry level is divided by 3 to ${items.first()}.")

        if (items[0] % div == 0) {
            println("\t\tCurrent worry level is divisible by $div.")
            ifTrue.items.addLast(items.removeFirst())
            println("\t\tItem with worry level ${items.first()} is thrown to monkey ${ifTrue.id}.")
        } else {
            println("\t\tCurrent worry level is not divisible by $div.")
            ifFalse.items.addLast(items.removeFirst())
            println("\t\tItem with worry level ${items.first()} is thrown to monkey ${ifFalse.id}.")
        }
    }
}