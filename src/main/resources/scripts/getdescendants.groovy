/*
 * Copyright (c) 2015 Mind Consulting UG(haftungsbeschränkt)
 */

package scripts
import org.apache.chemistry.opencmis.client.api.*

session.rootFolder.getDescendants(-1).each { printTree(it, 0) }

def printTree(Tree tree, int depth) {
    depth.times { print "  " }

    println tree.item.name

    tree.children.each { printTree(it, depth + 1) }
}