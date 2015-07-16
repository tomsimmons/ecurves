// A verification of the size of Icart's image
//  Coded by Thomas Simmons

#include "epoint.h"
#include "icart.h"
#include <stdio.h>
#include <unordered_map>

//using namespace NTL;
using namespace std;

int main(int argc, char* args[])
{
    unordered_map<uint64_t, int> points;
    ZZ prime;
    ZZ_p ayy, bee;
    unordered_map<uint64_t, int> specialCases;

    if (argc != 4)
    {
        cout << "This program takes three arguments exactly" << endl;
        cout << "A prime, an A coefficient, and a B coefficient" << endl;
        return 1;
    }
    else
    {
        prime = conv<ZZ>(args[1]);
        ZZ_p::init(prime);
        ayy = conv<ZZ_p>(conv<ZZ>(args[2]));
        bee = conv<ZZ_p>(conv<ZZ>(args[3]));
    }
    
    // Create new Icart hash object with appropriate parameters
    Icart ic = Icart(&prime,&ayy,&bee);
    // Bound the loop by p/2 (convert from ZZ to long)
    unsigned long bound = conv<unsigned long>(conv<ZZ>(args[1]));
    bound = (bound / 2) + 1;
    for (uint64_t u=1; u<bound; u++)
    {
        //cout << "u: " << u << endl;
        // Call hash function
        EPoint R = ic.hash(ZZ_p(u));
        //R.print(); cout << endl;
        //cout << hex << R.toLongLong() << endl;
        
	// Acount for special cases
	if (IsZero(R.y) &&
	    IsZero( conv<ZZ_p>(power(conv<ZZ>(u),4)) - (conv<ZZ_p>(6) 
	    * R.x * sqr(conv<ZZ_p>(u))) - (conv<ZZ_p>(3) * ayy) ) )
	{
            specialCases.emplace(R.toLongLong(), 1);
	}
	else
	{
	    // Blank the y coordinate because we want to count only one inverse
	    //  per half-field. See README to know what this even means
	    uint64_t halfPoint = R.toLongLong() & 0xFFFFFFFF00000000;
	    // Add point to hash map
            points.emplace(halfPoint, 1);
	}
    }
    //cout << "Special Cases: " << specialCases.size() << endl;
    cout << "Points hit: " << ((points.size() * 2) + specialCases.size() ) << endl;
    return 0;
}
