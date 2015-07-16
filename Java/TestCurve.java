// Test the Elliptic Curve over Fq 61
//  Brute force a generator over field, output points
//  The total points should be 71 (or implement Schoof's alg)

import java.math.BigInteger;
import java.util.Arrays;

public class TestCurve
{
    public static void main(String[] args)
    {
        long fieldP = 17;
        BigInteger[] P = new BigInteger[3];
        EllipticCurve ec = new EllipticCurve(BigInteger.valueOf(fieldP), BigInteger.valueOf(7), BigInteger.valueOf(0));
        P[2] = BigInteger.valueOf(1);
        BigInteger[][][] allPts = new BigInteger[41][41][2]; // Array to hold all generated points on curve
        //allPts[0][0] = BigInteger.ZERO;
        //BigInteger[][] generators = new BigInteger[100][3];
        long U;
        long[][] uVals = new long[29][6]; // Array of U vals that cause collisions
        long count = 0;
        
        // Constants for Icart's function
        BigInteger ts = BigInteger.valueOf(27).modInverse(ec.p);
        BigInteger th = BigInteger.valueOf(3).modInverse(ec.p);
        
        for (int i=0; i<41; i++)
        {
            for (int j=0; j<41;j++) allPts[i][j][0] = BigInteger.ZERO;
        }
        
        // User input parsing
        if (args.length != 0)
        {
            U = Long.parseLong(args[0]);
            //P[0] = Long.parseLong(args[0]); P[1] = Long.parseLong(args[1]);
        }
        else
        {
            U = 1;
            //P[0] = 18; P[1] = 18;
        }
        
        
        // Point generation and counting
        for (BigInteger x = BigInteger.ZERO; x.compareTo(ec.p) < 0; x = x.add(BigInteger.ONE))
        {
            P[0] = x;
            for (BigInteger y = BigInteger.ZERO; y.compareTo(ec.p) < 0; y = y.add(BigInteger.ONE))
            {
                P[1] = y;
                if ( ec.isPoint(P) )
                {
                    count++;
                    /*
                    allPts[0][0] = allPts[0][0].add(BigInteger.ONE);
                    allPts[ (int) allPts[0][0].longValue() ][0] = P[0];
                    allPts[ (int) allPts[0][0].longValue() ][1] = P[1];
                    */
                }
            }
        }
        //allPts[0][0] = allPts[0][0].add(BigInteger.ONE); 
        count++; // Add one for Point at Infinity
        System.out.println("Final point count: " + count);
        
        
        
        // Icart's function
        for ( ;U<fieldP;U++)
        {
            BigInteger bU = BigInteger.valueOf(U);
            BigInteger exp = ( (ec.p).multiply(BigInteger.valueOf(2)).subtract(BigInteger.ONE) ).multiply(BigInteger.valueOf(3).modInverse(ec.p)).mod(ec.p);
            BigInteger v = ( (BigInteger.valueOf(3).multiply(ec.a).subtract(bU.modPow(BigInteger.valueOf(4),ec.p)))
                            .multiply((BigInteger.valueOf(6).multiply(bU)).modInverse(ec.p)) ).mod(ec.p);
            //System.out.println("V val: " + v.toString());
            
            P[0] = ( v.pow(2).subtract(ec.b).subtract( ( bU.pow(6).multiply(ts) ).mod(ec.p) ).mod(ec.p) ).modPow(exp,ec.p)
                    .add(bU.pow(2).multiply(th)).mod(ec.p);
            
            P[1] = bU.multiply(P[0]).add(v).mod(ec.p);
            
            if (ec.isPoint(P))
            {
                //System.out.println(P[0].toString() + " " + P[1].toString() + " U val: " + bU.toString());
                if (allPts[ (int) P[0].longValue() ][ (int) P[1].longValue() ][0].equals(BigInteger.ZERO))
                {
                    allPts[ (int) P[0].longValue() ][ (int) P[1].longValue() ][0] =
                        allPts[ (int) P[0].longValue() ][ (int) P[1].longValue() ][0].add(BigInteger.ONE);
                    allPts[ (int) P[0].longValue() ][ (int) P[1].longValue() ][1] = bU;
                }
                else allPts[ (int) P[0].longValue() ][ (int) P[1].longValue() ][0] =
                    allPts[ (int) P[0].longValue() ][ (int) P[1].longValue() ][0].add(BigInteger.ONE);
            }
        }
        //System.out.println("Total missed: " + (allPts[0][0].longValue() - counter));
        
        for (int i=0; i<41; i++)
        {
            for (int j=0; j<41;j++)
            {
                if (!allPts[i][j][0].equals(BigInteger.ZERO))
                {
                    System.out.println("x: " + i + " y: " + j);
                    System.out.println("Appears: " + allPts[i][j][0].toString() + " U: " + allPts[i][j][1].toString());
                }
            }
        }
        
        
        /*
        //int genIndex = 0;
        //for (int g=1; g<allPts[0][0].intValue(); g++)
        //{
            //Q[0] = BigInteger.valueOf(13); Q[1] = BigInteger.valueOf(310);//allPts[g];
            Q = allPts[500];
            P = Q;
            System.out.println(Q[0] + " " + Q[1] + " " + P[0] + " " + P[1]);
            int i = 0;
            // Next two lines test if point only generates itself
            Q = ec.add(Q, Q); i++;
            //if (Arrays.equals(Q, allPts[g])) continue;
            // Otherwise, test if it is a generator allPts[g]
            while (i<1250 && !(Arrays.equals(Q, P)))
            {
                Q = ec.add(Q, Q);
                i++;
            }
            //if (i == allPts[0][0].intValue()) {generators[genIndex] = Q; genIndex++;} 
            System.out.println(i);//(Q[0] + " " + Q[1]);
        //}
        */
        
        //for (int i=0; i<genIndex; i++) System.out.println(generators[genIndex][0].toString() + " " + generators[genIndex][1].toString());
    }
    
    
}