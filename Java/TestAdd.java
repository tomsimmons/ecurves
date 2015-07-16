// Elliptic Curve unit test
//  Tests addition

import java.math.BigInteger;
import java.util.Arrays;

public class TestAdd
{
    public static void main(String[] args)
    {
        EllipticCurve ec = new EllipticCurve(BigInteger.valueOf(61), BigInteger.valueOf(60), BigInteger.valueOf(0));
        BigInteger[] P = {new BigInteger("10"),new BigInteger("21"),new BigInteger("1")};
        BigInteger[] Q = {new BigInteger("38"),new BigInteger("42"),new BigInteger("1")};
        BigInteger[] result;
        
        result = ec.add(P,Q);
        if (result[0].equals(BigInteger.valueOf(25)) && result[1].equals(BigInteger.valueOf(44)))
            System.out.println("Add successful: (10,21) + (38,42) = (25,44)");
        else
            System.out.println("Trouble adding (10,21) and (38,42).");
        
        
    }
}