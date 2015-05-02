/*
 * Copyright (c) 2015 Mind Consulting UG(haftungsbeschränkt)
 */

package scripts
import org.apache.chemistry.opencmis.client.api.*

cmis = new CMIS(session)

// destination folder
File localFolder = new File("/some/local/folder")

// source folder
Folder sourceFolder = cmis.getFolder("/folder/to/download")

// download folder tree
download(localFolder, sourceFolder)


//--------------------------------------------------

def download(File localFolder, Folder sourceFolder) {
    println "\nDownloading ${sourceFolder.name} to ${localFolder.absolutePath}\n"

    sourceFolder.children.each { child ->
        File newFile = new File(localFolder, child.name)

        if (child instanceof Folder) {
            println "[Folder] ${newFile.name}"
            newFile.mkdir()

            download(newFile, child)
        }
        else if (child instanceof Document) {
            println "[File]   ${newFile.name}"
            cmis.download(child, newFile)
        }
    }
}