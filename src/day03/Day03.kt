package day03

import AoCTask

// https://adventofcode.com/2022/day/3

fun Char.priority() = when {
    isLowerCase() -> code - 97 + 1
    isUpperCase() -> code - 65 + 27
    else -> throw Exception("Invalid char '$this' to get priority for")
}

fun testPriorities() {
    ('a'..'z').toList().plus(('A'..'Z')).forEachIndexed { index, c ->
        val expectedPriority = index + 1
        check(expectedPriority == c.priority()) {
            "Expected priority $expectedPriority for '$c'(${c.code}), but got ${c.priority()}"
        }
    }
}

fun part1(input: List<String>): Int {
    val mispackedItems = input.map { rucksack ->
        val compartment1 = rucksack.substring(0 until  rucksack.length/2)
        val compartment2 = rucksack.substring((rucksack.length/2) until rucksack.length)
        check(compartment1.length == compartment2.length) {
            "Compartments are of unequal length: ${compartment1.length} != ${compartment2.length}"
        }

        val uniqueItemsInCompartment1 = compartment1.toSet()
        val uniqueItemsInCompartment2 = compartment2.toSet()

        uniqueItemsInCompartment1.intersect(uniqueItemsInCompartment2).first()
    }

    return mispackedItems.sumOf(Char::priority)
}

fun part2(input: List<String>): Int {
    val badges = input.windowed(3, step = 3).map { group ->
        group
            .map(String::toSet)
            .reduce(Set<Char>::intersect)
            .first()
    }
    return badges.sumOf(Char::priority)
}

fun main() = AoCTask("day03").run {
    testPriorities()
    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    println(part1(input))
    println(part2(input))
}
