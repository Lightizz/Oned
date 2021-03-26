package fr.lightiz.oned;

import java.util.List;

public class RandomAccountIDGenerator {
    private static final int MAX_ID_LENGHT = 25;

    private static String[] avaiableChars = setAvaiableChars();

    public static String generateID(){
        String result = "";
        int indexToAdd = 0;
        for(int i = 0; i < MAX_ID_LENGHT; i ++){
            indexToAdd = (int) (Math.random() * avaiableChars.length);
            result = result + avaiableChars[indexToAdd];
        }
        return result;
    }

    private static String[] setAvaiableChars(){
        String allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return allChars.split("");
    }
}
