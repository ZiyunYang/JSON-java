package org.json.junit;

import org.json.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SWE262Test {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void shouldReadSubObject() {
        String xmlStr = "<?xml version=\"1.0\"?>\n" +
                "<catalog>\n" +
                "    <book id=\"bk109\">\n" +
                "        <author>Kress, Peter</author>\n" +
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
                "    <book id=\"bk111\">\n" +
                "        <author>O'Brien, Tim</author>\n" +
                "        <title>MSXML3: A Comprehensive Guide</title>\n" +
                "        <genre>Computer</genre>\n" +
                "        <price>36.95</price>\n" +
                "        <publish_date>2000-12-01</publish_date>\n" +
                "        <description>The Microsoft MSXML3 parser is covered in\n" +
                "            detail, with attention to XML DOM interfaces, XSLT processing,\n" +
                "            SAX and more.\n" +
                "        </description>\n" +
                "    </book>\n" +
                "    <book id=\"bk112\">\n" +
                "        <author>Galos, Mike</author>\n" +
                "        <title>Visual Studio 7: A Comprehensive Guide</title>\n" +
                "        <genre>Computer</genre>\n" +
                "        <price>49.95</price>\n" +
                "        <publish_date>2001-04-16</publish_date>\n" +
                "        <description>Microsoft Visual Studio 7 is explored in depth,\n" +
                "            looking at how Visual Basic, Visual C++, C#, and ASP+ are\n" +
                "            integrated into a comprehensive development\n" +
                "            environment.\n" +
                "        </description>\n" +
                "    </book>\n" +
                "</catalog>";

        try {
            Reader reader = new StringReader(xmlStr);
            JSONPointer pointer = new JSONPointer("/catalog/book/2/genre");

            JSONObject jo = XML.toJSONObject(reader, pointer);
            assertEquals("Correct result.","{\"genre\":\"Computer\"}",jo.toString());

            reader.close();
        } catch (JSONException e) {
            System.out.println("Caught a JSON Exception ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Caught a IO Exception ");
            e.printStackTrace();
        }
    }


    @Test
    public void handleReadEmptySub() {
        String xmlStr ="<?xml version=\"1.0\"?>\n" +
                "<catalog>\n" +
                "    <book id=\"bk109\">\n" +
                "        <author>Kress, Peter</author>\n" +
                "        <title>Paradox Lost</title>\n" +
                "        <genre>Science Fiction</genre>\n" +
                "        <price>6.95</price>\n" +
                "        <publish_date>2000-11-02</publish_date>\n" +
                "        <description>After an inadvertant trip through a Heisenberg\n" +
                "            Uncertainty Device, James Salway discovers the problems\n" +
                "            of being quantum.\n" +
                "        </description>\n" +
                "    </book>\n" +
                "</catalog>";

        try {
            Reader reader = new StringReader(xmlStr);
            JSONPointer pointer = new JSONPointer("");
            JSONObject jo = XML.toJSONObject(reader, pointer);
            reader.close();
        } catch (JSONException e) {
            System.out.println("Caught a JSON Exception ");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Caught a IO Exception ");
            e.printStackTrace();
        }catch (StringIndexOutOfBoundsException e){
            assertEquals("Expecting an exception message",
                    "String index out of range: -1",
                    e.getMessage());
        }
    }


    @Test
    public void handleReadWrongSub() {
        String xmlStr ="<?xml version=\"1.0\"?>\n" +
                "<catalog>\n" +
                "    <book id=\"bk109\">\n" +
                "        <author>Kress, Peter</author>\n" +
                "        <title>Paradox Lost</title>\n" +
                "        <genre>Science Fiction</genre>\n" +
                "        <price>6.95</price>\n" +
                "        <publish_date>2000-11-02</publish_date>\n" +
                "        <description>After an inadvertant trip through a Heisenberg\n" +
                "            Uncertainty Device, James Salway discovers the problems\n" +
                "            of being quantum.\n" +
                "        </description>\n" +
                "    </book>\n" +
                "</catalog>";

        try {
            Reader reader = new StringReader(xmlStr);
            JSONPointer pointer = new JSONPointer("/catalog/book/0/name");
            JSONObject jo = XML.toJSONObject(reader, pointer);
            reader.close();
        } catch (JSONException e) {
            assertEquals("Expecting an exception message",
                    "Misshaped element at 465 [character 10 line 15]",
                    e.getMessage());
        } catch (IOException e){
            System.out.println("Caught a IO Exception ");
            e.printStackTrace();
        }
    }

    @Test
    public void handleReplacement() {
        String xmlStr ="<?xml version=\"1.0\"?>\n" +
                "<catalog>\n" +
                "    <book id=\"bk109\">\n" +
                "        <author>Kress, Peter</author>\n" +
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

        try {
            Reader reader = new StringReader(xmlStr);
            JSONPointer pointer = new JSONPointer("/catalog/book/1");
            JSONObject newObj = new JSONObject();
            newObj.put("author", "Bredan Gregg");
            newObj.put("price", "100");
            newObj.put("title", "Systems Performance");

            JSONObject jo = XML.toJSONObject(reader, pointer,newObj);
            String newValue =  pointer.queryFrom(jo).toString();
            reader.close();
            assertEquals("Correct result","{\"author\":\"Bredan Gregg\",\"price\":\"100\",\"title\":\"Systems Performance\"}", newValue);
        } catch (JSONException e) {
            System.out.println("Caught a JSON Exception ");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Caught a IO Exception ");
            e.printStackTrace();
        }
    }

    @Test
    public void handleWrongReplacement() {
        String xmlStr ="<?xml version=\"1.0\"?>\n" +
                "<catalog>\n" +
                "    <book id=\"bk109\">\n" +
                "        <author>Kress, Peter</author>\n" +
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

        try {
            Reader reader = new StringReader(xmlStr);
            JSONPointer pointer = new JSONPointer("/catalog/name/1");
            JSONObject newObj = new JSONObject();
            newObj.put("author", "Bredan Gregg");
            newObj.put("price", "100");
            newObj.put("title", "Systems Performance");

            JSONObject jo = XML.toJSONObject(reader, pointer,newObj);
            String newValue =  pointer.queryFrom(jo).toString();
            reader.close();
        } catch (JSONException e) {
            assertEquals("Expecting an exception message",
                    "JSONObject[\"name\"] not found.",
                    e.getMessage());
        } catch (IOException e){
            System.out.println("Caught a IO Exception ");
            e.printStackTrace();
        }
    }


    @Test
    public void handleReplaceTag() {
        String xmlStr ="<?xml version=\"1.0\"?>\n" +
                "<catalog>\n" +
                "    <book id=\"bk109\">\n" +
                "        <author>Kress, Peter</author>\n" +
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

        try {
            Reader reader = new StringReader(xmlStr);
            JSONObject newObj = XML.toJSONObjectTest(reader, s -> "SWE262_" + s);
            assertEquals("Correct result.","{\"SWE262_catalog\":{\"SWE262_book\":[{\"SWE262_author\":\"Kress, Peter\",\"SWE262_title\":\"Paradox Lost\",\"SWE262_publish_date\":\"2000-11-02\",\"SWE262_genre\":\"Science Fiction\",\"SWE262_description\":\"After an inadvertant trip through a Heisenberg\\n            Uncertainty Device, James Salway discovers the problems\\n            of being quantum.\",\"SWE262_id\":\"bk109\",\"SWE262_price\":6.95},{\"SWE262_author\":\"O'Brien, Tim\",\"SWE262_title\":\"Microsoft .NET: The Programming Bible\",\"SWE262_publish_date\":\"2000-12-09\",\"SWE262_genre\":\"Computer\",\"SWE262_description\":\"Microsoft's .NET initiative is explored in\\n            detail in this deep programmer's reference.\",\"SWE262_id\":\"bk110\",\"SWE262_price\":36.95}]}}",newObj.toString());
//            System.out.println(newObj.toString());
            reader.close();
        } catch (JSONException e) {
            System.out.println("Caught a JSON Exception ");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Caught a IO Exception ");
            e.printStackTrace();
        }
    }
}
