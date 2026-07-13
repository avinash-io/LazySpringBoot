package io.github.avinashio.lazyspringboot.infrastructure.maven;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Component
public class MavenProjectInspector {

    private static final String SPRING_BOOT_GROUP_ID = "org.springframework.boot";

    private static final String DISALLOW_DOCTYPE_DECLARATION =
            "http://apache.org/xml/features/disallow-doctype-decl";

    public boolean isSpringBootProject(Path pomFile) throws IOException {
        try (InputStream inputStream = Files.newInputStream(pomFile)) {
            DocumentBuilderFactory documentBuilderFactory =
                    createSecureDocumentBuilderFactory();

            Document document =
                    documentBuilderFactory.newDocumentBuilder().parse(inputStream);

            return containsSpringBootGroupId(document);
        } catch (SAXException exception) {
            return false;
        } catch (ParserConfigurationException exception) {
            throw new IOException(
                    "Failed to configure XML parser", exception);
        }
    }

    private DocumentBuilderFactory createSecureDocumentBuilderFactory()
            throws ParserConfigurationException {
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        factory.setFeature(
                DISALLOW_DOCTYPE_DECLARATION,
                true);

        factory.setFeature(
                XMLConstants.FEATURE_SECURE_PROCESSING,
                true);

        factory.setAttribute(
                XMLConstants.ACCESS_EXTERNAL_DTD,
                "");

        factory.setAttribute(
                XMLConstants.ACCESS_EXTERNAL_SCHEMA,
                "");

        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);

        return factory;
    }

    private boolean containsSpringBootGroupId(Document document) {
        NodeList groupIds = document.getElementsByTagName("groupId");

        for (int index = 0; index < groupIds.getLength(); index++) {
            Node groupId = groupIds.item(index);

            if (SPRING_BOOT_GROUP_ID.equals(
                    groupId.getTextContent().trim())) {
                return true;
            }
        }

        return false;
    }
}