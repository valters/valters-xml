/*
  This file is licensed to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package io.github.valters.xml;

import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TransformToString {

    /** get single node as text representation */
    public String nodeToString( final Node node ) {
        try {
            final TransformerFactory tf = XmlDomUtils.transformerFactory();
            final Transformer transformer = XmlDomUtils.newFragmentTransformer( tf );

            final StringWriter stw = new StringWriter();
            transformer.transform( new DOMSource( node ), new StreamResult( stw ) );
            return stw.toString();
        }
        catch( TransformerException | TransformerFactoryConfigurationError e ) {
            return "{failed to serialize node "+node+": "+e+"}";
        }
    }

    /** also strip namespaces (if any) when printing node */
    public String nodeToStringClean( final Node node ) {
        if( node instanceof Attr ) {
            return nodeToString( node );
        }

        try {
            final TransformerFactory tf = XmlDomUtils.transformerFactory();
            final Transformer transformer = XmlDomUtils.newFragmentTransformer( tf );

            final StringWriter stw = new StringWriter();
            transformer.transform( new DOMSource( importNodeWithoutNamespaces( node ) ), new StreamResult( stw ) );
            return stw.toString();
        }
        catch( final ParserConfigurationException | TransformerException | TransformerFactoryConfigurationError e ) {
            return "{failed to serialize node "+node+": "+e+"}";
        }
    }

    /** use this to print children of some node, to exclude the parent node. */
    public String nodesToString( final NodeList nodes ) {
        final StringBuilder sb = new StringBuilder();

        for( int i = 0; i < nodes.getLength(); i++ ) {
            sb.append( nodeToString( nodes.item( i ) ) );
        }

        return sb.toString();
    }

    /** tricky stuff that removes namespaces */
    public Document importWithoutNamespaces( final Node node ) throws ParserConfigurationException {
        final Document doc = XmlDomUtils.documentBuilder().newDocument();
//        doc.setStrictErrorChecking( false ); // doc will throw error if elements have prefixes (xs:complexType), but not sure how to get rid of those
        final Node newNode = doc.importNode( node, true );
        final Node cleanNode = XmlDomUtils.removeNamespaceRecursive( newNode, doc );
        XmlDomUtils.removeXmlNsAttribute( cleanNode );
        doc.appendChild( cleanNode );
        return doc;
    }

    public Node importNodeWithoutNamespaces( final Node node ) throws ParserConfigurationException {
        return importWithoutNamespaces( node ).getDocumentElement();
    }

}
