package de.mc.cmis.client

import org.apache.chemistry.opencmis.client.api.Repository
import org.apache.chemistry.opencmis.client.api.Session
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl
import org.apache.chemistry.opencmis.commons.PropertyIds
import org.apache.chemistry.opencmis.commons.SessionParameter
import org.apache.chemistry.opencmis.commons.enums.BindingType
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl
import java.io.ByteArrayInputStream
import java.math.BigInteger
import java.util.ArrayList
import java.util.HashMap

public class QueryTest {
    private var session: Session? = null

    private fun getCmisClientSession() {
        // default factory implementation
        val factory = SessionFactoryImpl.newInstance()
        val parameters = HashMap<String, String>()
        // user credentials
        parameters.put(SessionParameter.USER, "dummyuser")
        parameters.put(SessionParameter.PASSWORD, "dummysecret")
        // connection settings
        parameters.put(SessionParameter.ATOMPUB_URL, CMIS_ENDPOINT_TEST_SERVER)
        parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value())
        // create session
        session = factory.getRepositories(parameters).get(0).createSession<Session>()
    }

    throws(javaClass<Exception>())
    public fun createTestArea() {

        //creating a new folder
        val root = session!!.getRootFolder()
        val folderProperties = HashMap<String, Any>()
        folderProperties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder")
        folderProperties.put(PropertyIds.NAME, "testdata")

        val newFolder = root.createFolder(folderProperties)
        //create a new content in the folder
        val name = "testdata1.txt"
        // properties
        // (minimal set: name and object type id)
        val contentProperties = HashMap<String, Any>()
        contentProperties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document")
        contentProperties.put(PropertyIds.NAME, name)

        // content
        val content = "CMIS Testdata One".getBytes()
        val stream = ByteArrayInputStream(content)
        val contentStream = ContentStreamImpl(name, BigInteger(content), "text/plain", stream)

        // create a major version
        val newContent1 = newFolder.createDocument(contentProperties, contentStream, null)
        System.out.println("Document created: " + newContent1.getId())
    }

    private fun doQuery() {
        val results = session!!.query("SELECT * FROM cmis:folder WHERE cmis:name='testdata'", false)
        for (result in results) {
            val id = result.getPropertyValueById<String>(PropertyIds.OBJECT_ID)
            System.out.println("doQuery() found id: " + id)
        }
    }

    companion object {
        private val CMIS_ENDPOINT_TEST_SERVER = "http://localhost:8080/inmemory/atom"

        public fun main(args: Array<String>) {
            val o = QueryTest()
            try {
                o.getCmisClientSession()
                o.createTestArea()
                o.doQuery()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        public fun xmain(args: Array<String>) {

            System.out.println(javaClass<Hello>().getName() + " started")

            // Create a SessionFactory and set up the SessionParameter map
            val sessionFactory = SessionFactoryImpl.newInstance()
            val parameter = HashMap<String, String>()

            // connection settings - we're connecting to a public cmis repo,
            // using the AtomPUB binding
            parameter.put(SessionParameter.ATOMPUB_URL, "http://localhost:8080/inmemory/atom/")
            parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value())

            // find all the repositories at this URL - there should only be one.
            var repositories: List<Repository> = ArrayList()
            repositories = sessionFactory.getRepositories(parameter)
            for (r in repositories) {
                System.out.println("Found repository: " + r.getName())
            }

            // create session with the first (and only) repository
            val repository = repositories.get(0)
            parameter.put(SessionParameter.REPOSITORY_ID, repository.getId())
            val session = sessionFactory.createSession(parameter)

            System.out.println("Got a connection to repository: " + repository.getName() + ", with id: " + repository.getId())

            //        // Get everything in the root folder and print the names of the objects
            //        Folder root = session.getRootFolder();
            //        ItemIterable<CmisObject> children = root.getChildren();
            //        System.out.println("Found the following objects in the root folder:-");
            //        for (CmisObject o : children) {
            //            System.out.println(o.getName());
            //        }
            //
            System.out.println(javaClass<QueryTest>().getName() + " ended")
        }
    }
}