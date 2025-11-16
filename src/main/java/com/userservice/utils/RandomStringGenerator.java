package com.userservice.utils;

public class RandomStringGenerator {

    private static final String alhpaNumerics="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generateRandomStringWithLength(int length){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int index=(int)(Math.random()*alhpaNumerics.length());
            sb.append(alhpaNumerics.charAt(index));
        }
        return sb.toString();
    }
}
