package day07

import AoCTask

// https://adventofcode.com/2022/day/7

sealed interface Command

sealed interface TreeNode {
    val name: String
    val size: Int
}

sealed class CmdLine {
    data class CdCommand(val path: String): CmdLine(), Command
    object LsCommand: CmdLine(), Command
    data class File(override val name: String, override val size: Int): CmdLine(), TreeNode
    data class Directory(override val name: String, override var size: Int = 0): CmdLine(), TreeNode
}

data class Tree(val data: TreeNode, val parent: Tree? = null, val children: MutableList<Tree> = mutableListOf())

fun parseLine(line: String): CmdLine {
    return if (line.startsWith("$")) {
        val cmdAndArgs = line.drop(2).split(" ")
        if (cmdAndArgs.first() == "cd") {
            CmdLine.CdCommand(path = cmdAndArgs.last())
        } else {
            CmdLine.LsCommand
        }
    } else {
        val fileOrDir = line.split(" ")
        if (fileOrDir.first() == "dir") {
            CmdLine.Directory(fileOrDir.last())
        } else {
            CmdLine.File(fileOrDir.last(), fileOrDir.first().toInt())
        }
    }
}

fun renderTree(tree: Tree, depth: Int = 0): String {
    return when (tree.data) {
        is CmdLine.Directory -> {
            val string = "${" ".repeat(depth)}- ${tree.data.name} (dir)"
            string + tree.children.joinToString("\n") {
                renderTree(it, depth + 1)
            }
        }
        is CmdLine.File -> {
            "${" ".repeat(depth)}- ${tree.data.name} (file, size=${tree.data.size})"
        }
    }
}

fun computeSizes(tree: Tree): Int {
    return if (tree.children.isEmpty()) {
        if (tree.data is CmdLine.File) {
            tree.data.size
        } else {
            0
        }
    } else {
        if (tree.data is CmdLine.Directory) {
            val totalSize = tree.children.sumOf { computeSizes(it) }
            tree.data.size = totalSize
            totalSize
        } else {
            0
        }
    }
}

fun collectDirectories(tree: Tree): List<CmdLine.Directory> {
    return if (tree.data is CmdLine.Directory) {
        listOf(tree.data) + tree.children.flatMap { collectDirectories(it) }
    } else {
        emptyList()
    }
}


private fun constructFileSystem(input: List<String>): Tree {
    val lines = input.map { parseLine(it) }

    var currentDir = Tree(CmdLine.Directory("/"))

    lines.forEach { line ->
        when (line) {
            is CmdLine.CdCommand -> {
                currentDir = when (line.path) {
                    "/" -> Tree(CmdLine.Directory("/"))
                    ".." -> currentDir.parent!!
                    else -> currentDir.children.first {
                        it.data is CmdLine.Directory && it.data.name == line.path
                    }
                }
            }

            is CmdLine.Directory -> {
                currentDir.children.add(Tree(line, currentDir))
            }

            is CmdLine.File -> {
                currentDir.children.add(Tree(line, currentDir))
            }

            CmdLine.LsCommand -> {

            }
        }
    }

    while (currentDir.parent != null) {
        currentDir = currentDir.parent!!
    }

    computeSizes(currentDir)

    return currentDir
}


fun part1(input: List<String>): Int {
    val root  = constructFileSystem(input)

    val dirs = collectDirectories(root)

    val total = dirs.filter {
        it.size <= 100000
    }.sumOf {
        it.size
    }

    return total
}

fun part2(input: List<String>): Int {
    val diskSize = 70000000
    val minSizeRequired = 30000000

    val root  = constructFileSystem(input)
    val currentFreeSpace = diskSize - root.data.size
    val minSpaceToFreeUp = minSizeRequired - currentFreeSpace

    val dirs = collectDirectories(root)

     val dirToDelete = dirs.filter {
         it.size >= minSpaceToFreeUp
     }.minBy {
         it.size
     }

    return dirToDelete.size
}

fun main() = AoCTask("day07").run {
    // test if implementation meets criteria from the description, like:
    check(part1(testInput).also(::println) == 95437)
    check(part2(testInput).also(::println) == 24933642)

    println(part1(input))
    println(part2(input))
}
