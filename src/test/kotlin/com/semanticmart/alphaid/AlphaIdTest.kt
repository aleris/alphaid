package com.semanticmart.alphaid

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test
import java.util.Random
import java.util.regex.Pattern

internal class AlphaIdTest {
    @Test
    fun `generates an id with defaults`() {
        val id = AlphaId.randomId()
        println("id=$id")
        assertThat(id).hasSize(AlphaId.DEFAULT_LENGTH)
    }

    @Test
    fun `generates an id specifying the length`() {
        val id = AlphaId.randomId(21)
        println("id=$id")
        assertThat(id).hasSize(21)
    }

    @Test
    fun `generates ids repeatedly with a custom alphabet`() {
        val alphaId = AlphaId(alphabet = "xyz".toCharArray())
        val id1 = alphaId.next(10)
        println("id1=$id1")
        assertThat(id1).hasSize(10)
        val id2 = alphaId.next(10)
        println("id2=$id1")
        assertThat(id2).hasSize(10)
        assertThat(id1).isNotEqualTo(id2)
    }

    @Test
    fun `generates ids repeatedly with a fast random generator specifying the seed`() {
        val alphaId = AlphaId(randomGenerator = Random(123))
        val id1 = alphaId.next(10)
        println("id1=$id1")
        assertThat(id1).hasSize(10)
        val id2 = alphaId.next(10)
        println("id2=$id2")
        assertThat(id2).hasSize(10)
        assertThat(id1).isNotEqualTo(id2)
    }

    @Test
    fun `generates same ids with provided seed`() {
        val randomGenerator = Random(12345)
        val randomId = AlphaId(randomGenerator = randomGenerator)

        val expectedIds = arrayOf(
            "kutqLNv1wDmIS56EcT3j7",
            "U497UttnWzKWWRPMHpLD7",
            "7nj2dWW1gjKLtgfzeI8eC",
            "I6BXYvyjszq6xV7L9k2A9",
            "uIolcQEyyQIcn3iM6Odoa",
        )

        for (expectedId in expectedIds) {
            val generatedId: String = randomId.next(21)
            assertThat(generatedId).isEqualTo(expectedId)
        }
    }

    @Test
    fun `generates ids with alphabets of any size from 1 to 255`() {
        fun buildRegexForAlphabet(alphabet: CharArray): Regex {
            val patternBuilder = StringBuilder()
            patternBuilder.append("^[")
            for (character in alphabet) {
                patternBuilder.append(Pattern.quote(character.toString()))
            }
            patternBuilder.append("]+$")
            return Regex(patternBuilder.toString())
        }

        for (symbolCount in 1..255) {
            val alphabet = CharArray(symbolCount)
            for (i in 0 until symbolCount) {
                alphabet[i] = i.toChar()
            }
            val id: String = AlphaId(alphabet = alphabet).next(100)

            val regexForAlphabet = buildRegexForAlphabet(alphabet)
            assertThat(id.matches(regexForAlphabet))
                .withFailMessage("$id didn't match $regexForAlphabet")
                .isTrue
        }
    }

    @Test
    fun `generates correct id with sizes between 1 and 1024`() {
        for (size in 1..1024) {
            val id: String = AlphaId.randomId(size)
            assertThat(id).hasSize(size)
        }
    }

    @Test
    fun `generates correct id with max size`() {
        val max = Short.MAX_VALUE.toInt()
        assertThat(AlphaId.randomId(max)).hasSize(max)
    }

    @Test
    fun `generates unique ids`() {
        val idCount = 1_000_000
        val ids = HashSet<String>(idCount)

        for (i in 0 until idCount) {
            val id: String = AlphaId.randomId(10)
            assertThat(ids.contains(id)).withFailMessage("Non unique id generated $id").isFalse
            ids.add(id)
        }
    }

    @Test
    fun `generated ids have symbols well distributed`() {
        val idCount = 1_000_000
        val alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray()

        fun assertThatDistributionIsApproxEven(charCounts: HashMap<Char, Long>) {
            for (charCount in charCounts.values) {
                val distribution = charCount * alphabet.size / (idCount * AlphaId.DEFAULT_LENGTH).toDouble()
                assertThat(distribution).isCloseTo(1.0, Offset.offset(0.05))
            }
        }

        val charCounts = HashMap<Char, Long>()

        val randomId = AlphaId(alphabet = alphabet)
        for (i in 0 until idCount) {
            val id = randomId.next()
            for (symbol in id) {
                charCounts.compute(symbol) { _, v -> if (v == null) 1L else v + 1 }
            }
        }

        assertThatDistributionIsApproxEven(charCounts)
    }

    @Test
    fun `providing a non positive size throws an exception`() {
        Assertions.assertThatThrownBy { AlphaId.randomId(0) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `providing a size more than max throws an exception`() {
        Assertions.assertThatThrownBy { AlphaId.randomId(Short.MAX_VALUE + 1) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `providing an empty alphabet throws an exception`() {
        Assertions.assertThatThrownBy { AlphaId(alphabet = charArrayOf()) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `providing an alphabet bigger than 255 throws an exception`() {
        Assertions.assertThatThrownBy { AlphaId(alphabet = "x".repeat(256).toCharArray()) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
