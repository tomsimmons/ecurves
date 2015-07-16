/* Icart's function testing
*  Brute force a point count then implement Icart's function and count collisions
*    Coded by Thomas Simmons
*  
*  Usage: This code can count collisions for Icart or hash a single point
*  Show all points with 1 or more collisions on the curve over the prime field 17
*    icart collide 17 1
*  Hash the given value 6 to a point on the curve over the prime field of 17
*    icart hash 17 6
*/

import java.math.BigInteger;
import java.util.Arrays;

public class Icart
{
    // Declare constants for Icart's function
    static BigInteger ts;
    static BigInteger th;
    
    public static void main(String[] args)
    {
        // Our elliptic curve. Here over (default) 29 where y^2 = x^3 - x
        EllipticCurve ec = new EllipticCurve(BigInteger.valueOf(29), BigInteger.valueOf(7), BigInteger.valueOf(0));
        long fieldP = 29;    // The field over which the E curve is generated
        long lowerBound = 0; // Lower bound for times a point must appear to be printed [line 133]
        long U = 1;          // Value to encode on the curve
        char mode;           // Determines code action. 0 = Count collisions, 1 = hash a given u val
        
        // User input parsing
        if (args.length != 0)
        {
            // The collide argument will count collisions on a given curve
            //  User must supply two additional arguments, a field prime and the lower bound for collision output
            if ( (args[0].compareTo("collide")) == 0 )
            {
                mode = 1;
                fieldP = Long.parseLong(args[1]);
                lowerBound = Long.parseLong(args[2]);
            }
            // The hash argument will hash a user-supplied u value only a given curve
            //  User must supply two additional arguments, a field prime and a value to encode
            else if ( (args[0].compareTo("hash")) == 0 )
            {
                mode = 2;
                fieldP = Long.parseLong(args[1]);
                U = Long.parseLong(args[2]);
            }
            // The user has supplied an incorrect argument
            else
            {
                mode = 0;
                System.out.println("Invalid argument given");
            }
        }
        // Default to checking all collisions with default prime
        else
        {
            mode = 1;
            lowerBound = 0;
        }
        
        if (mode != 0)
        {
            ec.setPrime(BigInteger.valueOf(fieldP));
            // Initialize constants for Icart's function
            ts = BigInteger.valueOf(27).modInverse(ec.p);
            th = BigInteger.valueOf(3).modInverse(ec.p);
        }
        
        BigInteger[] P = new BigInteger[3];  // The point used in calculations [x, y, 1]
        //P[2] = BigInteger.valueOf(1);        // Bring point into the projective plane
        
        if (mode == 1)
        {
            // Array to hold all Icart results where [x][y][0] = # of (x,y) results
            //  and [x][y][1..5] = U vals that generated (x,y)
            long[][][] allPts = new long[(int)fieldP][(int)fieldP][6];
            long count = 0;   // The count of points
            int  misses = 0;  // The count of values Icart produces that are not points
            
            // Initialize Icart results to -1
            for (int i=0; i<(int)fieldP; i++)
            {
                for (int j=0; j<(int)fieldP;j++) allPts[i][j][0] = -1;
            }
            
            // Point generation and counting by brute force
            for (BigInteger x = BigInteger.ZERO; x.compareTo(ec.p) < 0; x = x.add(BigInteger.ONE))
            {
                if (ec.isX(x))
                {
                    P[0] = x;
                    for (BigInteger y = BigInteger.ZERO; y.compareTo(ec.p) < 0; y = y.add(BigInteger.ONE))
                    {
                        P[1] = y;
                        if ( ec.isPoint(P) )
                        {
                            count++;
                            int indexX = (int) P[0].longValue();
                            int indexY = (int) P[1].longValue();
                            allPts[ indexX ][ indexY ][0] = 0;
                        }
                    }
                }
            }
            count++; // Add one for Point at Infinity
            System.out.println("Final point count: " + count);
        
            // Icart's function
            for ( ;U<fieldP;U++)
            {
                P = icart(U, ec);
                
                if (ec.isPoint(P))
                {
                    int indexX = (int) P[0].longValue();
                    int indexY = (int) P[1].longValue();
                    //System.out.println(indexX + " " + indexY + " U val: " + U);
                    
                    // Increment the number of times the point is observed and save the U val
                    allPts[ indexX ][ indexY ][0]++;
                    allPts[ indexX ][ indexY ][ (int) allPts[ indexX ][ indexY ][0] ] = U;
                }
            }
        
            // Print out all points based on how many times Icart's function generated them
            //  along with the U values that encoded to the point in question
            for (int i=0; i<(int)fieldP; i++)
            {
                for (int j=0; j<(int)fieldP;j++)
                {
                    if (allPts[i][j][0] > lowerBound)
                    {
                        System.out.println("( " + i + "," + j + " ) appears " + allPts[i][j][0] + " time(s).");
                        System.out.print("U val(s): ");
                        for (int k=1; k<allPts[i][j][0]+1; k++)
                        {
                            if (k==allPts[i][j][0]) System.out.print(allPts[i][j][k] + "\n");
                            else System.out.print(allPts[i][j][k] + ", ");
                        }
                        System.out.println(" ");
                    }
                    else if (allPts[i][j][0] == 0)
                    {
                        System.out.println("( " + i + "," + j + " ) was missed by the hash function.");
                    }
                }
            }
        }
        
        if (mode == 2)
        {
            P = icart(U, ec);
            System.out.println( U + " -> (" + P[0] + "," + P[1] + ")" );
        }
    }
    
    // Icart's function
    public static BigInteger[] icart(long uVal, EllipticCurve c)
    {
        BigInteger[] point = new BigInteger[3]; // The point to be returned that we hash to
        point[2] = BigInteger.valueOf(1);       // Bring point into the projective plane
        
        BigInteger bigU = BigInteger.valueOf(uVal);
        
        // BigInt value of exponent, (2p-1)/3
        BigInteger exp = ( (c.p).multiply(BigInteger.valueOf(2)).subtract(BigInteger.ONE) ).multiply(BigInteger.valueOf(3).modInverse(c.p)).mod(c.p);
        BigInteger v = ( (BigInteger.valueOf(3).multiply(c.a).subtract(bigU.modPow(BigInteger.valueOf(4),c.p)))
                         .multiply((BigInteger.valueOf(6).multiply(bigU)).modInverse(c.p)) ).mod(c.p);
        //System.out.println("V val: " + v.toString());
        
        point[0] = ( v.pow(2).subtract(c.b).subtract( ( bigU.pow(6).multiply(ts) ).mod(c.p) ).mod(c.p) ).modPow(exp,c.p)
                      .add(bigU.pow(2).multiply(th)).mod(c.p);
        point[1] = bigU.multiply(point[0]).add(v).mod(c.p);
        
        return point;
    }
    
}