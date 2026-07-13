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
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MavenProjectInspector {

    private static final String SPRING_BOOT_GROUP_ID = "org.springframework.boot";

    private static final String DISALLOW_DOCTYPE_DECLARATION =
            "http://apache.org/xml/features/disallow-doctype-decl";

    private final MavenDependencyParser dependencyParser;

    public MavenProjectInspector(
            MavenDependencyParser dependencyParser) {
        this.dependencyParser = dependencyParser;
    }

    public boolean isSpringBootProject(Path pomFile) throws IOException {
        try (InputStream inputStream = Files.newInputStream(pomFile)) {
            DocumentBuilderFactory documentBuilderFactory =
                    createSecureDocumentBuilderFactory();

            Document document =
                    documentBuilderFactory.newDocumentBuilder().parse(inputStream);

            return hasSpringBootParent(document);
        } catch (SAXException exception) {
            return false;
        } catch (ParserConfigurationException exception) {
            throw new IOException("Failed to configure XML parser", exception);
        }
    }

    private boolean hasSpringBootParent(Document document) {
        Node project = document.getDocumentElement();
        Node parent = findDirectChild(project, "parent");

        if (parent == null) {
            return false;
        }

        String groupId = findDirectChildValue(parent, "groupId");
        String artifactId = findDirectChildValue(parent, "artifactId");

        return SPRING_BOOT_GROUP_ID.equals(groupId)
                && "spring-boot-starter-parent".equals(artifactId);
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

    public MavenProjectMetadata inspect(Path pomFile) throws IOException {
        try (InputStream inputStream = Files.newInputStream(pomFile)) {
            DocumentBuilderFactory documentBuilderFactory =
                    createSecureDocumentBuilderFactory();

            Document document =
                    documentBuilderFactory.newDocumentBuilder().parse(inputStream);

            Node project = document.getDocumentElement();

            return new MavenProjectMetadata(
                    findProjectGroupId(document),
                    findDirectChildValue(project, "artifactId"),
                    findSpringBootVersion(document),
                    findJavaVersion(document),
                    dependencyParser.parse(document));

        } catch (SAXException exception) {
            throw new IOException("Invalid Maven POM: " + pomFile, exception);
        } catch (ParserConfigurationException exception) {
            throw new IOException("Failed to configure XML parser", exception);
        }
    }

    private String findProjectGroupId(Document document) {
        Node project = document.getDocumentElement();

        String groupId = findDirectChildValue(project, "groupId");

        if (groupId != null) {
            return groupId;
        }

        Node parent = findDirectChild(project, "parent");

        if (parent == null) {
            return null;
        }

        return findDirectChildValue(parent, "groupId");
    }

    private String findSpringBootVersion(Document document) {
        Node project = document.getDocumentElement();
        Node parent = findDirectChild(project, "parent");

        if (parent == null) {
            return null;
        }

        String groupId = findDirectChildValue(parent, "groupId");
        String artifactId = findDirectChildValue(parent, "artifactId");

        if (SPRING_BOOT_GROUP_ID.equals(groupId)
                && "spring-boot-starter-parent".equals(artifactId)) {
            return findDirectChildValue(parent, "version");
        }

        return null;
    }

    private String findJavaVersion(Document document) {
        Node project = document.getDocumentElement();
        Node properties = findDirectChild(project, "properties");

        if (properties == null) {
            return null;
        }

        return findDirectChildValue(properties, "java.version");
    }

    private String findDirectChildValue(Node parent, String nodeName) {
        Node child = findDirectChild(parent, nodeName);

        if (child == null) {
            return null;
        }

        return child.getTextContent().trim();
    }

    private Node findDirectChild(Node parent, String nodeName) {
        NodeList children = parent.getChildNodes();

        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);

            if (nodeName.equals(child.getNodeName())) {
                return child;
            }
        }

        return null;
    }



}