package day13.puzzle1

import day13.util.IntegerNode
import day13.util.ListNode
import java.io.File
import day13.util.SignalNode

fun parse(packet: Iterator<String>): SignalNode {

    var nodes = mutableListOf<SignalNode>()
    while (packet.hasNext()) {
        when (val token = packet.next()) {
            "[" -> nodes.add(parse(packet))
            "]" -> return ListNode(nodes)
            else -> nodes.add(IntegerNode(token.toInt()))
        }
    }
    return ListNode(nodes)
}

fun splitTokens(packet: String): List<String> {
    var tokens = mutableListOf<String>()
    var digit: CharArray = charArrayOf()
    packet.forEach {

        when (it) {
            '[' -> tokens.add("[")
            ']' -> {
                if (digit.isNotEmpty()) {
                    tokens.add(String(digit))
                    digit = charArrayOf()
                }
                tokens.add("]")
            }
            ',' -> {
                if (digit.isNotEmpty()) {
                    tokens.add(String(digit))
                    digit = charArrayOf()
                }
            }
            else -> {
                digit += it
            }
        }
    }

    return tokens
}

fun main(args : Array<String>) {

    var input = File("day13/input.txt").readLines().filter { it.isNotBlank() }.map {
        parse(splitTokens(it).iterator())
    }

    var sum = 0
    input.chunked(2).forEachIndexed { index, pair -> sum += if (pair.first() < pair.last()) index + 1 else 0 }

    println("The sum of properly ordered pair indices is $sum.")
}