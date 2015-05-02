/*
 * Copyright (c) 2015 Mind Consulting UG(haftungsbeschränkt)
 */

package scripts
import org.apache.chemistry.opencmis.client.api.*

String cql = "SELECT cmis:objectId, cmis:name, cmis:contentStreamLength FROM cmis:document"

ItemIterable<QueryResult> results = session.query(cql, false)

//ItemIterable<QueryResult> results = session.query(cql, false).getPage(10)
//ItemIterable<QueryResult> results = session.query(cql, false).skipTo(10).getPage(10)

results.each { hit ->
    hit.properties.each { println "${it.queryName}: ${it.firstValue}" }
    println "--------------------------------------"
}

println "--------------------------------------"
println "Total number: ${results.totalNumItems}"
println "Has more: ${results.hasMoreItems}"
println "--------------------------------------"