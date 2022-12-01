package day01

import AoCTask

// https://adventofcode.com/2022/day/1

fun part1(input: List<String>): Int {

    val elvesRations = getTotalRationsPerElf(input)

    return elvesRations.max()
}

fun part2(input: List<String>): Int {

    val elvesRations = getTotalRationsPerElf(input)

    return elvesRations.sortedDescending().take(3).sum()
}

private fun getTotalRationsPerElf(input: List<String>): List<Int> {
    val elves = input.joinToString("\n")
        .split("\n\n").map { elfRations ->
            elfRations.split("\n").sumOf { it.toInt() }
        }
    return elves
}

fun main() = AoCTask("day01").run {
    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    println(part1(input))
    println(part2(input))
}
