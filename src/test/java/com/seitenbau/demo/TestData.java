package com.seitenbau.demo;

public class TestData
{

  public static Book expectedBook = new Book();

  static
  {
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
  }

}
