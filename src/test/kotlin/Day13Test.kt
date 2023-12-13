import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class Day13Test {

    @Nested
    inner class Day13RunnerTest {
        private val runner = Day13(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 405
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 400
        }
    }

}
