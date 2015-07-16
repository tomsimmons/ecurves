// Elliptic Curve Point Implementation
//  Coded by Thomas Simmons

#ifndef __POINT_H__
#define __POINT_H__

//#include <NTL/ZZ.h>
#include <NTL/ZZ_p.h>
#include <stdio.h>

using namespace NTL;

class EPoint
{
private:
    static ZZ_p a,b; // a and b coefficients for a curve

public:
    ZZ_p x,y;        // The actual point coordinates
    bool PaI;        // Switch that indicates this is the point at infinity
    
    EPoint(ZZ_p, ZZ_p, bool);
    EPoint(const EPoint &pt);
    static void setPrime(ZZ);
    void setCoord(ZZ_p, ZZ_p, bool);
    static bool setCoeff(ZZ_p, ZZ_p);
    bool isPoint();
    void add(EPoint);
    EPoint operator+(EPoint);
    //EPoint operator=(
    void print();
    uint64_t toLongLong();
};

#endif
