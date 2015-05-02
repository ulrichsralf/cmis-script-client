/*
 * Copyright (c) 2015 Mind Consulting UG(haftungsbeschränkt)
 */

package scripts
import org.apache.chemistry.opencmis.client.api.*

println "'cmis:document' and subtypes:     ${countTypes('cmis:document')}"
println "'cmis:item' and subtypes:         ${countTypes('cmis:item')}"
println "'cmis:folder' and subtypes:       ${countTypes('cmis:folder')}"
println "'cmis:relationship' and subtypes: ${countTypes('cmis:relationship')}"
println "'cmis:policy' and subtypes:       ${countTypes('cmis:policy')}"



int countTypes(String typeId) {
    def counter = 0

    try {
        session.getTypeDescendants(typeId, -1, false).each { counter += 1 + count(it) }
        counter++
    }
    catch (any) {
    }

    counter
}

int count(Tree tree) {
    def counter = 0
    tree.children.each { counter += 1 + count(it) }

    counter
}