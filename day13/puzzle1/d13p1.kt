package day13.puzzle1

import java.io.File

interface SignalNode

private class IntegerNode(val value: Int): SignalNode {
    fun toList(): SignalNode {
        return ListNode(listOf(this))
    }
}

private class ListNode(val nodes: List<SignalNode>): SignalNode {

}

fun solve(left: String, right: String, pairIndex: Int): Int {

    println("Pair #$pairIndex:")
    println("\t left: $left")
    println("\tright: $right")

    println(left.toList())
    println(right.toList())
    return 0
}

fun parse(packet: CharIterator): SignalNode {
    var nodes = mutableListOf<SignalNode>()
    while (packet.hasNext()) {
        when (val char = packet.next()) {
            '[' -> nodes.add(parse(packet))
            ']' -> return ListNode(nodes)
            else -> nodes.add(IntegerNode(char.digitToInt()))
        }
    }

    return ListNode(nodes)
}

fun main(args : Array<String>) {

    var input = File("day13/inputTest.txt").readLines().filter { it.isNotBlank() }.map {
        parse(it.filter { it != ',' }.toCharArray().iterator())
    }

    var test = "[1,[2,3],4,5]"
    var nodes = parse(test.filter { it != ',' }.toCharArray().iterator())

    println("hello")
//    var left = ""
//    var pairIndex = 1
//    var orderSum = 0
//    input.forEach {
//        if (left == "") {
//            left = it
//        } else {
//            orderSum += solve(left, it, pairIndex)
//            left = ""
//            pairIndex++
//        }
//    }
//
//    println("The sum of properly ordered pair indices is $orderSum.")
}