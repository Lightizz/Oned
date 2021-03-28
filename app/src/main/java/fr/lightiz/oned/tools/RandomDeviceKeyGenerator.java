package fr.lightiz.oned.tools;

public class RandomDeviceKeyGenerator {
    private static final int MAX_ID_LENGTH = 35;

    private static final String[] availableChars = setAvailableChars();

    public static String generateKey(){
        String result = "";
        int indexToAdd = 0;
        for(int i = 0; i < MAX_ID_LENGTH; i ++){
            indexToAdd = (int) (Math.random() * availableChars.length);
            result = result + availableChars[indexToAdd];
        }
        return result;
    }

    private static String[] setAvailableChars(){
        String allChars = "0123456789";
        return allChars.split("");
    }
}
