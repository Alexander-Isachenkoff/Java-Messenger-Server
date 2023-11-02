package messager.requests;

import messager.ClientXMLDebug;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransferableObjectTest {

    @Test
    void test() {
        TransferableObject testObject = new TransferableObject()
                .put("par1", 1)
                .put("par2", "2")
                .put("par3", 3.5)
                .put("par4", 10L)
                .put("par5", true);

        StringList list = new StringList();
        list.put(1);
        list.put("num");
        testObject.put("par25", list);

        TransferableObject innerObject = new TransferableObject()
                .put("in1", 21)
                .put("in2", "hello");

        testObject.put("innerParams", innerObject);

        ObjectList objectList = new ObjectList();
        TransferableObject transferableObject1 = new TransferableObject()
                .put("superInnerDouble", 48.5);
        objectList.put(transferableObject1);
        testObject.put("objectsList", objectList);

        assertEquals(testObject.getInt("par1"), 1);
        assertEquals(testObject.getString("par2"), "2");
        assertEquals(testObject.getDouble("par3"), 3.5);
        assertEquals(testObject.getLong("par4"), 10L);
        assertTrue(testObject.getBoolean("par5"));

        assertEquals(21, testObject.getParams("innerParams").getInt("in1"));
        assertEquals("hello", testObject.getParams("innerParams").getString("in2"));

        assertEquals(1, testObject.getStringList("par25").getInt(0));
        assertEquals("num", testObject.getStringList("par25").getString(1));

        assertEquals(48.5, testObject.getObjectList("objectsList").get(0).getDouble("superInnerDouble"));

        new ClientXMLDebug().post(testObject);
    }

}