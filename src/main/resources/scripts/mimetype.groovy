/*
 * Copyright (c) 2015 Mind Consulting UG(haftungsbeschränkt)
 */

package scripts
import org.apache.chemistry.opencmis.commons.data.*
import org.apache.chemistry.opencmis.client.api.*

Document doc = session.getObject("...");

MutableContentStream contentStream = doc.getContentStream();
contentStream.setMimeType("application/other");

doc.setContentStream(contentStream, true);