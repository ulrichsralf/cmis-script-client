/*
 * Copyright (c) 2015 Mind Consulting UG(haftungsbeschränkt)
 */

package de.mc.cmis.client.util

import java.io.IOException
import java.io.InputStream
import java.util.Random

public class RandomInputStream(private val size: Long) : InputStream() {

    private val random = Random()
    private var isClosed = false
    private var bytesRead: Long = 0

    throws(javaClass<IOException>())
    override fun read(): Int {
        checkOpen()
        val len = checkLimit(1)
        if (len > 0) {
            var result = random.nextInt() % 256
            if (result < 0) result = -result
            bytesRead++
            return result
        } else {
            return -1
        }
    }

    throws(javaClass<IOException>())
    override fun read(data: ByteArray, offset: Int, length: Int): Int {
        checkOpen()
        val sizeToRead = checkLimit(length.toLong()).toInt()
        if (0 == sizeToRead) return -1
        val temp = ByteArray(sizeToRead)
        random.nextBytes(temp)
        System.arraycopy(temp, 0, data, offset, sizeToRead)
        bytesRead += length.toLong()
        return length
    }

    throws(javaClass<IOException>())
    override fun read(data: ByteArray): Int {
        checkOpen()
        val len = checkLimit(data.size().toLong())
        if (0L == len) return -1
        random.nextBytes(data)
        bytesRead += len
        return len.toInt()
    }

    throws(javaClass<IOException>())
    override fun skip(bytesToSkip: Long): Long {
        checkOpen()
        val skip = checkLimit(bytesToSkip)
        bytesRead += skip
        return skip
    }

    override fun close() {
        this.isClosed = true
    }

    override fun available(): Int {
        return (size - bytesRead).toInt()
    }

    throws(javaClass<IOException>())
    private fun checkOpen() {
        if (isClosed) throw IOException("RandomInputStream was already closed.")
    }

    private fun checkLimit(sizeRequested: Long): Long {
       return  if (bytesRead >= size) {
             0
        } else if (bytesRead + sizeRequested >= size) {
             size - bytesRead
        } else {
             sizeRequested
        }
    }

}
