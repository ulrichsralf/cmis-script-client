/*
 * Copyright (c) 2015 Mind Consulting UG(haftungsbeschränkt)
 */

package de.mc.cmis.client.util

import java.io.IOException
import java.io.InputStream
import java.util.*

public class RandomInputStream(private val size: Long) : InputStream() {

    private val random = Random()
    private var isClosed = false
    private var bytesRead: Long = 0


    override fun read(): Int {
        checkOpen()
        val len = checkLimit(1)
        if (len > 0) {
            var result = "CMIS ".get(random.nextInt(5)).toInt()
            bytesRead++
            return result
        } else {
            return -1
        }
    }


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
