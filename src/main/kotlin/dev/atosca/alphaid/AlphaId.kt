package dev.atosca.alphaid

import org.apache.commons.math3.util.FastMath
import java.security.SecureRandom
import java.util.Random
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Random pseudo-unique string id generator with a default web friendly alphabet.
 *
 * **Sample Usage**
 *
 * ```kotlin
 * val id = AlphaId.randomId()
 * // id is something like `DVUQIrcvdhuAZhEWYapF58j4_m-5pT6j`
 *
 *
 * val id = AlphaId.randomId(4)
 * // id is something like `5Osn`
 *
 *
 * val alphaId = AlphaId(alphabet = "xyz".toCharArray())
 * val id1 = alphaId.next(10)
 * // id1 is something like `xxzxzyyyzy`
 * val id2 = alphaId.next(10)
 * // id2 is something like `yyyxyzzzxz`
 *
 *
 * val alphaId = AlphaId(randomGenerator = Random(123))
 * val id1 = alphaId.next(10)
 * // id1 is `rLvTj2WWfa`
 * val id2 = alphaId.next(10)
 * // id2 is `kfl_MAUg3l`
 * ```
 *
 * @param alphabet the set of chars used to generate ids; defaults to [DEFAULT_ALPHABET].
 * @param randomGenerator the random generator used to generate random bytes; defaults to [DEFAULT_RANDOM_GENERATOR].
 * @constructor creates an AlphaId instance with a specified alphabet and/or random generator which can be used to
 * generate ids repeatedly.
 */
class AlphaId(
    private val alphabet: CharArray = DEFAULT_ALPHABET,
    private val randomGenerator: Random = DEFAULT_RANDOM_GENERATOR,
) {
    init {
        if (alphabet.isEmpty() || 255 < alphabet.size) {
            throw IllegalArgumentException("alphabet size must be at least 1 and at most 255")
        }
    }

    /**
     * Generate an id [String] with a specified size.
     *
     * @param length the length of the generated string id, defaults to [DEFAULT_LENGTH].
     * @return the generated [String] id.
     */
    fun next(length: Int = DEFAULT_LENGTH): String {
        if (length <= 0 || Short.MAX_VALUE < length) {
            throw IllegalArgumentException("length must be at least 1 and at most ${Short.MAX_VALUE}")
        }
        // https://github.com/aventrix/jnanoid/blob/develop/src/main/java/com/aventrix/jnanoid/jnanoid/NanoIdUtils.java#L109
        val mask = (2 shl floor(FastMath.log(alphabet.size.toDouble() - 1) / LOG_OF_2).toInt()) - 1
        val step = ceil(1.6 * mask * length / alphabet.size).toInt()
        val idBuilder = StringBuilder(length)
        val bytes = ByteArray(step)
        while (true) {
            randomGenerator.nextBytes(bytes)
            for (i in 0 until step) {
                val alphabetIndex = bytes[i].toInt() and mask
                if (alphabetIndex < alphabet.size) {
                    idBuilder.append(alphabet[alphabetIndex])
                    if (idBuilder.length == length) {
                        return idBuilder.toString()
                    }
                }
            }
        }
    }

    companion object {
        private val LOG_OF_2 = FastMath.log(2.0)

        /**
         * Default length used when generating ids without specifying an explicit length.
         */
        const val DEFAULT_LENGTH = 32

        /**
         * Set of chars used for the default id generation.
         */
        val DEFAULT_ALPHABET = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

        /**
         * The default [Random] used to generate ids.
         */
        val DEFAULT_RANDOM_GENERATOR = SecureRandom()

        private val DEFAULT = AlphaId()

        /**
         * Generate an id using the default alphabet [DEFAULT_ALPHABET] with the default length [DEFAULT_LENGTH] and
         * using the default random generator [DEFAULT_RANDOM_GENERATOR].
         */
        fun randomId() = DEFAULT.next()

        /**
         * Generate an id with the specified length using the default alphabet [DEFAULT_ALPHABET] and the default
         * random generator [DEFAULT_RANDOM_GENERATOR].
         *
         * @param length the length of the generated string id.
         */
        fun randomId(length: Int) = DEFAULT.next(length)
    }
}
