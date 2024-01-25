Exploring data storage efficiency, my implementation of the Huffman Coding algorithm delves into the realm of compression. Focused on text file compression, the project navigates the ASCII encoding intricacies,
where each character maps to a unique sequence of eight bits. The mission is to compress text data intelligently, using fewer bits to represent the same information.

Overview of Files-

HuffmanCoding class, which contains the methods I implemented: makeSortedList(), makeTree(), makeEncodings(), encode() and decode().

CharFreq class, which houses a Character object “character” representing a certain ASCII character, and a double “probOcc” representing its probability of occurrence (value between 0 and 1 showing its frequency). These objects are implemented to compare primarily by probOcc, then by character if those are equal. Note that “character” can be null. Getters and setters are provided.

Queue class, which functions as a simple generic queue. It implements isEmpty(), size(), enqueue(), dequeue(), and peek().

TreeNode class, which houses a CharFreq object “data” representing a certain character and its frequency, and TreeNodes “left” and “right” representing the left and right subtrees of the binary tree. Getters and setters are provided.

Driver class, tests methods interactively.

StdIn and StdOut, which are used by the driver, provided methods, and some implemented methods as well.

Multiple text files which contain input data, and can be read by the driver as test cases. These files, as well as the files used for grading are guaranteed to be ASCII only.



Algorithm to build the Huffman Tree-

Start two empty queues: Source and Target

Create a node for each character present in the input file, each node contains the character and its occurrence probability. 

Enqueue the nodes in the Source queue in increasing order of occurrence probability.

Repeat until the Source queue is empty and the Target queue has only one node.

Dequeue from either queue or both the two nodes with the smallest occurrence probability. If the front node of Source and Target have the same occurrence probability, dequeue from Source first.

Create a new node whose character is null and occurrence probability is the sum of the occurrence probabilities of the two dequeued nodes. Add the two dequeued nodes as children: the first dequeued node as the left child and the second dequeued node as the right child.

Enqueue the new node into Target.
