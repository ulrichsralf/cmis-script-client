/*
 * Copyright (c) 2015 Mind Consulting UG(haftungsbeschränkt)
 */
package de.mc.cmis.client

import org.apache.chemistry.opencmis.client.api.Session
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl
import org.apache.chemistry.opencmis.commons.SessionParameter
import org.apache.chemistry.opencmis.commons.enums.BindingType
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.Date
import java.util.HashMap
import java.util.Properties
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.script.ScriptEngineManager

public class ScriptClient {
    companion object {
        public fun main(args: Array<String>) {

            println("client started")
            val props = Properties()
            props.load(ScriptClient.javaClass.getResourceAsStream("/scriptclient.properties"))
            val sessionFactory = SessionFactoryImpl.newInstance()

            val parameter = HashMap<String, String>()
            parameter.put(SessionParameter.BINDING_TYPE, BindingType.BROWSER.value())
            parameter.put(SessionParameter.BROWSER_URL, props.getProperty("cmisurl"))
            parameter.put(SessionParameter.USER, props.getProperty("cmisuser"))
            parameter.put(SessionParameter.PASSWORD, props.getProperty("cmispassword"))
            parameter.put(SessionParameter.AUTH_HTTP_BASIC, "true")
            parameter.put(SessionParameter.AUTH_SOAP_USERNAMETOKEN, "true")
            parameter.put(SessionParameter.COOKIES, "true")
            parameter.put(SessionParameter.COMPRESSION, "true")
            parameter.put(SessionParameter.REPOSITORY_ID, props.getProperty("cmisrepo"))


            val script = props.getProperty("script")
            val threads= props.getProperty("threads").toInt()

            val es = Executors.newFixedThreadPool(threads)

            IntRange(1,threads).forEach { i ->
                es execute  {
                    println("${Date()} start thread $i")
                    val session = sessionFactory.createSession(HashMap(parameter))
                    val stream = ScriptClient.javaClass.getResourceAsStream("/scripts/${script}.groovy")
                    val reader = InputStreamReader(stream)
                    val out = PrintWriter(System.out)
                    val err = PrintWriter(System.err)
                    val mgr = ScriptEngineManager()
                    val engine = mgr.getEngineByExtension("groovy")
                    engine.getContext().setWriter(out)
                    engine.getContext().setErrorWriter(err)
                    engine.put("index", i)
                    engine.put("session", session)
                    engine.put("binding", session.getBinding())
                    engine.put("out", PrintWriter(out))
                    engine.eval(reader)
                    println("finished thread $i")
                }
            }

            es.shutdown()
            es.awaitTermination(10, TimeUnit.DAYS)

            println("${Date()} client finished")
        }
    }
}


fun main(args: Array<String>) = ScriptClient.main(args)
