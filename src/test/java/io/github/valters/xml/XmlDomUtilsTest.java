package io.github.valters.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class XmlDomUtilsTest {

    @Test
    public void shouldRemoveNsPrefix() {
        assertThat( XmlDomUtils.removeNsPrefix( "xs:element" ), is( "element") );
    }

    @Test
    public void shouldHandleEdgeCasesOnNsRemove() {
        assertThat( XmlDomUtils.removeNsPrefix( "element" ), is( "element") );
        assertThat( XmlDomUtils.removeNsPrefix( ":element" ), is( "element") );
        assertThat( XmlDomUtils.removeNsPrefix( "xs:" ), is( "") );
        assertThat( XmlDomUtils.removeNsPrefix( ":element:" ), is( "element:") );
    }


}
