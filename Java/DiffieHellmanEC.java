// Diffie-Hellman on an Elliptic Curve
//  Coded by Thomas Simmons

import java.util.Arrays;

public class DiffieHellmanEC
{
    public static void main(String[] args)
    {
        long[] P = new long[3];
        long[] Q = new long[3];
        EllipticCurve ec = new EllipticCurve(61, -1, 0);
        long[] R = new long[3];
        
        P[2] = 1; Q[2] = 1;
        if (args.length != 0)
        {
            P[0] = Long.parseLong(args[0]); P[1] = Long.parseLong(args[1]);
            Q[0] = Long.parseLong(args[2]); Q[1] = Long.parseLong(args[3]);
        }
        else
        {
            P[0] = 18; P[1] = 18;
            Q[0] = 47; Q[1] = 36;
        }
        //R = ec.add(P, Q);
        
        R = simulateKeyExchange(P, Q[0], Q[1], ec);
        System.out.print(R[0] + " " + R[1]);
    }
    
    // Simulate a key exchange
    //in: Public point P, 'private' constants a and t
    //out: shared secret point atP
    public static long[] simulateKeyExchange( long[] pubP, long a, long t, EllipticCurve E )
    {
        long[] actor1 = new long[3];
        long[] actor2 = new long[3];
        long[] actor1result = new long[3];
        long[] actor2result = new long[3];
        long[] errorResult = {-1, -1, -1};
        
        // Actor 1 calculates aP
        for (int i=0; i<a; i++) actor1 = E.add(pubP, pubP);
        // Actor 2 calculates tP
        for (int i=0; i<t; i++) actor2 = E.add(pubP, pubP);
        
        // At this point Actors 1 and 2 would exchange calculated points
        // Actor 1 calculates a*tP
        for (int i=0; i<a; i++) actor1result = E.add(actor2, actor2);
        // Actor 2 calculates t*aP
        for (int i=0; i<t; i++) actor2result = E.add(actor1, actor1);
        
        if (Arrays.equals(actor1result, actor2result)) return actor1result;
        else return errorResult;
    }
    
    
}
