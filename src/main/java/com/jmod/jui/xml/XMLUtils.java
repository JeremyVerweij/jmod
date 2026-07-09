package com.jmod.jui.xml;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.Stack;

public class XMLUtils {
    private static final XMLInputFactory factory = XMLInputFactory.newInstance();

    public static XMLStreamReader createXMLStream(InputStream stream){
        try {
            return factory.createXMLStreamReader(stream);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    public static XMLNode createNodeTree(XMLStreamReader streamReader) {
        try {
            return createNodeTreeUnsafe(streamReader);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private static XMLNode createNodeTreeUnsafe(XMLStreamReader streamReader) throws XMLStreamException{
        Stack<XMLNode> stack = new Stack<>();
        XMLNode root = null;

        while (streamReader.hasNext()){
            int event = streamReader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT -> {
                    String type = streamReader.getLocalName();
                    XMLNode newNode = new XMLNode(type);

                    for (int i = 0; i < streamReader.getAttributeCount(); i++) {
                        String attrName = streamReader.getAttributeLocalName(i);
                        String attrValue = streamReader.getAttributeValue(i);
                        newNode.addAttribute(attrName, attrValue);
                    }

                    if (root == null) root = newNode;

                    if (!stack.isEmpty()){
                        stack.peek().addChild(newNode);
                    }

                    stack.push(newNode);
                }
                case XMLStreamConstants.CHARACTERS -> {
                    if (!stack.isEmpty()) {
                        String text = streamReader.getText().trim();

                        if (!text.isEmpty()) {
                            stack.peek().setValue(text);
                        }
                    }
                }
                case XMLStreamConstants.END_ELEMENT -> {
                    if (!stack.isEmpty()) {
                        stack.pop();
                    }
                }
            }
        }

        return root;
    }
}
