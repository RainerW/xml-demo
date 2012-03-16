package com.seitenbau.demo;

import java.util.Collection;
import java.util.Vector;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


public class Book
{

  Collection<Chapter> chapters = new Vector<Chapter>();
  
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