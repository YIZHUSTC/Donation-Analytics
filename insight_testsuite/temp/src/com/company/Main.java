package com.company;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Main {
    public static void main(String args[]) throws Exception {

        Integer percentile = Integer.parseInt(ReadPercentile.readTxtFile(args[1]).replaceAll("[^0-9]",""));
        File fin = new File(args[0]);   // input file for read
        File fout = new File(args[2]);   //  output file for write

        int bufSize = 1000000;  //  read this much
        FileChannel fcin = new RandomAccessFile(fin, "r").getChannel();
        ByteBuffer rBuffer = ByteBuffer.allocate(bufSize);
        FileChannel fcout = new RandomAccessFile(fout, "rws").getChannel();
        ByteBuffer wBuffer = ByteBuffer.allocateDirect(bufSize);
        ReadFile.readFileByLine(bufSize, fcin, rBuffer, fcout, wBuffer, percentile);

        if(fcin.isOpen()){
            fcin.close();
        }
        if(fcout.isOpen()){
            fcout.close();
        }
    }
}