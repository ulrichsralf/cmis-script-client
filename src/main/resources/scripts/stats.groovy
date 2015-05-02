/*
 * Copyright (c) 2015 Mind Consulting UG(haftungsbeschränkt)
 */

package scripts
import org.apache.chemistry.opencmis.client.api.*

Folder folder = (Folder) session.getObjectByPath("/")


def stats = count(folder, true)

println "Folder ${folder.name}"
println "----------------------------------------------"
println "Folders:   ${stats['folders']}"
println "Documents: ${stats['documents']}"
println "Items:     ${stats['items']}"
println "Policies:  ${stats['policies']}"
println "Content:   ${stats['bytes']} bytes"



def count(Folder folder, boolean tree = false) {
    def stats = [:]
    stats["folders"] = 0
    stats["documents"] = 0
    stats["items"] = 0
    stats["policies"] = 0
    stats["bytes"] = 0

    OperationContext oc = session.createOperationContext()
    oc.setFilterString("cmis:objectId,cmis:contentStreamLength")
    oc.setIncludeAllowableActions(false)
    oc.setMaxItemsPerPage(10000)

    countInternal(folder, tree, oc, stats)

    stats
}

def countInternal(Folder folder, boolean tree, OperationContext oc, def stats) {
    folder.getChildren(oc).each { child ->
        if (child instanceof Folder) {
            stats["folders"]++
            if (tree) {
                countInternal(child, true, oc, stats)
            }
        } else if (child instanceof Document) {
            stats["documents"]++
            long size = ((Document) child).getContentStreamLength()
            if (size > 0) {
                stats["bytes"] += size
            }
        } else if (child instanceof Item) {
            stats["item"]++
        } else if (child instanceof Policy) {
            stats["policies"]++
        }
    }
}