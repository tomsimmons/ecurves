// Icart's function implementation
//  Coded by Thomas Simmons

#include "icart.h"
#include <iostream>

//using namespace NTL;


Icart::Icart(ZZ* p, ZZ_p* coA, ZZ_p* coB)
{
    setPrime(p);
    a = *coA; b = *coB;
}

// Sets the prime defining the field for the curve and stores certain values
void Icart::setPrime(ZZ* p)
{
    //ZZ_p::init(*p);
    // Icart hash function uses 1/3 root, which is equivalent to (2p-1)/3
    exp = MulMod( SubMod( MulMod(ZZ(2), *p, *p), ZZ(1), *p), InvMod(ZZ(3),*p), *p);
    // Store inverse values to be used later
    ts = inv(ZZ_p(27));
    th = inv(ZZ_p(3));
}

// Icart's hash function
EPoint Icart::hash(ZZ_p u)
{
    // 0 maps to the point at infinity
    if (IsZero(u))
    {
        return EPoint(ZZ_p(0), ZZ_p(0), true);
    }
    
    // v = (3a - u^4) / 6u
    ZZ_p v = ((ZZ_p(3) * a) - power(u, 4)) * inv(ZZ_p(6) * u);
    // x = (v^2 - b - u^6/27)^(1/3) + u^2/3
    ZZ_p x = power( sqr(v) - b - (power(u, ZZ(6)) * ts), exp) + (sqr(u) * th);
    // y = ux + v
    ZZ_p y = (u * x) + v;
    
    return EPoint(x, y, false);
}
