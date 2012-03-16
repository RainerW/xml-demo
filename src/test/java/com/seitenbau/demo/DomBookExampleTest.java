package com.seitenbau.demo;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Collection;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DomBookExampleTest
{

  private static final String NUMBER_ATTR = "number";
  private static final String CHAPTER_NODE = "chapter";

  class Book
  {

    Collection<Chapter> chapters = new Vector<DomBookExampleTest.Chapter>();
    
    @Override
    public int hashCode()
    {
      return HashCodeBuilder.reflectionHashCode(this);
    }
    
    @Override
    public boolean equals(Object obj) {
      return EqualsBuilder.reflectionEquals(this, obj);
    }
  }

  class Chapter
  {

    int number;
    
    String title;

    String content;
    
    @Override
    public int hashCode()
    {
      return HashCodeBuilder.reflectionHashCode(this);
    }
    
    @Override
    public boolean equals(Object obj) {
      return EqualsBuilder.reflectionEquals(this, obj);
    }
  }
  
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
    Book book = new Book();
    
    // create DOM Document
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    Document document = documentBuilder.parse(bookInputStream);

    // Parse the XML File
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
          if(StringUtils.equals(chapterChildNodeName, "title"))
          {
            String textContent = chapterChildNode.getTextContent();
            chapter.title = StringUtils.trim(textContent);
          }
          else if(StringUtils.equals(chapterChildNodeName, "content"))
          {
            String textContent = chapterChildNode.getTextContent();
            chapter.content = StringUtils.trim(textContent);
          }
          chapterChildNode = chapterChildNode.getNextSibling();
        }
      }
      chapterNode = chapterNode.getNextSibling();
    }
    
    // Verify the result
    Book expectedBook = new Book();
    Chapter chapter1 = new Chapter();
    chapter1.number = 1;
    chapter1.title = "DOM";
    chapter1.content = "Reads the whole XML document into the memory...";
    expectedBook.chapters.add(chapter1);
    Chapter chapter2 = new Chapter();
    chapter2.number = 2;
    chapter2.title = "SAX";
    chapter2.content = "Reads NOT the whole XML document into the memory...";
    expectedBook.chapters.add(chapter2);
    
    assertEquals(expectedBook, book);
  }
}
