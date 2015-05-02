/*
 * Copyright (c) 2015 Mind Consulting UG(haftungsbeschränkt)
 */

package scripts
import org.apache.chemistry.opencmis.commons.enums.*
import org.apache.chemistry.opencmis.client.api.*

import java.util.concurrent.ExecutorService

cmis = new CMIS(session)

// destination folder
Folder destFolder = cmis.getFolder("/")

// source folder
String localPath = "/some/local/folder"

// upload folder tree
upload(destFolder, localPath)


//--------------------------------------------------

def upload(destination, String localPath,
        String folderType = "cmis:folder",
        String documentType = "cmis:document",
        VersioningState versioningState = VersioningState.MAJOR) {

    println "Uploading...\n"
    doUpload(destination, new File(localPath), folderType, documentType, versioningState)
    println "\n...done."
}

def doUpload(Folder parent, File folder, String folderType, String documentType, VersioningState versioningState) {
    folder.eachFile {
        if (it.name.startsWith(".")) {
            println "Skipping ${it.name}"
            return
        }

        println it.name

        if (it.isFile()) {
            cmis.createDocumentFromFile(parent, it, documentType, versioningState)
        }
        else if(it.isDirectory()) {
            Folder newFolder = cmis.createFolder(parent, it.name, folderType)
            doUpload(newFolder, it, folderType, documentType, versioningState)
        }
    }
}
