The compression works for all files. Writes a binary file. There is no set byte size and my dictionary has no max size. You're welcome for working code :)
Optimization Part 3 
Encoder works and outputs a file. It is already very efficient, so no optimizations can be made further. 
For lzw-file3.txt, the run time is around 650-700 milliseconds. 
The decoder does not function. However, I optimized it so that it can run and compile, greatly improving the project. Although a decoded file is created, there are no contents in the file, meaning there is a flaw in the decompress method. 