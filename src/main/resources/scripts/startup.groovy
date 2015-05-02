/*
 * Copyright (c) 2015 Mind Consulting UG(haftungsbeschränkt)
 */

package scripts
import org.apache.chemistry.opencmis.client.api.*

// variable 'session' is bound to the current OpenCMIS session


// print the repository name - Java style
println "Repository: " + session.getRepositoryInfo().getName()

// print the repository name - Groovy style
println "Repository: ${session.repositoryInfo.name}"


// get root folder
Folder root = session.getRootFolder()
println "--- Root Folder: " + root.getName() + " ---"

// print root folder children
for(CmisObject object: root.getChildren()) {
    println object.getName() + " \t(" + object.getType().getId() + ")"
}

// run a quick query
for(QueryResult hit: session.query("SELECT * FROM cmis:document", false)) {
    hit.properties.each{ println "${it.queryName}: ${it.firstValue}" }
    println "----------------------------------"
}

// CMIS helper script
def cmis = new CMIS(session)

cmis.printProperties "/"                    // access by path
cmis.printProperties session.rootFolder.id  // access by id
cmis.printProperties session.rootFolder     // access by object

// Folder folder = cmis.createFolder("/", "test-folder", "cmis:folder")

// Document doc = cmis.createTextDocument(folder, "test.txt", "Hello World!", "cmis:document")
// cmis.printProperties doc
// cmis.download(doc, "/some/path/helloWorld.txt")
// cmis.delete doc.id

// cmis.delete folder


// see /org.apache.chemistry.opencmis.scripts/CMIS.groovy for more methods