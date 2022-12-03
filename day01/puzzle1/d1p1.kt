import java.io.File

fun main(args : Array<String>) {
    val fileName = "day01/input.txt"
    val lines: List<String> = File(fileName).readLines()

    var maxCals: Int = Int.MIN_VALUE
    var sum: Int = 0
    lines.forEach {
        var cal = it.toIntOrNull()
        if (cal != null) {
            sum += cal
        } else {
            maxCals = if (sum > maxCals) sum else maxCals
            sum = 0
        }
    }

    println(maxCals)
}