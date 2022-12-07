package day07.puzzle1

import day07.util.FileNode
import day07.util.FileType
import java.io.File
import java.math.BigInteger

fun main(args : Array<String>) {
    val fileName = "day07/input.txt"
    val lines: List<String> = File(fileName).readLines()

    // build tree
    var root: FileNode = FileNode(FileType.DIR, "/")
    var dir: FileNode = FileNode(FileType.DIR)
    lines.forEach {
        var tokens = it.split(" ")

        // capture cd and ls
        if (tokens[0] == "$") {

            // handle cd; ignore ls
            if (tokens[1] == "cd") {
                dir = if (tokens[2] == "/") {
                    root
                } else if (tokens[2] == "..") {
                    dir.getParent()!!
                } else {
                    if (!dir.contains(FileType.DIR, tokens[2])) {
                        dir.add(FileNode(FileType.DIR, tokens[2], dir))
                    }
                    dir.get(FileType.DIR, tokens[2])!!
                }
            }
        } else if (tokens[0] == "dir") { // add dir
            dir.add(FileNode(FileType.DIR, tokens[1], dir))
        } else { // add file
            dir.add(FileNode(FileType.FILE, tokens[1], dir, BigInteger(tokens[0])))
        }
    }

    println(sumDir(root))
}

fun sumDir(root: FileNode): BigInteger {
    if (root == null) {
        return BigInteger("0")
    }

    var sum = BigInteger("0")
    val queue: ArrayDeque<FileNode> = ArrayDeque()
    queue.addLast(root)
    while (queue.isNotEmpty()) {
        var node = queue.removeFirst()

        if (node.type == FileType.DIR && node.getSize() <= BigInteger("100000")) {
            sum = sum.add(node.getSize())
        }

        node.getContents().forEach {
            if (it.type == FileType.DIR) {
                queue.addLast(it)
            }
        }
    }

    return sum
}