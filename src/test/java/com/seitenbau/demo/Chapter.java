package com.seitenbau.demo;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Chapter
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