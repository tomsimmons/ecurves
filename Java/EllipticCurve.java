/* Elliptic curve class
*  A basic class for elliptic curve math (addition)
*    Coded by Thomas Simmons
*/


import java.math.BigInteger;
import java.util.Arrays;

public class EllipticCurve
{
    public BigInteger p;    // Prime to form the finite field
    public BigInteger a, b; // Coefficients of the curve formula in Weirstrass form
    private BigInteger[] PaI = {new BigInteger("0"),new BigInteger("1"),new BigInteger("0")}; // Point at Infinity
    
    public EllipticCurve(BigInteger prime, BigInteger coA, BigInteger coB)
    {
        this.setPrime(prime);
        if (!this.checkDisc(coA, coB))
        {
            this.setCoeff(coA, coB);
        }
    }
    
    // Set the prime to form the finite field
    public void setPrime( BigInteger prime )
    {
        this.p = prime;
    }
    
    // Set the coefficients of the curve
    public void setCoeff( BigInteger coA, BigInteger coB )
    {
        coA = coA.mod(this.p);
        coB = coB.mod(this.p);
        if (!this.checkDisc(coA, coB))
        {
            this.a = coA; this.b = coB;
        }
    }
    
    // Adds two points on curve
    //in: two int arrays where index 0 is the x coord and index 1 is the y
    //out: an int array for the sum point with the same form as input
    public BigInteger[] add( BigInteger[] P, BigInteger[] Q )
    {
        BigInteger[] R = new BigInteger[3];
        
        // P + Point at Infinity = P
        if (Arrays.equals(P, this.PaI) || Arrays.equals(Q, this.PaI))
        {
            R = (Arrays.equals(P, this.PaI)) ? Q : P;
        }
        // P + (-P) = Point at Infinity
        else if (P[0].equals(Q[0]) && P[1].equals(Q[1].negate()))
        {
            R = this.PaI;
        }
        // P + P
        else if (P[0].equals(Q[0]) && P[1].equals(Q[1]))
        {
            BigInteger n = P[0].pow(2).multiply(BigInteger.valueOf(3)).add(this.a).multiply(P[1].shiftLeft(1).modInverse(this.p));
            R[0] = (n.pow(2).subtract(P[0].shiftLeft(1))).mod(this.p);
            R[1] = (n.multiply(P[0].subtract(R[0])).subtract(P[1])).mod(this.p);
            R[2] = new BigInteger("1"); // Used to bring affine points into projective plane
        }
        // P + Q
        else
        {
            BigInteger n = Q[1].subtract(P[1]).multiply(Q[0].subtract(P[0]).modInverse(this.p));
            R[0] = (n.pow(2).subtract(P[0]).subtract(Q[0])).mod(this.p);
            R[1] = (n.multiply(P[0].subtract(R[0])).subtract(P[1])).mod(this.p);
            R[2] = new BigInteger("1"); // Used to bring affine points into projective plane
        }
        
        return R;
    }
    
    // Checks that the discriminent is nonzero, where disc takes form -16(4a^3 + 27b^2)
    private boolean checkDisc( BigInteger coA, BigInteger coB )
    {
        return (coA.pow(3).multiply(BigInteger.valueOf(4))
                   .add(coB.pow(2).multiply(BigInteger.valueOf(27)))
                   .multiply(BigInteger.valueOf(16).negate()))
                   .equals(BigInteger.ZERO);
    }
    
    // Checks if the provided point falls on the curve or is the point at inffinity
    public boolean isPoint( BigInteger[] P )
    {
        if (Arrays.equals(P, this.PaI)) return true;
        else
        {
            BigInteger ySide = (P[1].modPow(BigInteger.valueOf(2), this.p)).mod(this.p);
            BigInteger xSide = (P[0].modPow(BigInteger.valueOf(3), this.p).add(P[0].multiply(this.a)).add(this.b)).mod(this.p);
            return ySide.equals(xSide);
        }
    }
    
    // Calculates the Legendre symbol to determine if the given x is valid
    public boolean isX( BigInteger x )
    {
        // Calculate the right-hand side of the elliptic curve equation x^3 + ax + b
        BigInteger rhs = x.modPow(BigInteger.valueOf(3), this.p).add(this.a.multiply(x)).add(this.b).mod(this.p);
        // Determine whether the right-hand side has a quadratic residue (i.e. a corresponding y^2)
        int lsym = rhs.modPow((this.p.subtract(BigInteger.ONE).shiftRight(1)),this.p).intValue();
        return (lsym != -1);
    }
    
}