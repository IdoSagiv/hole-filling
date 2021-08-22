## 1)

### Q:

if there are m boundary pixels and n pixels inside the hole, what’s the complexity of the algorithm that fills the hole,
assuming that the hole and boundary were already found? Try to also express the complexity only in terms of n.

### A:

First, we notice that m=O(n) because in the worst case m=8n (8 connectivity and every hole pixel is not connected to the
other).</br>
Now, assuming that the hole and boundary were already found, the time complexity of setting the new value to one hole
pixel is O(m)=O(n), and therefore the total time complexity is **O(mn)=O(n^2)**.

## 2)

### Q:

Describe an algorithm that approximates the result in O(n) to a high degree of accuracy. As a bonus, implement the
suggested algorithm in your library in addition to the algorithm described above.

### A:

#### Algorithm

- find all boundary pixels
- split the boundary pixels into k (constant!) sets:
    - each set contains a continuous section of boundary pixels
    - each set is represented by the mean pixel value and center pixel coordinates
- apply the previous algorithm on the 'new' boundary pixels (each set is a new pixel)

** the larger k is, the more accurate the result is

#### Complexity

- split the boundary pixels into sets - O(n)
- calculate one hole pixel new value - O(k)=O(1)
- total - O(m+kn)=O(n)

#### Implementation

one possible way to split the boundary pixels to k sets is to iterate them in ascending order according to their
coordinates, and insert m/k pixels to each set.</br>
Alternatively, we can think about a smarter splitting in which the sets aren't necessarily disjoint or sized the same
and each set contains pixels with similar values (in order to keep the runtime, this splitting needs to be computed in
O(n) time complexity).

** implementation to the first option can be found in the HoleFiller class