
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Main<br>
 * @author pabvald
 * 
 * This class provides a text interface needed to use the Gerrymandering class. 
 * The configuration of the precincts must be written in a text file
 * with the following structure:<br>
 *      - Number of precincts, n. (Line 1)<br>
 *      - Popullation of each precinct, m. (Line 2)<br>
 *      - Votes to party A in each of the n precincts, n integer values separated by spaces. (Line 3) <br>
 *      - Adjacencies matrix, an n x n matrix. Each row of the matrix must be written in a new line.
 *        Each column must be separated by one space. The position [i][j] will be a 1 if the precinct i is 
 *        adjacent to precint j, or a 0 if precinct i is NOT adjacent to precinct j.
 * 
 */
public class Main {

    private static Scanner reader;  // File reader
    private static File file;
    private static Scanner in = new Scanner(System.in); // Keyboard input

    public static void main(String[] args) {
        String fileName;
        int[] votesA;         // Votes to party A in each of the n precincts     
        int[][] adjacencies;  // Adjacencies matrix
        Integer[][] g;
        int n = -1;     // Number of precincts
        int m = -1;     //Population in the precincts    
        int votesD1, votesD2;      
        String tmpVotes = null;
        ArrayList<String> tmpAdj = null;

        println("************** GERRYMANDER **************\n");
        print("> File route: ");
        if(args.length == 1) {
            fileName = args[0];
        } else {
            fileName = in.nextLine();
        }
        println("");

        file = new File(fileName);

        try {           
            reader = new Scanner(file);
            n = Integer.parseInt(reader.nextLine());  
            m = Integer.parseInt(reader.nextLine());
            tmpVotes = reader.nextLine();

            tmpAdj = new ArrayList<String>(n);
            for(int i = 0; i < n; i++) {               
                tmpAdj.add(reader.nextLine());
            }
        } catch(Exception e) {
            println(e.getMessage());

        } finally {
            if (n%2 != 0 || n <= 0) {
                throw new IllegalArgumentException("The number of precincts must be positive and even");
            }
            if (m <= 0) {
                throw new IllegalArgumentException("The popullation of each precinct must greater or equal than 1");
            }
           
            votesA = getVotesA(n,tmpVotes);
            adjacencies = getAdjacenciesMatrix(n,tmpAdj); 
        }        
        
        if ( (g = Gerrymandering.gerrymander(n, m, votesA,adjacencies)) != null) {
            votesD1 = 0;
            votesD2 = 0;
            for(int i : g[0])
                votesD1 += votesA[i-1];
            for(int j : g[1])
                votesD2 += votesA[j-1];
            println("\nIt's GERRYMANDERABLE !!");
            print("D1: ");
            printArray(g[0]);
            println(" with  " + votesD1 + "  for party A.");
            print("D2: ");
            printArray(g[1]);
            println(" with  "+ votesD2 + "  for party A.");
            

        } else {
            println("It's  NOT GERRYMANDERABLE...");
        }
    }

    /**
     * It converts to int the values of the adjacencies matrix read from the file.
     * @param adjStr adjacencies matrix in String format.
     */
    private static int[][] getAdjacenciesMatrix(int n, ArrayList<String> adjStr) {
        int[][] adjacencies = new int[n][n];
        int tmp;
        String line;
        String[] splits;

        for(int i = 0; i <  n; i++) {
            line = adjStr.get(i);
            splits = line.split(" ");
            for(int j = 0; j < n; j++) {
                tmp = Integer.parseInt(splits[j]);
                if(tmp != 0 && tmp != 1) {
                    throw new IllegalArgumentException("The adjacency matrix does not have the correct format");
                }
                adjacencies[i][j] = tmp;
            }
        }
        return adjacencies;
    }

    /**
     * It obtains the array of the votes to party A in each precinct from 
     * a String containing those values. 
     * @param n number of precincts
     * @param votesStr contains the values of the votes to party A
     */
    private static int[] getVotesA(int n, String votesStr) {
        String[] splits = votesStr.split(" ");
        int[] votesA = new int[n];

        for(int i= 0; i < n; i++) 
            votesA[i] = Integer.parseInt(splits[i]);
        
        return votesA;
    }



    /* PRINTING METHODS */

    /**
     * It prints the representation of an Object.
     * @param {*} o its representation is printed
     */
    private static void print(Object o) {
        System.out.print(o);
    }

    /**
     * It prints the representation of an Object and a new line.
     * @param o its representation is printed
     */
    private static void println(Object o) {
        System.out.println(o);
    }

    /**
     * It prints the content of an array of Objects, separating each element 
     * by one space.
     * @param a array of Objects to be printed
     */
    private static void printArray(Object[] a) {
        for(Object i : a) {
            print(i + " ");
        }
    }
}