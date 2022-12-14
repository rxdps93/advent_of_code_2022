package day13.util

class IntegerNode(val value: Int): SignalNode {
    override fun compareTo(other: SignalNode): Int {
        return if (other is IntegerNode) {
            value.compareTo(other.value)
        } else {
            ListNode(listOf(this)).compareTo(other)
        }
    }

}