package day13.util

class ListNode(val nodes: List<SignalNode>): SignalNode {
    override fun compareTo(other: SignalNode): Int {
        return if (other is ListNode) {
            nodes.zip(other.nodes).map {
                it.first.compareTo(it.second)
            }.firstOrNull { it != 0 } ?: nodes.size.compareTo(other.nodes.size)
        } else {
            compareTo(ListNode(listOf(other as IntegerNode)))
        }
    }
}