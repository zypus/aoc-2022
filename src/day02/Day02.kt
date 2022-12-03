package day02

import AoCTask

// https://adventofcode.com/2022/day/2

enum class RPSMove(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    infix fun vs(other: RPSMove) = when (other) {
        this -> RPSOutcome.DRAW
        this.beats() -> RPSOutcome.WIN
        else -> RPSOutcome.LOSS
    }

    fun beats() = when(this) {
        ROCK -> SCISSORS
        PAPER -> ROCK
        SCISSORS -> PAPER
    }

    fun beatenBy() = beats().beats()
}

enum class RPSOutcome(val score: Int) {
    LOSS(0),
    DRAW(3),
    WIN(6)
}

class RPSRound(val opponent: RPSMove, val me: RPSMove) {
    companion object {
        fun fromOpponentString(string: String): RPSRound {
            val (op, me) = string.split(" ")
            return RPSRound(
                opponent = when(op) {
                    "A" -> RPSMove.ROCK
                    "B" -> RPSMove.PAPER
                    else -> RPSMove.SCISSORS
                },
                me = when(me) {
                    "X" -> RPSMove.ROCK
                    "Y" -> RPSMove.PAPER
                    else -> RPSMove.SCISSORS
                }
            )
        }

        fun fromInstructionString(string: String): RPSRound {
            val (op, instruction) = string.split(" ")
            val opponent = when (op) {
                "A" -> RPSMove.ROCK
                "B" -> RPSMove.PAPER
                else -> RPSMove.SCISSORS
            }
            return RPSRound(
                opponent = opponent,
                me = when(instruction) {
                    "X" -> opponent.beats()
                    "Y" -> opponent
                    else -> opponent.beatenBy()
                }
            )
        }
    }

    fun outcome() = me vs opponent

    fun score() = outcome().score + me.score
}

fun part1(input: List<String>): Int {
    val totalScore = input.map {
        RPSRound.fromOpponentString(it)
    }.sumOf {
        it.score()
    }
    return totalScore
}

fun part2(input: List<String>): Int {
    val totalScore = input.map {
        RPSRound.fromInstructionString(it)
    }.sumOf {
        it.score()
    }
    return totalScore
}

fun main() = AoCTask("day02").run {
    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    println(part1(input))
    println(part2(input))
}
