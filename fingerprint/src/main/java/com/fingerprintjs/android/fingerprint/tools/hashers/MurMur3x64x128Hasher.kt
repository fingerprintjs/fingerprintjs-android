package com.fingerprintjs.android.fingerprint.tools.hashers


import java.nio.ByteBuffer
import java.nio.ByteOrder


// Converted to Kotlin from https://github.com/sangupta/murmur/blob/master/src/main/java/com/sangupta/murmur/Murmur3.java
public class MurMur3x64x128Hasher :
    Hasher {
    override fun hash(data: String): String {
        val hashResult = hash_x64_128(data.toByteArray(Charsets.US_ASCII), data.length)
        val hashSb = StringBuilder()
        hashResult.forEach {
            hashSb.append(java.lang.Long.toHexString(it))
        }
        return hashSb.toString()
    }

    private val X64_128_C1 = -0x783c846eeebdac2bL
    private val X64_128_C2 = 0x4cf5ad432745937fL

    private fun hash_x64_128(data: ByteArray, length: Int, seed: Long = 0L): LongArray {
        var h1 = seed
        var h2 = seed
        val buffer = ByteBuffer.wrap(data)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        while (buffer.remaining() >= 16) {
            val k1 = buffer.long
            val k2 = buffer.long
            h1 = h1 xor mixK1(k1)
            h1 = java.lang.Long.rotateLeft(h1, 27)
            h1 += h2
            h1 = h1 * 5 + 0x52dce729
            h2 = h2 xor mixK2(k2)
            h2 = java.lang.Long.rotateLeft(h2, 31)
            h2 += h1
            h2 = h2 * 5 + 0x38495ab5
        }

        buffer.compact()
        buffer.flip()
        val remaining = buffer.remaining()
        if (remaining > 0) {
            var k1: Long = 0
            var k2: Long = 0
            when (buffer.remaining()) {
                15 -> {
                    k2 = k2 xor ((buffer[14].toLong() and UNSIGNED_MASK) shl 48)
                    k2 = k2 xor ((buffer[13].toLong() and UNSIGNED_MASK) shl 40)
                    k2 = k2 xor ((buffer[12].toLong() and UNSIGNED_MASK) shl 32)
                    k2 = k2 xor ((buffer[11].toLong() and UNSIGNED_MASK) shl 24)
                    k2 = k2 xor ((buffer[10].toLong() and UNSIGNED_MASK) shl 16)
                    k2 = k2 xor ((buffer[9].toLong() and UNSIGNED_MASK) shl 8)
                    k2 = k2 xor (buffer[8].toLong() and UNSIGNED_MASK)
                    k1 = k1 xor buffer.long
                }
                14 -> {
                    k2 = k2 xor ((buffer[13].toLong() and UNSIGNED_MASK) shl 40)
                    k2 = k2 xor ((buffer[12].toLong() and UNSIGNED_MASK) shl 32)
                    k2 = k2 xor ((buffer[11].toLong() and UNSIGNED_MASK) shl 24)
                    k2 = k2 xor ((buffer[10].toLong() and UNSIGNED_MASK) shl 16)
                    k2 = k2 xor ((buffer[9].toLong() and UNSIGNED_MASK) shl 8)
                    k2 = k2 xor (buffer[8].toLong() and UNSIGNED_MASK)
                    k1 = k1 xor buffer.long
                }
                13 -> {
                    k2 = k2 xor ((buffer[12].toLong() and UNSIGNED_MASK) shl 32)
                    k2 = k2 xor ((buffer[11].toLong() and UNSIGNED_MASK) shl 24)
                    k2 = k2 xor ((buffer[10].toLong() and UNSIGNED_MASK) shl 16)
                    k2 = k2 xor ((buffer[9].toLong() and UNSIGNED_MASK) shl 8)
                    k2 = k2 xor (buffer[8].toLong() and UNSIGNED_MASK)
                    k1 = k1 xor buffer.long
                }
                12 -> {
                    k2 = k2 xor ((buffer[11].toLong() and UNSIGNED_MASK) shl 24)
                    k2 = k2 xor ((buffer[10].toLong() and UNSIGNED_MASK) shl 16)
                    k2 = k2 xor ((buffer[9].toLong() and UNSIGNED_MASK) shl 8)
                    k2 = k2 xor (buffer[8].toLong() and UNSIGNED_MASK)
                    k1 = k1 xor buffer.long
                }
                11 -> {
                    k2 = k2 xor ((buffer[10].toLong() and UNSIGNED_MASK) shl 16)
                    k2 = k2 xor ((buffer[9].toLong() and UNSIGNED_MASK) shl 8)
                    k2 = k2 xor (buffer[8].toLong() and UNSIGNED_MASK)
                    k1 = k1 xor buffer.long
                }
                10 -> {
                    k2 = k2 xor ((buffer[9].toLong() and UNSIGNED_MASK) shl 8)
                    k2 = k2 xor (buffer[8].toLong() and UNSIGNED_MASK)
                    k1 = k1 xor buffer.long
                }
                9 -> {
                    k2 = k2 xor (buffer[8].toLong() and UNSIGNED_MASK)
                    k1 = k1 xor buffer.long
                }
                8 -> k1 = k1 xor buffer.long
                7 -> {
                    k1 = k1 xor ((buffer[6].toLong() and UNSIGNED_MASK) shl 48)
                    k1 = k1 xor ((buffer[5].toLong() and UNSIGNED_MASK) shl 40)
                    k1 = k1 xor ((buffer[4].toLong() and UNSIGNED_MASK) shl 32)
                    k1 = k1 xor ((buffer[3].toLong() and UNSIGNED_MASK) shl 24)
                    k1 = k1 xor ((buffer[2].toLong() and UNSIGNED_MASK) shl 16)
                    k1 = k1 xor ((buffer[1].toLong() and UNSIGNED_MASK) shl 8)
                    k1 = k1 xor (buffer[0].toLong() and UNSIGNED_MASK)
                }
                6 -> {
                    k1 = k1 xor ((buffer[5].toLong() and UNSIGNED_MASK) shl 40)
                    k1 = k1 xor ((buffer[4].toLong() and UNSIGNED_MASK) shl 32)
                    k1 = k1 xor ((buffer[3].toLong() and UNSIGNED_MASK) shl 24)
                    k1 = k1 xor ((buffer[2].toLong() and UNSIGNED_MASK) shl 16)
                    k1 = k1 xor ((buffer[1].toLong() and UNSIGNED_MASK) shl 8)
                    k1 = k1 xor (buffer[0].toLong() and UNSIGNED_MASK)
                }
                5 -> {
                    k1 = k1 xor ((buffer[4].toLong() and UNSIGNED_MASK) shl 32)
                    k1 = k1 xor ((buffer[3].toLong() and UNSIGNED_MASK) shl 24)
                    k1 = k1 xor ((buffer[2].toLong() and UNSIGNED_MASK) shl 16)
                    k1 = k1 xor ((buffer[1].toLong() and UNSIGNED_MASK) shl 8)
                    k1 = k1 xor (buffer[0].toLong() and UNSIGNED_MASK)
                }
                4 -> {
                    k1 = k1 xor ((buffer[3].toLong() and UNSIGNED_MASK) shl 24)
                    k1 = k1 xor ((buffer[2].toLong() and UNSIGNED_MASK) shl 16)
                    k1 = k1 xor ((buffer[1].toLong() and UNSIGNED_MASK) shl 8)
                    k1 = k1 xor (buffer[0].toLong() and UNSIGNED_MASK)
                }
                3 -> {
                    k1 = k1 xor ((buffer[2].toLong() and UNSIGNED_MASK) shl 16)
                    k1 = k1 xor ((buffer[1].toLong() and UNSIGNED_MASK) shl 8)
                    k1 = k1 xor (buffer[0].toLong() and UNSIGNED_MASK)
                }
                2 -> {
                    k1 = k1 xor ((buffer[1].toLong() and UNSIGNED_MASK) shl 8)
                    k1 = k1 xor (buffer[0].toLong() and UNSIGNED_MASK)
                }
                1 -> k1 = k1 xor (buffer[0].toLong() and UNSIGNED_MASK)
                else -> throw AssertionError("Code should not reach here!")
            }

            // mix
            h1 = h1 xor mixK1(k1)
            h2 = h2 xor mixK2(k2)
        }

        h1 = h1 xor length.toLong()
        h2 = h2 xor length.toLong()
        h1 += h2
        h2 += h1
        h1 = fmix64(h1)
        h2 = fmix64(h2)
        h1 += h2
        h2 += h1
        return longArrayOf(h1, h2)
    }

    private fun mixK1(k1Prev: Long): Long {
        var k1 = k1Prev
        k1 *= X64_128_C1
        k1 = java.lang.Long.rotateLeft(k1, 31)
        k1 *= X64_128_C2
        return k1
    }

    private fun mixK2(k2Prev: Long): Long {
        var k2 = k2Prev
        k2 *= X64_128_C2
        k2 = java.lang.Long.rotateLeft(k2, 33)
        k2 *= X64_128_C1
        return k2
    }

    private fun fmix64(kPrev: Long): Long {
        var k = kPrev
        k = k xor (k ushr 33)
        k *= -0xae502812aa7333L
        k = k xor (k ushr 33)
        k *= -0x3b314601e57a13adL
        k = k xor (k ushr 33)
        return k
    }
}

private const val UNSIGNED_MASK = 0xffL