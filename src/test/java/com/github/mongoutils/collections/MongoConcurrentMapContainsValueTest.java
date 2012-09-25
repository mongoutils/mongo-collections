package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class MongoConcurrentMapContainsValueTest {

    MongoConcurrentMap<String, TestBean> map;
    @Mock
    DBCollection collection;
    @Mock
    DBObjectSerializer<TestBean> valueSerializer;
    @Mock
    DBObject queryObject;
    @Mock
    DBObject resultObject;
    @Captor
    ArgumentCaptor<TestBean> valueCaptor;

    @Before
    public void createMap() throws UnknownHostException, MongoException {
        when(valueSerializer.toDBObject(any(TestBean.class), anyBoolean(), anyBoolean())).thenReturn(queryObject);
        map = new MongoConcurrentMap<String, TestBean>(collection, null, valueSerializer);
    }

    @Test
    public void keyNotContained() {
        TestBean testBean = new TestBean("testbean");

        when(collection.findOne(queryObject)).thenReturn(null);

        assertFalse(map.containsValue(testBean));

        verify(valueSerializer).toDBObject(valueCaptor.capture(), eq(true), eq(false));
        assertEquals(testBean, valueCaptor.getValue());
    }

    @Test
    public void keyContained() {
        TestBean testBean = new TestBean("testbean");

        when(collection.count(queryObject)).thenReturn(1L);

        assertTrue(map.containsValue(testBean));

        verify(valueSerializer).toDBObject(valueCaptor.capture(), eq(true), eq(false));
        assertEquals(testBean, valueCaptor.getValue());
    }

}
