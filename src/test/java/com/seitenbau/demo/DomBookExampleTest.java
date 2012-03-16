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

/**
 *  DOM demo shows how the Java DOM API works, to read or write XML documents. 
 */
public class DomBookExampleTest
{

  static final String BOOK_NODE_NAME = "book";
  static final String CONTENT_NODE_NAME = "content";
  static final String TITLE_NODE_NAME = "title";
  static final String NUMBER_ATTR_NAME = "number";
  static final String CHAPTER_NODE_NAME = "chapter";

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

  /**
   * This demo shows how to read a XML document with the Java DOM parser into
   * a Java simple model. 
   */
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
  
  Book parse(Document document)
  {
    // create the new book instance
    Book book = new Book();
    // get the book XML Node from the document
    Node bookNode = document.getFirstChild();
    // verify that the first node has the tag name book
    assertEquals(BOOK_NODE_NAME, bookNode.getNodeName());
    // iterate over all book nodes search for chapter nodes all other nodes are ignored 
    for(Node chapterNode = bookNode.getFirstChild(); chapterNode != null;){
      // get the name of the node
      String nodeName = chapterNode.getNodeName();
      // check if the node is a chapter node
      if(StringUtils.equals(nodeName, CHAPTER_NODE_NAME)){
        // create and add a chapter instance to the result book
        Chapter chapter = new Chapter();
        book.chapters.add(chapter);
        // if the chapter has a attribute check for number attribute
        if(chapterNode.hasAttributes()){
          NamedNodeMap attributes = chapterNode.getAttributes();
          // is there a attribute with the name number
          Node numberAttrNode = attributes.getNamedItem(NUMBER_ATTR_NAME);
          if(numberAttrNode != null) 
          {
            // then set the number value as integer into the chapter instance
            chapter.number = Integer.valueOf(numberAttrNode.getNodeValue());
          }
        }
        // iterate over all chapter child nodes search for title and content tags all other tags are ignored.
        for(Node chapterChildNode = chapterNode.getFirstChild(); chapterChildNode != null;)
        {
          // check if the chapter child node is a title node
          String chapterChildNodeName = chapterChildNode.getNodeName();
          if(StringUtils.equals(chapterChildNodeName, TITLE_NODE_NAME))
          {
            // add the text content of the title node into the chapter title attribute
            String textContent = chapterChildNode.getTextContent();
            chapter.title = StringUtils.trim(textContent);
          }
          // check if the chapter child node is a content node 
          else if(StringUtils.equals(chapterChildNodeName, CONTENT_NODE_NAME))
          {
            // add the text content of the content into the content attribute of the chapter object.
            String textContent = chapterChildNode.getTextContent();
            chapter.content = StringUtils.trim(textContent);
          }
          // set the child node to the next sibling.
          chapterChildNode = chapterChildNode.getNextSibling();
        }
      }
      // set the child node to the next sibling.
      chapterNode = chapterNode.getNextSibling();
    }
    return book;
  }
  
  /**
   * This demo shows how to create a XML document with the DOM API.
   */
  @Test
  public void testDomWriteXml() throws Exception
  {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    
    // create a new book document
    Document document = documentBuilder.newDocument();
    
    // create the root node of the document
    Element bookNode = document.createElement(BOOK_NODE_NAME);
    // append the book as root child to the document
    document.appendChild(bookNode);
    
    // create the first chapter node
    bookNode.appendChild(createChapterNode(document, "1", "DOM", 
        "Reads the whole XML document into the memory..."));
    
    // create the second chapter node
    bookNode.appendChild(createChapterNode(document, "2", "SAX", 
        "Reads NOT the whole XML document into the memory..."));
    
    // parse the dom
    Book book = parse(document);
    
    // Verify the result
    assertEquals(TestData.expectedBook, book);
  }

  /**
   * Creates a chapter node.
   * 
   * @param document the DOM document not null.
   * @param number the number as String of the chapter.
   * @param title the title of the chapter.
   * @param content the content of the chapter.
   * @return a XML chapter node.
   */
  Element createChapterNode(Document document, String number, String title, String content)
  {
    Element chapterNode = document.createElement(CHAPTER_NODE_NAME);
    chapterNode.setAttribute(NUMBER_ATTR_NAME, number);
    Element titleNode = document.createElement(TITLE_NODE_NAME);
    titleNode.setTextContent(title);
    chapterNode.appendChild(titleNode);
    Element contentNode = document.createElement(CONTENT_NODE_NAME);
    contentNode.setTextContent(content);
    chapterNode.appendChild(contentNode);
    return chapterNode;
  }
  
}
