package com.tcubedstudios.angularstudio.shared.libs.shopobot

import java.io.UnsupportedEncodingException
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.IllegalCharsetNameException
import java.nio.charset.UnsupportedCharsetException

// Pulled from:
//https://www.javatips.net/api/compliance-master/ctk-transport/src/main/java/org/ga4gh/ctk/transport/FragmentEncoder.java
/**
 * This class is used to encode a string using the format required by [RFC 3986](http://tools.ietf.org/html/rfc3986).
 *
 * @author [julius schorzman](https://github.com/juliuss)
 */
object FragmentEncoder {
    const val digits = "0123456789ABCDEF"

    /**
     * Encodes the given string `s` in a x-www-form-urlencoded string using
     * the specified encoding scheme `enc`.
     *
     *
     * All characters except letters ('a'..'z', 'A'..'Z') and numbers ('0'..'9')
     * and characters '.', '-', '*', '_', '!', '$', '&', ''', '(', ')', '*', '+',
     * ',', ';', '=', '~', ':', '@', '/', '?' are converted into their hexadecimal
     * value prepended by '%'. For example: '#' -> %23. In addition, spaces are
     * substituted by '+'
     *
     * @param s
     * the string to be encoded.
     * @param enc
     * the encoding scheme to be used.
     * @return the encoded string.
     * @throws UnsupportedEncodingException
     * if the specified encoding scheme is invalid.
     */
    @JvmStatic
    @Throws(UnsupportedEncodingException::class)
    fun encode(s: String?, enc: String?): String {
        if (s == null || enc == null) {
            throw NullPointerException()
        }
        // check for UnsupportedEncodingException
        "".toByteArray(charset(enc))

        // Guess a bit bigger for encoded form
        val buf = StringBuilder(s.length + 16)
        var start = -1
        for (i in 0 until s.length) {
            val ch = s[i]
            if (((ch >= 'a' && ch <= 'z' || ch >= 'A') && ch <= 'Z' || ch >= '0') && ch <= '9' || " .-*_!$&'()+,;=~:@/?".indexOf(ch) > -1) {
                if (start >= 0) {
                    convert(s.substring(start, i), buf, enc)
                    start = -1
                }
                if (ch != ' ') {
                    buf.append(ch)
                } else {
                    buf.append('+')
                }
            } else {
                if (start < 0) {
                    start = i
                }
            }
        }
        if (start >= 0) {
            convert(s.substring(start, s.length), buf, enc)
        }
        return buf.toString()
    }

    @Throws(UnsupportedEncodingException::class)
    private fun convert(s: String, buf: StringBuilder, enc: String) {
        val bytes = s.toByteArray(charset(enc))
        for (j in bytes.indices) {
            buf.append('%')
            buf.append(digits[bytes[j].toInt() and 0xf0 shr 4])
            buf.append(digits[bytes[j].toInt() and 0xf])
        }
    }

    /**
     * Decodes the argument which is assumed to be encoded in the `x-www-form-urlencoded` MIME content type using the specified encoding
     * scheme.
     *
     *
     * '+' will be converted to space, '%' and two following hex digit
     * characters are converted to the equivalent byte value. All other
     * characters are passed through unmodified. For example "A+B+C %24%25" ->
     * "A B C $%".
     *
     * @param s
     * the encoded string.
     * @param encoding
     * the encoding scheme to be used.
     * @return the decoded clear-text representation of the given string.
     * @throws UnsupportedEncodingException
     * if the specified encoding scheme is invalid.
     */
    @Throws(UnsupportedEncodingException::class)
    fun decode(s: String, encoding: String?): String {
        if (encoding == null) {
            throw NullPointerException()
        }
        if (encoding.isEmpty()) {
            throw UnsupportedEncodingException(encoding)
        }
        if (s.indexOf('%') == -1) {
            if (s.indexOf('+') == -1) return s
            val str = s.toCharArray()
            for (i in str.indices) {
                if (str[i] == '+') str[i] = ' '
            }
            return String(str)
        }
        var charset: Charset? = null
        charset = try {
            Charset.forName(encoding)
        } catch (e: IllegalCharsetNameException) {
            throw (UnsupportedEncodingException(
                encoding
            ).initCause(e) as UnsupportedEncodingException)
        } catch (e: UnsupportedCharsetException) {
            throw (UnsupportedEncodingException(
                encoding
            ).initCause(e) as UnsupportedEncodingException)
        }
        return decode(s, charset)
    }

    private fun decode(s: String, charset: Charset?): String {
        val str_buf = CharArray(s.length)
        val buf = ByteArray(s.length / 3)
        var buf_len = 0
        var i = 0
        while (i < s.length) {
            val c = s[i]
            if (c == '+') {
                str_buf[buf_len] = ' '
            } else if (c == '%') {
                var len = 0
                do {
                    require(i + 2 < s.length) { "Incomplete % sequence at: $i" }
                    val d1 = s[i + 1].digitToIntOrNull(16) ?: -1
                    val d2 = s[i + 2].digitToIntOrNull(16) ?: -1
                    require(!(d1 == -1 || d2 == -1)) {
                        ("Invalid % sequence "
                                + s.substring(i, i + 3)
                                + " at " + i)
                    }
                    buf[len++] = ((d1 shl 4) + d2).toByte()
                    i += 3
                } while (i < s.length && s[i] == '%')
                val cb = charset!!.decode(ByteBuffer.wrap(buf, 0, len))
                len = cb.length
                System.arraycopy(cb.array(), 0, str_buf, buf_len, len)
                buf_len += len
                continue
            } else {
                str_buf[buf_len] = c
            }
            i++
            buf_len++
        }
        return String(str_buf, 0, buf_len)
    }
}