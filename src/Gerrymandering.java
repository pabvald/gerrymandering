import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Gerrymandering<br>
 * 
 * @author pabvald
 * 
 * This class implements the Gerrymandering algorithm using Dynamic Programming.
 */
public class Gerrymandering {

    /**
     * It determines if 'gerrymandering' a given set of n precincts in 2 districts is possible, 
     * and, if it is possible, obtains the 'gerrymander' 
     * @param n number of precincts. n must be EVEN.
     * @param m popullation of each precinct.
     * @param votesA  votes to party A in the n precincts.
     * @return true if it's possible to 'gerrymander' the n precincts.
     */
    public static Integer[][] gerrymander(int n, int m, int[] votesA, int[][] adjacencies) {
        int j,k,x,y;
        int mn = m * n;
        int mayority = mn/4 + 1;   
        boolean gerrymanderable;
        Integer[][] gerrymander = null;

        byte[][][][] c = new byte[n][(n/2)+1][mn+1][mn+1]; // Table of results
        boolean[][][][] s = new boolean[n][(n/2)+1][mn+1][mn+1]; //Table of states
        
        for(j = 0; j < n; j++) {
            for(k = 0; k <= n/2; k++) {
                for(x = 0; x <= mn; x++ ) {
                    for(y = 0; y <= mn; y++) {
                        if(j == 0) {
                           if (x == votesA[j] && k == 1 && y == 0) {
                                s[j][k][x][y] = true;
                                c[j][k][x][y] = 1;
                            } else if ( y == votesA[j] && k==0 && x==0) {
                                s[j][k][x][y] = true;
                                c[j][k][x][y] = 2;
                            } else {
                                s[j][k][x][y] = false;
                           }
                        } else {                            
                            if (x >= votesA[j] && k>= 1 && s[j-1][k-1][x-votesA[j]][y]) {
                                s[j][k][x][y] = true;
                                c[j][k][x][y] = 1;
                            } else if ( y >= votesA[j] && s[j-1][k][x][y-votesA[j]]) {
                                s[j][k][x][y] = true;
                                c[j][k][x][y] = 2;
                            } else {
                                s[j][k][x][y] = false;
                            }                            
                        }
                    }
                }
            }
        }

        gerrymanderable = false; // Control variable    

        for(x = mayority; x <= mn && !gerrymanderable; x ++) {
            for(y = mayority; y <= mn && !gerrymanderable; y++) {
                if(s[n-1][n/2][x][y]) {     
                    gerrymander = getGerryMander2Districts(n,c,votesA,x,y);
                    if(validGerrymander(n,gerrymander,adjacencies)) 
                        gerrymanderable = true;                      
                }
            }
        }
        return gerrymander;
    }

    /**
     * It obtains the gerrymander of two districts from the table of results.
     * @param votesA array with the the votes to party A in each precinct
     * @param c table of results
     * @param n number of precincts
     * @param xFinal number of votes to party A in district D1
     * @param yFinal number of votes to party A in district D2
     * @return 'gerrymander' of the n precincts in two districts
     * @throws IllegalStateException if the table c contains an invalid value.
     */
    private static Integer[][] getGerryMander2Districts
                            (int n, byte[][][][]c, int[] votesA, int xFinal, int yFinal)
                            throws IllegalStateException {
        int j = n-1;
        int k = n/2;
        int x = xFinal;
        int y = yFinal;
        Integer[][] gerrymander = new Integer[2][n/2]; // 2 districts of n/2 precincts each
        ArrayList<Integer> d1 = new ArrayList<>(); 
        ArrayList<Integer> d2 = new ArrayList<>();

        while (j >= 0) {
            if (c[j][k][x][y] == 1) {
                d1.add(j+1);
                k--;
                x -= votesA[j];
            } else if (c[j][k][x][y] == 2) {
                d2.add(j+1);
                y -= votesA[j];
            } else {
                throw new IllegalStateException("c["+j+","+k+","+x+","+y+"]");
            }
            j--;
        }
        
        d1.toArray(gerrymander[0]);
        d2.toArray(gerrymander[1]);
        return gerrymander;
    }

    /**
     * It determines if a possible 'gerrymander' is valid, where valid means that the precincts in
     * both districts are adjacent.
     * @param gerrymander possible 'gerrymander' of the precincts in two districts
     * @param adjacencies adjacencies between the precincts
     * @return true if the possible 'gerrymander' is valid
     */
    private static boolean validGerrymander(int n, Integer[][] gerrymander, int[][] adjacencies) {
        TreeSet<Integer> d1 = new TreeSet<>();
        TreeSet<Integer> d2 = new TreeSet<>();
        TreeSet<Integer> adjacencies_d1 = new TreeSet<>();
        TreeSet<Integer> adjacencies_d2 = new TreeSet<>();

        for(int i : gerrymander[0]) {
            d1.add(i);
            for(int j = 0; j < n; j ++) {
                if(adjacencies[i-1][j] == 1)
                    adjacencies_d1.add(j+1);
            }
        }

        if( !adjacencies_d1.containsAll(d1)) { return false; }

        for(int i : gerrymander[1]) {
            d2.add(i);
            for(int j = 0; j < n; j ++) {
                if(adjacencies[i-1][j] == 1)
                    adjacencies_d2.add(j+1);
            }
        }

        if( !adjacencies_d2.containsAll(d2)) { return false; } 

        return true;
    }
}