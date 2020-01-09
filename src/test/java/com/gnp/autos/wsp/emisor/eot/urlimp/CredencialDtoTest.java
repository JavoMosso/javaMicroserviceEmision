package com.gnp.autos.wsp.emisor.eot.urlimp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
public class CredencialDtoTest {
    /* Clase Test. */
    @InjectMocks
    private CredencialDto base;
    @InjectMocks
    private CredencialDto equal;
    @InjectMocks
    private CredencialDto notEqual;
    @InjectMocks
    private CredencialDto nulo;
    @InjectMocks
    private CredencialDto object3;
    
    @Before
    public void initMocksBase() {
        base = new CredencialDto();
        ReflectionTestUtils.setField(base, "cveRol", "cveRol");
        ReflectionTestUtils.setField(base, "userlogin", "userlogin");
        equal = new CredencialDto();
        ReflectionTestUtils.setField(equal, "cveRol", base.getCveRol());
        ReflectionTestUtils.setField(equal, "userlogin", base.getUserlogin());
        notEqual = new CredencialDto();
        ReflectionTestUtils.setField(notEqual, "cveRol", "cveRoln");
        ReflectionTestUtils.setField(notEqual, "userlogin", "userloginn");
        nulo = null;
        object3 = new CredencialDto();
        ReflectionTestUtils.setField(object3, "cveRol", base.getCveRol());
        ReflectionTestUtils.setField(object3, "userlogin", base.getUserlogin());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        assertNotNull(base.getCveRol());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getCveRol(), base.getCveRol());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getCveRol(), base.getCveRol());
        assertNotEquals(base, notEqual);
        assertNotEquals(base.hashCode(), notEqual.hashCode());
        assertNotEquals(base.hashCode(), nulo);
    }
    
    @Test
    public void testAssert() {
        assertTrue(base.canEqual(base));
        assertTrue(base.equals(base));
        assertTrue(base.canEqual(equal));
        assertTrue(base.equals(equal));
    }
    
    @Test
    public void testDeny() {
        assertFalse(base.equals(notEqual));
        assertFalse(base.equals(null));
    }
    
    @Test
    public void testNull() {
        assertNull(nulo.getCveRol());
    }
    
    @Test
    public void testNotNull() {
        assertNotNull(base);
        assertNotNull(base.toString());
    }
    
    @Test
    public void testReflexive() {
        assertEquals(base, base);
    }
    
    @Test
    public void testSymmetric() {
        assertEquals(base, equal);
        assertEquals(equal, base);
    }
    
    @Test
    public void testTransitive() {
        assertEquals(base, equal);
        assertEquals(equal, object3);
        assertEquals(base, object3);
    }
}
