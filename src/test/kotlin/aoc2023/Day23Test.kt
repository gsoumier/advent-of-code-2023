package aoc2023

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day23Test {

    @Nested
    inner class Day23RunnerTest {
        private val runner = Day23(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 94
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 154
        }
    }

}
