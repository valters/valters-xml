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

import javax.xml.namespace.QName;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NodeToString {

    private final TransformToString transform = new TransformToString();

    public String nodeToString( final Node node ) {
        final String str = trim( transform.nodeToStringClean( node ) );
        if( ! isNullOrEmpty( str ) ) {
            return str;
        }
        return altPrint( node ); // is probably attribute
    }

    private String trim( final String str ) {
        if( isNullOrEmpty(  str  ) ) {
            return str;
        }
        return str.trim();
    }

    private boolean isNullOrEmpty( final String str ) {
        if( str == null || str.length() == 0 ) {
            return true;
        }
        return false;
    }

    /** simple print when clever fails */
    private String altPrint( final Node node ) {
        return String.valueOf( node );
    }

    private String printAttributes( final NamedNodeMap attributes ) {
        if( attributes == null || attributes.getLength() == 0 ) {
            return ""; // nothing
        }

        final StringBuilder b = new StringBuilder();
        for( int i = 0; i < attributes.getLength(); i++ ) {
            b.append( "@" ).append( attributes.item( i ) );
        }
        return b.toString();
    }

    public String printNodeParentInfo( final Node node ) {
        final Node parentNode = node.getParentNode();
        if( parentNode == null ) {
            return "(no parent node)";
        }

        if( parentNode.getOwnerDocument() == null ) {
            return "(attached to root)";
        }

        return printNodeSignature( parentNode );
    }

    public String printNodeSignature( final Node node ) {
        return "<"+node.getNodeName() + " " + printAttributes( node.getAttributes() ) +">";
    }

    public String attrToString( final Node node, final QName value ) {
        if( value == null ) {
            return "(null)";
        }
        if( isNullOrEmpty( value.getNamespaceURI() ) ) {
            return String.valueOf( node.getAttributes().getNamedItem( value.getLocalPart() ) );
        }
        else {
            return String.valueOf( node.getAttributes().getNamedItemNS( value.getNamespaceURI(), value.getLocalPart() ) );
        }
    }

    public String attrToString( final Attr node ) {
        if( node == null ) {
            return "(null)";
        }

        return String.valueOf( node );
    }

}
