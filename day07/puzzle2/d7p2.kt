package day07.puzzle2

import day07.util.FileNode
import day07.util.FileType
import java.io.File
import java.math.BigInteger

fun main() {
    val lines = File("day07/input.txt").readLines()

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

    val space = BigInteger("30000000").subtract(BigInteger("70000000").subtract(root.getSize()))

    var dirSize = BigInteger(Int.MAX_VALUE.toString())
    val queue: ArrayDeque<FileNode> = ArrayDeque()
    queue.addLast(root)
    while (queue.isNotEmpty()) {
        var node = queue.removeFirst()

        if (node.type == FileType.DIR && node.getSize() >= space && node.getSize() < dirSize) {
            dirSize = node.getSize()
        }

        node.getContents().forEach {
            if (it.type == FileType.DIR) {
                queue.addLast(it)
            }
        }
    }

    return dirSize
}