// Elliptic Curve Implementation
//  Coded by Thomas Simmons

#ifndef __ECURVE_H__
#define __ECURVE_H__

#include <NTL/ZZ.h>
#include <NTL/ZZ_p.h>

using namespace NTL;

class ECurve
{
private:
    ZZ p;       // Prime to form the finite field
    ZZ_p PaI;   // Point at infinity
    ZZ_p a,b;   // Coefficients of the curve formula in Weirstrass form
    bool checkDisc(ZZ_p, ZZ_p);

public:
    ECurve(ZZ, ZZ_p, ZZ_p);
    void setPrime(ZZ);
    bool setCoeff(ZZ_p, ZZ_p);
    bool isPoint(ZZ_p, ZZ_p);
    ZZ_p add(ZZ_p, ZZ_p);
};

#endif