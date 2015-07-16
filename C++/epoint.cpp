// Elliptic Curve Point Implementation
//  Coded by Thomas Simmons

#include "epoint.h"

using namespace NTL;

ZZ_p EPoint::a; ZZ_p EPoint::b;

// Point constructor. It is left to the user to handle x,y vals in the PaI case
EPoint::EPoint(ZZ_p iX, ZZ_p iY, bool ptAinf)
{
    x = iX; y = iY; PaI = ptAinf;
}

// Copy constructor
EPoint::EPoint(const EPoint &pt)
{
    x = pt.x; y = pt.y; PaI = pt.PaI;
}

// Sets the prime defining the field for the curve
void EPoint::setPrime(ZZ p)
{
    ZZ_p::init(p);
}

// Sets the coordinates of the point or change to PaI
void EPoint::setCoord(ZZ_p iX, ZZ_p iY, bool ptAinf)
{
    x = iX; y = iY; PaI = ptAinf;
}

// Sets the coefficients of the curve over which these points exist
//  Returns true on success, false if discriminant is zero
bool EPoint::setCoeff(ZZ_p coA, ZZ_p coB)
{
    if (!IsZero( ZZ_p(-16) * ((ZZ_p(4) * power(coA,3)) + (ZZ_p(27) * sqr(coB))) ))
    {
        a = coA; b = coB;
        return true;
    }
    return false;
}

// Checks if the point is valid over the given curve
//  Returns true if y^2 = x^3 + ax + b
bool EPoint::isPoint()
{
    ZZ_p ySide = sqr(y);
    ZZ_p xSide = power(x,3) + (a * x) + b;
    
    return ySide == xSide;
}

// Adds two points together as P += Q
void EPoint::add(EPoint Q)
{
    ZZ_p yNeg;
    negate(yNeg, Q.y); // We will need this later
    
    // P + Point at Infinity = P
    if (PaI || Q.PaI)
    {
        // If Q is the PaI, leave P unchanged. If P is PaI, P now equals Q
        if (PaI) setCoord(Q.x, Q.y, Q.PaI);
    }
    // P + (-P) = Point at Infinity
    else if (x == Q.x && y == yNeg)
    {
       PaI = true;
    }
    // P + P
    else if (x == Q.x && y == Q.y)
    {
        ZZ_p xNew = sqr((3*sqr(x) + a) * inv(2 * y)) - (2 * x);
        y = yNeg + (((3*sqr(x) + a) * inv(2 * y)) * (x - xNew));
        x = xNew;
    }
    // P + Q
    else
    {
        negate(yNeg, y);
        ZZ_p xNew = sqr((Q.y - y) * inv(Q.x - x)) - x - Q.x;
        y = yNeg + (((Q.y - y) * inv(Q.x - x)) * (x - xNew));
        x = xNew;
    }
}

// Adds two points together as R = P + Q
//  Returns the value R
EPoint EPoint::operator+(EPoint Q)
{
    EPoint R(a,b,PaI); // Create a copy of this point, P
    R.add(Q);
    
    return R;
}

// Prints the point to standard output
void EPoint::print()
{
    std::cout << "(" << x << "," << y << ")";
}

// Returns a 64 bit int where the higher order 32 bits are x and the others are y
uint64_t EPoint::toLongLong()
{
    // Copy in x coord and shift left by 32
    uint64_t output = 0;
    output ^= conv<long>(x);
    output<<=32;
    // Copy in y coord
    output ^= conv<long>(y);
    
    return output;
}
