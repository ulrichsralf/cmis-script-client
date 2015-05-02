/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.mc.cmis.client

import java.util.ArrayList
import java.util.HashMap

import org.apache.chemistry.opencmis.client.api.CmisObject
import org.apache.chemistry.opencmis.client.api.Folder
import org.apache.chemistry.opencmis.client.api.ItemIterable
import org.apache.chemistry.opencmis.client.api.Repository
import org.apache.chemistry.opencmis.client.api.Session
import org.apache.chemistry.opencmis.client.api.SessionFactory
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl
import org.apache.chemistry.opencmis.commons.SessionParameter
import org.apache.chemistry.opencmis.commons.enums.BindingType
import org.apache.chemistry.opencmis.commons.impl.Constants

public class Hello {
    companion object {
        public fun main(args: Array<String>) {

            System.out.println( " started")

            // Create a SessionFactory and set up the SessionParameter map
            val sessionFactory = SessionFactoryImpl.newInstance()
            val parameter = HashMap<String, String>()

            // connection settings - we are connecting to a public cmis repo,
            // using the AtomPUB binding
            parameter.put(SessionParameter.BROWSER_URL, " http://localhost:8080/browser/")
            parameter.put(SessionParameter.BINDING_TYPE, BindingType.BROWSER.value())
            parameter.put(SessionParameter.USER, "ralf")
            parameter.put(SessionParameter.PASSWORD, "ralf")


          //  sessionFactory.createSession(parameter)
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

            // Get everything in the root folder and print the names of the objects
            val root = session.getRootFolder()
            val children = root.getChildren()
            System.out.println("Found the following objects in the root folder:-")
            for (o in children) {
                System.out.println(o.getName())
            }

            System.out.println( " ended")
        }
    }
}

fun main(args: Array<String>) = Hello.main(args)
