package day07.util

import java.math.BigInteger

enum class FileType {
    FILE, DIR
}

class FileNode(val type: FileType, val name: String = "", private val parent: FileNode? = null,
               private val size: BigInteger = BigInteger("0")) {

    private var contents: ArrayList<FileNode> = arrayListOf()

    fun getParent(): FileNode? {
        return this.parent
    }

    fun getContents(): ArrayList<FileNode> {
        return contents
    }

    fun get(type: FileType, name: String): FileNode? {
        this.contents.forEach {
            if (it.name == name && it.type == type) {
                return it
            }
        }
        return null
    }

    fun add(vararg input: FileNode) {
        for (node in input) {
            this.contents.add(node)
        }
    }

    fun contains(type: FileType, name: String): Boolean {
        this.contents.forEach {
            if (it.name == name && it.type == type) {
                return true
            }
        }
        return false
    }

    fun containsExactly(node: FileNode): Boolean {
        return contents.contains(node)
    }

    fun getSize(): BigInteger {
        return if (this.type == FileType.FILE) {
            this.size
        } else {
            this.contents.sumOf { it.getSize() }
        }
    }

    override fun toString(): String {
        return "${this.name} (${this.type}, size=${this.getSize()})"
    }
}