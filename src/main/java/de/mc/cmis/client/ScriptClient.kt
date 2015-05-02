/*
 * Copyright (c) 2015 Mind Consulting UG(haftungsbeschränkt)
 */
package de.mc.cmis.client

import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl
import org.apache.chemistry.opencmis.commons.SessionParameter
import org.apache.chemistry.opencmis.commons.enums.BindingType
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.HashMap
import java.util.Properties
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

            val session = sessionFactory.createSession(parameter)

            val stream = ScriptClient.javaClass.getResourceAsStream("/scripts/" + props.getProperty("script") + ".groovy")
            val reader = InputStreamReader(stream)
            val out = PrintWriter(System.out)
            val err = PrintWriter(System.err)
            val mgr = ScriptEngineManager()
            val engine = mgr.getEngineByExtension("groovy")
            engine.getContext().setWriter(out)
            engine.getContext().setErrorWriter(err)
            engine.put("session", session)
            engine.put("binding", session.getBinding())
            engine.put("out", PrintWriter(out))
            engine.eval(reader)

            println("client finished")
        }
    }
}

fun main(args: Array<String>) = ScriptClient.main(args)
