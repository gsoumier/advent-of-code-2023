package aoc2024

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day1Test {

    @Nested
    inner class Day1RunnerTest {
        private val runner = Day1(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 11
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 31
        }
    }

}
