import org.apache.chemistry.opencmis.client.api.Folder
import org.apache.chemistry.opencmis.commons.enums.VersioningState

cmis = new scripts.CMIS(session)

// destination folder
Folder destFolder = cmis.getFolder("/")

// upload folder tree
upload(destFolder)

//--------------------------------------------------

def upload(destination,
           String folderType = "cmis:folder",
           String documentType = "cmisbox:versionedDocument",
           VersioningState versioningState = VersioningState.MAJOR) {

    println "Uploading...\n"
    doUpload(destination, index + "", folderType, documentType, versioningState)
    println "\n...done."
}

def doUpload(Folder parent, String folder, String folderType, String documentType, VersioningState versioningState) {
    Folder threadFolder = cmis.createFolder(parent, folder, folderType)
    for (int i = 0; i < 100; i++) {
        Folder newFolder = cmis.createFolder(threadFolder, "folder_" + i, folderType)
        for (int j = 0; j < 10; j++) {
            Folder newFolder2 = cmis.createFolder(newFolder, "subfolder_" + i + "_" + j, folderType)
            cmis.createRandomContentDocument(newFolder,"document_" + i+ "_" + j ,(1024 * 1024* 4).toLong(), documentType, versioningState)
            Thread.sleep(0)
            for (int k = 0; k < 10; k++) {
                cmis.createFolder(newFolder2, "subfolder_" + i + "_" + j + "_" + k, folderType)
            }
            Thread.sleep(0)
        }

    }
}
