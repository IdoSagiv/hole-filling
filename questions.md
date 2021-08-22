*** Q1 ***
Q:
    if there are m boundary pixels and n pixels inside the hole, what’s the complexity of the algorithm that fills the hole,
    assuming that the hole and boundary were already found? Try to also express the complexity only in terms of n.
A:
    First of all, we notice that m=O(n) because in the worst case  m=8*n (8 connectivity and every hole pixel is not connected to the other).
    Now, assuming that the hole and boundary were already found, the time complexity of setting the new value of each hole pixel is O(m)=O(n),
    and therefore the total time complexity is O(m*n)=O(n^2)
    

