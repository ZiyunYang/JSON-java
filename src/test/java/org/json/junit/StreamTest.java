package org.json.junit;

import org.json.JSONObject;
import org.json.XML;
import org.json.XMLParserConfiguration;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StreamTest {
    @Test
    public void streamTest(){
        String xml ="<?xml version=\"1.0\"?>\n" +
                "<catalog>\n" +
                "    <book id=\"bk109\">\n" +
                "        <author>Kress Peter</author>\n" +
                "        <title>Paradox Lost</title>\n" +
                "        <genre>Science Fiction</genre>\n" +
                "        <price>6.95</price>\n" +
                "        <publish_date>2000-11-02</publish_date>\n" +
                "        <description>After an inadvertant trip through a Heisenberg\n" +
                "            Uncertainty Device, James Salway discovers the problems\n" +
                "            of being quantum.\n" +
                "        </description>\n" +
                "    </book>\n" +
                "    <book id=\"bk110\">\n" +
                "        <author>O'Brien, Tim</author>\n" +
                "        <title>Microsoft .NET: The Programming Bible</title>\n" +
                "        <genre>Computer</genre>\n" +
                "        <price>36.95</price>\n" +
                "        <publish_date>2000-12-09</publish_date>\n" +
                "        <description>Microsoft's .NET initiative is explored in\n" +
                "            detail in this deep programmer's reference.\n" +
                "        </description>\n" +
                "    </book>\n" +
                "</catalog>";
        JSONObject jsonObject =
                XML.toJSONObject(xml);
        Stream<JSONObject> stream = jsonObject.toStream();
        int i=0;
        List<JSONObject> list = stream.collect(Collectors.toList());
        assertEquals("{\"author\":\"Kress Peter\"}",list.get(0).toString());
    }
}
