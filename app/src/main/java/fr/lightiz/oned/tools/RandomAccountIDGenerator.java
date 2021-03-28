package fr.lightiz.oned.tools;

public class RandomAccountIDGenerator {
    private static final int MAX_ID_LENGTH = 25;

    private static final String[] avaiableChars = setAvailableChars();

    public static String generateID(){
        String result = "";
        int indexToAdd = 0;
        for(int i = 0; i < MAX_ID_LENGTH; i ++){
            indexToAdd = (int) (Math.random() * avaiableChars.length);
            result = result + avaiableChars[indexToAdd];
        }
        return result;
    }

    private static String[] setAvailableChars(){
        String allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return allChars.split("");
    }
}
