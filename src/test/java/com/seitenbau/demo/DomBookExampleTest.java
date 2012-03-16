package com.seitenbau.demo;

import static org.junit.Assert.*;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DomBookExampleTest
{

  private static final String CONTENT_NODE = "content";
  private static final String TITLE_NODE = "title";
  private static final String NUMBER_ATTR = "number";
  private static final String CHAPTER_NODE = "chapter";

  InputStream bookInputStream;

  @Before
  public void setup() throws Exception
  {
    bookInputStream = getClass().getResource("/book.xml").openStream();
  }

  @After
  public void tearDown() throws Exception
  {
    bookInputStream.close();
  }

  @Test
  public void testDomReadXml() throws Exception
  {
    // create DOM Document
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    Document document = documentBuilder.parse(bookInputStream);

    // Parse the XML File
    Book book = parse(document);
    
    // Verify the result
    assertEquals(TestData.expectedBook, book);
  }
  
  @Test
  public void testDomWriteXml() throws Exception
  {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    
    // create a new book document
    Document document = documentBuilder.newDocument();
    Element bookNode = document.createElement("book");
    document.appendChild(bookNode);
    {
      Element chapterNode = document.createElement(CHAPTER_NODE);
      chapterNode.setAttribute("number", "1");
      Element titleNode = document.createElement(TITLE_NODE);
      titleNode.setTextContent("DOM");
      chapterNode.appendChild(titleNode);
      Element contentNode = document.createElement(CONTENT_NODE);
      contentNode.setTextContent("Reads the whole XML document into the memory...");
      chapterNode.appendChild(contentNode);
      bookNode.appendChild(chapterNode);
    }
    {
      Element chapterNode = document.createElement(CHAPTER_NODE);
      chapterNode.setAttribute("number", "2");
      Element titleNode = document.createElement(TITLE_NODE);
      titleNode.setTextContent("SAX");
      chapterNode.appendChild(titleNode);
      Element contentNode = document.createElement(CONTENT_NODE);
      contentNode.setTextContent("Reads NOT the whole XML document into the memory...");
      chapterNode.appendChild(contentNode);
      bookNode.appendChild(chapterNode);
    }
    
    // parse the dom
    Book book = parse(document);
    
    // Verify the result
    assertEquals(TestData.expectedBook, book);
  }

  Book parse(Document document)
  {
    Book book = new Book();
    Node bookNode = document.getFirstChild();
    for(Node chapterNode = bookNode.getFirstChild(); chapterNode != null;){
      String nodeName = chapterNode.getNodeName();
      if(StringUtils.equals(nodeName, CHAPTER_NODE)){
        Chapter chapter = new Chapter();
        book.chapters.add(chapter);
        if(chapterNode.hasAttributes()){
          NamedNodeMap attributes = chapterNode.getAttributes();
          Node numberAttrNode = attributes.getNamedItem(NUMBER_ATTR);
          if(numberAttrNode != null) 
          {
            chapter.number = Integer.valueOf(numberAttrNode.getNodeValue());
          }
        }
        for(Node chapterChildNode = chapterNode.getFirstChild(); chapterChildNode != null;)
        {
          String chapterChildNodeName = chapterChildNode.getNodeName();
          if(StringUtils.equals(chapterChildNodeName, TITLE_NODE))
          {
            String textContent = chapterChildNode.getTextContent();
            chapter.title = StringUtils.trim(textContent);
          }
          else if(StringUtils.equals(chapterChildNodeName, CONTENT_NODE))
          {
            String textContent = chapterChildNode.getTextContent();
            chapter.content = StringUtils.trim(textContent);
          }
          chapterChildNode = chapterChildNode.getNextSibling();
        }
      }
      chapterNode = chapterNode.getNextSibling();
    }
    return book;
  }
}
