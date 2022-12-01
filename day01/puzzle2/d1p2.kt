import java.io.File

fun main(args : Array<String>) {
    val fileName = "../input.txt"
    val lines: List<String> = File(fileName).readLines()

    var elfCals: ArrayList<Int> = arrayListOf()
    var sum: Int = 0
    lines.forEach {
        var cal = it.toIntOrNull()
        if (cal != null) {
            sum += cal
        } else {
            elfCals.add(sum)
            sum = 0
        }
    }

    elfCals.sortDescending()
    println(elfCals[0] + elfCals[1] + elfCals[2])
}