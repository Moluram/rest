package com.rest.helpers;

public class RestPreconditions {
  public static <T> T checkFound(T o) {
    if (null == o) {
      throw new ResourceNotFoundException();
    }
    return o;
  }
}
