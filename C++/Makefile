CXX = g++
NTL_INCLUDE = -IF:/WinNTL-6_2_1/include
NTL_LIB = -LF:/WinNTL-6_2_1/lib -lNTL
CXXFLAGS = -Wall -std=c++11 -O2 $(NTL_INCLUDE)
LDFLAGS = $(NTL_LIB)
EXE = icart.exe

all: $(EXE)

$(EXE): main.o epoint.o icart.o
	$(CXX) main.o epoint.o icart.o $(LDFLAGS) -o $@

main.o: main.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@
	
ecurve.o: ecurve.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

epoint.o: epoint.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

icart.o: icart.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

clean:
	rm *.o && rm $(EXE)