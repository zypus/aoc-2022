package day04

import AoCTask

// https://adventofcode.com/2022/day/4

operator fun IntRange.contains(other: IntRange): Boolean {
    return other.first in this && other.last in this
}

private fun parseRangePairs(input: List<String>) = input.map { line ->
    line
        .split(",", limit = 2)
        .map { ranges ->
            val (from, to) = ranges.split("-").map { it.toInt() }
            from..to
        }
}

fun part1(input: List<String>): Int {
    val countOfFullyContained = parseRangePairs(input).count { ranges ->
        val (a,b) = ranges
        a in b || b in a
    }
    return countOfFullyContained
}

fun part2(input: List<String>): Int {
    val countOfOverlaps = parseRangePairs(input).count { ranges ->
        val (a,b) = ranges
        a.first in b || a.last in b || b.first in a || b.last in a
    }
    return countOfOverlaps
}

fun main() = AoCTask("day04").run {
    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    println(part1(input))
    println(part2(input))
}
