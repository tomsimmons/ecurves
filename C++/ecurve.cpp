// Elliptic Curve Implementation
//  Coded by Thomas Simmons

#include "ecurve.h"

using namespace NTL;

ECurve::ECurve(ZZ prime, ZZ_p coA, ZZ_p coB)
{
    p = prime;
    ZZ_p::init(p);
    a = coA;
    b = coB;
}

// Set the prime to form the finite field
void ECurve::setPrime(ZZ prime)
{
    p = prime;
    ZZ_p::init(p);
}

// Set the coefficients of the curve
// Returns true on success
bool ECurve::setCoeff(ZZ_p coA, ZZ_p coB)
{
    if (!checkDisc(coA, coB))
    {
        a = coA; b = coB;
        return true;
    }
    return false;
}

// Checks that the discriminent is nonzero, where disc takes form -16(4a^3 + 27b^2)
// Returns true if the discriminant is zero
bool ECurve::checkDisc(ZZ_p coA, ZZ_p coB)
{
    return IsZero( ZZ_p(-16) * ((ZZ_p(4) * power(coA,3)) + (ZZ_p(27) * sqr(coB))) );
}

// Checks if the given values are a point on the curve
bool ECurve::isPoint(ZZ_p x, ZZ_p y)
{
    return true;
}

// Adds two points on the curve
//ZZ_p ECurve::add(
