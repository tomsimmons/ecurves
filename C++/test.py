# Python script to automate Icart function coverage
#  The magic inequality we seek is |N - (5/8)q| <= 55q^(1/2)

from math import sqrt
from subprocess import Popen,PIPE
from sys import argv
import linecache

# UNTIL WE OPTIMIZE, DO SINGLE PRIME FILE AND CONDUCTOR LIST
# Prime file line splits
linesPerJob = [280,79,130,197,137,227]
# Which prime file are we doing?
pf = 0 #CHANGE BACK COND RANGE,SUPRESS OUTPUT,RM BREAK

# Open primes file
#primes = open("data/IcartPrimes" + argv[1] + ".txt","r")
primes = open("data/IcartPrimes" + str(pf) + ".txt","r")
# Open Cremona data file
#conductors = open("data/conductors/cremona." + argv[1] + "0000-" + argv[1] + "9999","r")
conductors = open("data/conductors/cremona.00000-09999","r")
# Counter for number of times inequality holds
losses = 0

# For all of Icart's primes, test the inequality
#for num in primes:

# CHANGE THIS PER PRIME FILE
rangeStart = linesPerJob[pf] * int(argv[1])
rangeEnd = rangeStart + linesPerJob[pf]
for p in xrange(rangeStart, rangeEnd):
    
    # DELETE AFTER OPTIMIZE
    num = linecache.getline(primes.name,p+1)
    
    # Make prime into string and usable number
    primeS = num.rstrip('\r\n')
    try:
        primeN = int(primeS)
    except ValueError:
        print("Incorrect processing for prime: " + primeS)
	continue

    # Pull in repeated conductors
    for c in xrange(100,101):
        
        line = linecache.getline(conductors.name, c+1)
        
        # Process Cremona data, which starts in form "n [a1,a2,a3,a4,a6]"
        cond = line.split(" ")
        cond[1] = cond[1].lstrip('[').rstrip(']\n')
        a = cond[1].split(",")
        for i in range(5):
            a[i] = int(a[i])
        
        # Find A,B from long form coefficients
        b2 = pow(a[0],2,primeN) + 4*a[1] % primeN
        b4 = a[0]*a[2] + 2*a[3] % primeN
        b6 = pow(a[2],2,primeN) + 4*a[4] % primeN
        
        c4 = pow(b2,2,primeN) - 24*b4 % primeN
        c6 = (-1)*pow(b2,3,primeN) + 36*b2*b4 - 216*b6 % primeN
        
        inv48 = pow(48,primeN-2,primeN)
        inv864 = pow(864,primeN-2,primeN)
        
        A = (-1) * c4 * inv48
        B = (-1) * c6 * inv864
        
        # Access primes list and cull next number
        # Code - access file, format into array ['prog name','prime','coA','coB']
        #prime = num.rstrip('\r\n')
        cmd = ["./icart",primeS,str(A),str(B)]
        
        # Call each program separately, pull out relevant numbers
        output = []
        
        #  Call Icart function
        p = Popen(cmd, stdout=PIPE)
        #  Grab stdout, decode to string, then slice to grab point count
        out = p.communicate()[0].lstrip('Pointsh: ').rstrip('\r\n')
        try:
            output.append(int(out))
        except ValueError:
            print("Invalid Icart output for prime ",prime)
            continue
        
        #  Switch to Schoof program
        #cmd[0] = "./schoof"
        #  Call Schoof's algorithm
        #p = Popen(cmd, stdout=PIPE)
        #  Grab stdout, readline until correct one is found, then decode and slice
        #for i in range(5): p.stdout.readline()
        #out = p.stdout.readline()[4:-1]
        #output.append(int(out))
    
        print(output[0],primeS,A,B)
        
        lhs = abs(output[0] - (5.0/8.0 * float(primeN)))
        rhs = 55 * sqrt(float(primeN))
        
        #print("LHS: ",lhs,"RHS: ",rhs)
        
        if (lhs <= rhs):
            if (False): print("Point count for prime " + primeS + " is " + output[0])
        else:
            losses += 1
            print("Inequality does not hold for prime:" + primeS + " and conductor:" + cond[0] + "(line " + c + ")")
    
    break
    # Reset the position in the file to the beginning
    #conductors.seek(0,0)

primes.close()
conductors.close()
print("The inequality did not hold ",losses," times")
