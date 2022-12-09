package day08

import AoCTask

// https://adventofcode.com/2022/day/8


private fun firstPassFromEdge(input: List<String>): List<List<Pair<Int, Boolean>>> {
    val rowHeights = input.first().map { -1 }.toMutableList()
    val firstPass = input.map { row ->
        var maxPreviousHeight = -1
        row.mapIndexed { index, tree ->
            val height = tree.digitToInt()
            var visible = false
            if (rowHeights[index] < height) {
                rowHeights[index] = height
                visible = true
            }
            if (maxPreviousHeight < height) {
                maxPreviousHeight = height
                visible = true
            }
            height to visible
        }
    }
    return firstPass
}


private fun secondPassFromEdge(
    firstPass: List<List<Pair<Int, Boolean>>>
): List<List<Pair<Int, Boolean>>> {
    val rowHeights = firstPass.first().map { -1 }.toMutableList()
    return firstPass.reversed().map { row ->
        var maxPreviousHeight = -1
        row.reversed().mapIndexed { index, (height, alreadyVisible) ->
            var visible = alreadyVisible
            if (rowHeights[index] < height) {
                rowHeights[index] = height
                visible = true
            }
            if (maxPreviousHeight < height) {
                maxPreviousHeight = height
                visible = true
            }
            height to visible
        }
    }
}

private fun computeTreeScores(input: List<String>): List<Int> {
    return input.flatMapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, tree ->
            val height = tree.digitToInt()
            val left = minOf(colIndex, row.take(colIndex).reversed().takeWhile { it.digitToInt() < height }.length + 1)
            val right = minOf(row.length - colIndex - 1, row.drop(colIndex + 1).takeWhile { it.digitToInt() < height }.length + 1)
            val top = minOf(rowIndex, input.take(rowIndex).reversed().takeWhile { it[colIndex].digitToInt() < height }.size + 1)
            val bottom = minOf(input.size - rowIndex - 1, input.drop(rowIndex + 1).takeWhile { it[colIndex].digitToInt() < height }.size + 1)
            left * right * top * bottom
        }
    }
}

fun part1(input: List<String>): Int {
    val firstPass = firstPassFromEdge(input)
    val secondPass = secondPassFromEdge(firstPass)
    return secondPass.sumOf { row -> row.count { it.second } }
}

fun part2(input: List<String>): Int {
    val scores = computeTreeScores(input)

    return scores.max()
}

fun main() = AoCTask("day08").run {
    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    println(part1(input))
    println(part2(input))
}
