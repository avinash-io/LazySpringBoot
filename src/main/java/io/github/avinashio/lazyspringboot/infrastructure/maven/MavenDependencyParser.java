package io.github.avinashio.lazyspringboot.infrastructure.maven;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Component
public class MavenDependencyParser {

    public List<DependencyCoordinate> parse(InputStream inputStream)
            throws IOException {
        try {
            Document document =
                    DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder()
                            .parse(inputStream);

            return findDependencies(document);
        } catch (ParserConfigurationException | SAXException exception) {
            throw new IOException(
                    "Failed to parse Maven dependencies",
                    exception);
        }
    }

    List<DependencyCoordinate> parse(
            Document document) {
        return findDependencies(document);
    }

    private List<DependencyCoordinate> findDependencies(
            Document document) {
        Node project = document.getDocumentElement();

        Node dependencies =
                findDirectChild(project, "dependencies");

        if (dependencies == null) {
            return List.of();
        }

        List<DependencyCoordinate> coordinates =
                new ArrayList<>();

        NodeList children = dependencies.getChildNodes();

        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);

            if (!"dependency".equals(child.getNodeName())) {
                continue;
            }

            String groupId =
                    findDirectChildValue(child, "groupId");

            String artifactId =
                    findDirectChildValue(child, "artifactId");

            if (groupId == null || artifactId == null) {
                continue;
            }

            coordinates.add(
                    new DependencyCoordinate(
                            groupId,
                            artifactId));
        }

        return List.copyOf(coordinates);
    }

    private Node findDirectChild(
            Node parent,
            String childName) {
        NodeList children = parent.getChildNodes();

        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);

            if (childName.equals(child.getNodeName())) {
                return child;
            }
        }

        return null;
    }

    private String findDirectChildValue(
            Node parent,
            String childName) {
        Node child =
                findDirectChild(parent, childName);

        if (child == null) {
            return null;
        }

        return child.getTextContent().trim();
    }

}