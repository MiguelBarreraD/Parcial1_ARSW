package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

        // Calcula la cantidad de trabajo por hilo
        int cantidad = count / number;
        // Variable para almacenar la posición de inicio de cada hilo
        int inicio;
        
        for (int i = 0; i < number; i++) {
            inicio = start + cantidad * i;   
            if (i == number - 1) { // Si es el último hilo, ajusta la cantidad 
                cantidad = count - cantidad * i;
            }
             // Se crea un objeto DigitsThread con la posición de inicio y la cantidad de trabajo
            DigitsThread thread = new DigitsThread(inicio, cantidad);
            threads.add(thread);
        }

        for (DigitsThread thread : threads) {
            thread.start();
        }

        // Se crea una bandera "live" que se vuelve falsa cuando todos los hilos terminan
        boolean live = true;

        // Bucle principal que se ejecuta hasta que todos los hilos hayan terminado
        while (live) {
            try {
                // Espera 5 segundos antes de realizar una operación
                Thread.sleep(5000);
                for (DigitsThread thread : threads) { // → Recorre la lista de hilos y acumula los resultados parciales en la variable "digits"
                    digits += bytesToHex(thread.getDigits());
                    thread.pause();
                }
                
                System.out.println(digits); // → Imprime los resultados parciales
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            System.out.println("Presiona Enter para continuar...");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            // Reanuda todos los hilos 
            for (DigitsThread thread : threads) {
                thread.conti();
            }

            // Verifica si los hilos ya terminaron
            for (DigitsThread thread : threads) {
                if (!thread.isAlive()) {
                    live = false;
                }
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