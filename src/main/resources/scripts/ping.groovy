/*
 * Copyright (c) 2015 Mind Consulting UG(haftungsbeschränkt)
 */

package scripts
import org.apache.chemistry.opencmis.client.api.*

CmisObject root = session.getRootFolder()

ping({ root.refresh() })




def ping(func, int sleepSeconds = 2, int turns = 100) {
    func()

    long total = 0
    long max = 0
    long min = Long.MAX_VALUE

    for (i in 1..turns) {
        long start = System.currentTimeMillis()
        func()
        long end = System.currentTimeMillis()

        long time = end - start
        total += time
        max = max < time ? time : max
        min = min > time ? time : min

        println String.format('[%1s] %2$5d: %3$5d ms   (min: %4$5d ms, max: %5$5d ms, avg: %6$7.1f ms)',
                (new Date(start)).format('yyyy-MM-dd hh:mm:ss'),
                i,
                time,
                min,
                max,
                total / i)

        sleep sleepSeconds * 1000
    }
}