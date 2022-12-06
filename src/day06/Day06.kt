package day06

import AoCTask

// https://adventofcode.com/2022/day/6

fun Sequence<Char>.charactersReadToFindSequenceOfDistinctSize(size: Int): Int {
    val index = this
        .windowed(size, step = 1)
        .indexOfFirst {
            it.toSet().size == size
        }
    return index + size
}

fun part1(input: List<String>): Int {
    val result = input
        .first()
        .asSequence()
        .charactersReadToFindSequenceOfDistinctSize(4)
    return result
}

fun part2(input: List<String>): Int {
    val result = input
        .first()
        .asSequence()
        .charactersReadToFindSequenceOfDistinctSize(14)
    return result
}

fun main() = AoCTask("day06").run {
    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 7)
    check(part1(readTestInput(2)) == 5)
    check(part1(readTestInput(3)) == 6)
    check(part1(readTestInput(4)) == 10)
    check(part1(readTestInput(5)) == 11)

    check(part2(testInput) == 19)
    check(part2(readTestInput(2)) == 23)
    check(part2(readTestInput(3)) == 23)
    check(part2(readTestInput(4)) == 29)
    check(part2(readTestInput(5)) == 26)

    println(part1(input))
    println(part2(input))
}
