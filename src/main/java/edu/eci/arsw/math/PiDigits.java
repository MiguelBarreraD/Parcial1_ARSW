package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.List;

///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    

    
    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public static String getDigits(int start, int count, int number) {

        List<DigitsThread> threads = new ArrayList<>();

        String digits = ""; 
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        // Comienza el primero en strat
        int finalAnterior;
        int cantidad = count/number;
        int inicio;
        for (int i=0; i < number; i++){
            inicio = start + cantidad  * i;
            if (i == number - 1){
                cantidad = count - cantidad*i;
            }
            DigitsThread thread = new DigitsThread(inicio , cantidad);
            threads.add(thread);
        }
        
        for (DigitsThread thread : threads){
            thread.start();
            try {
                thread.join();
                digits += bytesToHex(thread.getDigits());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return digits;
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<hexChars.length;i=i+2){
            //sb.append(hexChars[i]);
            sb.append(hexChars[i+1]);            
        }
        return sb.toString();
    }

    
}
