// Icart's function implementation
//  Coded by Thomas Simmons

#ifndef __ICART_H__
#define __ICART_H__

#include <NTL/ZZ.h>
#include <NTL/ZZ_p.h>
#include "epoint.h"

//using namespace NTL;

class Icart
{
private:
    ZZ_p ts, th; // 27 and 3 inverse for the set prime
    ZZ exp;      // The exponent for the hash function
    ZZ_p a, b;   // Coefficients for the elliptic curve

public:
    Icart(ZZ*, ZZ_p*, ZZ_p*);
    void setPrime(ZZ*);
    EPoint hash(ZZ_p);
};

#endif