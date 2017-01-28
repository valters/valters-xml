package io.github.valters.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TransformToStringTest {

    private final TransformToString transform = new TransformToString();
    private final XPathUtils xpath = new XPathUtils();

    @Test
    public void shouldPrintXmlNodeText() throws Exception {
        final Document doc = parseTestDoc();

        assertThat( transform.nodesToString( xpath.findNode( doc, "/diffreport/diff" ).getChildNodes() ), is( "[IMG]<br/>\n[GMI]") );
    }

    private Document parseTestDoc() throws SAXException, IOException, ParserConfigurationException {
        try( InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream( "test/test.xml" ) ) {
            final Document doc = XmlDomUtils.documentBuilder().parse( is );
            return doc;
        }
    }

}
