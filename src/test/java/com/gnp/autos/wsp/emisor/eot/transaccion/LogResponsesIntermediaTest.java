package com.gnp.autos.wsp.emisor.eot.transaccion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
public class LogResponsesIntermediaTest {
    /* Clase Test. */
    @InjectMocks
    private LogResponsesIntermedia base;
    @InjectMocks
    private LogResponsesIntermedia equal;
    @InjectMocks
    private LogResponsesIntermedia notEqual;
    @InjectMocks
    private LogResponsesIntermedia nulo;
    @InjectMocks
    private LogResponsesIntermedia object3;
    
    @Before
    public void initMocksBase() {
        List<LogResponseIntermedia> lgResp = new ArrayList<>();
        lgResp.add(new LogResponseIntermedia());
        base = new LogResponsesIntermedia();
        ReflectionTestUtils.setField(base, "lgResp", lgResp);
        equal = new LogResponsesIntermedia();
        ReflectionTestUtils.setField(equal, "lgResp", base.getLgResp());
        notEqual = new LogResponsesIntermedia();
        ReflectionTestUtils.setField(notEqual, "lgResp", new ArrayList<>());
        nulo = null;
        object3 = new LogResponsesIntermedia();
        ReflectionTestUtils.setField(object3, "lgResp", base.getLgResp());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setLgResp(new ArrayList<>());
        assertNotNull(base.getLgResp());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getLgResp(), base.getLgResp());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getLgResp(), base.getLgResp());
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
        assertNull(nulo.getLgResp());
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
