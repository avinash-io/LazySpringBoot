package io.github.avinashio.lazyspringboot.infrastructure.maven;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Component
public class MavenProjectInspector {

    private static final String SPRING_BOOT_GROUP_ID = "org.springframework.boot";

    public boolean isSpringBootProject(Path pomFile) throws IOException {
        try (InputStream inputStream = Files.newInputStream(pomFile)) {
            Document document =
                    DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder()
                            .parse(inputStream);

            return containsSpringBootGroupId(document);
        } catch (SAXException exception) {
            return false;
        } catch (Exception exception) {
            throw new IOException("Failed to inspect Maven project: " + pomFile, exception);
        }
    }

    private boolean containsSpringBootGroupId(Document document) {
        NodeList groupIds = document.getElementsByTagName("groupId");

        for (int index = 0; index < groupIds.getLength(); index++) {
            Node groupId = groupIds.item(index);

            if (SPRING_BOOT_GROUP_ID.equals(groupId.getTextContent().trim())) {
                return true;
            }
        }

        return false;
    }
}