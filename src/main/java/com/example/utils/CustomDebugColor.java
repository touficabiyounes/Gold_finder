package com.example.utils;

public class CustomDebugColor {
  private static final String DEFAULT = "\u001B[0m";
  private static final String CUSTOM_RED = "\u001B[91m";
  private static final String CUSTOM_GREEN = "\u001B[92m";
  private static final String CUSTOM_PURPLE = "\u001B[34m";
  private static final String CUSTOM_ORANGE = "\u001B[93m";
    private static final String CUSTOM_BLUE = "\u001B[94m";


  private static final String CUSTOM_BACKGROUND_PINK = "\u001B[41m";
  private static final String CUSTOM_BACKGROUND_ORANGE = "\u001B[43m";
  private static final String CUSTOM_BACKGROUND_PURPLE = "\u001B[44m";

  public static void customInfo(String message) {
    System.out.println(CUSTOM_BLUE + message + DEFAULT);
  }
  public static void customError(String message) {System.out.println(CUSTOM_RED + "ERROR : " + DEFAULT + CUSTOM_BACKGROUND_PINK + message + DEFAULT);
  }
  public static void customSuccess(String message) {
    System.out.println(CUSTOM_GREEN + message + DEFAULT);
  }


  public static void customDebug(String message) {
    System.out.println(CUSTOM_PURPLE + message + DEFAULT);
  }

  public static void customWarning(String message) {
    System.out.println(CUSTOM_ORANGE + "WARNING : " + DEFAULT + CUSTOM_BACKGROUND_ORANGE + message + DEFAULT);
  }

  public static void customOrange(String message) {
    System.out.println(CUSTOM_ORANGE +  message + DEFAULT);
  }

  public static String customPurple(String message) {
    return CUSTOM_PURPLE + message + DEFAULT;
  }

}



