package com.company;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

public class ReadFile {

    private static Map<String, Integer> donors = new HashMap();   //  <name+zipcode, year>
    private static Set<String> repeatDonors = new HashSet();   //  <name+zipcode>
    private static Map<String, Recipient> contribution  = new HashMap();  // <year+recipient+zipcode, Recipient>

    public static void readFileByLine(int bufSize, FileChannel fcin, ByteBuffer rBuffer, FileChannel fcout, ByteBuffer wBuffer, Integer percentile) {
        List<String> dataList = new ArrayList<String>();    //  store each line
        byte[] lineByte = new byte[0];

        String encode = "UTF-8";
        try {
            byte[] temp = new byte[0];
            while (fcin.read(rBuffer) != -1) {  // fcin.read(rBuffer) read fron channel to rBuffer
                int rSize = rBuffer.position(); // position after read, length of read
                byte[] bs = new byte[rSize];    //  context of read
                rBuffer.rewind();   //  set position to 0, enables re-read Buffer, otherwise cannot get()
                rBuffer.get(bs);    //  rBuffer.get(bs,0,bs.length()) starting from position and read bs.length of byte and write to from bs[0] to [bs.length-1]
                rBuffer.clear();

                int startNum = 0;
                int LF = 10;    //line feed
                int CR = 13;    //return
                boolean hasLF = false;  // whether there is a line feed or an enter
                for(int i = 0; i < rSize; i++){
                    if(bs[i] == LF){
                        hasLF = true;
                        int tempNum = temp.length;
                        int lineNum = i - startNum;
                        lineByte = new byte[tempNum + lineNum]; //  array size does not contain line feed or enter

                        System.arraycopy(temp, 0, lineByte, 0, tempNum);    //  fill lineByte[0]~lineByte[tempNum-1]
                        temp = new byte[0];
                        System.arraycopy(bs, startNum, lineByte, tempNum, lineNum); //  fill lineByte[tempNum]~lineByte[tempNum+lineNum-1]

                        String line = new String(lineByte, 0, lineByte.length, encode); //  a whole line without line feed or enter
                        line = Filter.filter(line, donors, repeatDonors, contribution, percentile);
                        dataList.add(line);
                        if(line != "") {
                            WriteFile.writeFileByLine(fcout, wBuffer, line + "\r\n");
                        }

                        //  filter enter
                        if(i + 1 < rSize && bs[i + 1] == CR){
                            startNum = i + 2;
                        }else{
                            startNum = i + 1;
                        }
                    }
                }

                if(hasLF){
                    temp = new byte[bs.length - startNum];
                    System.arraycopy(bs, startNum, temp, 0, temp.length);
                }else{  // not a whole line
                    byte[] toTemp = new byte[temp.length + bs.length];
                    System.arraycopy(temp, 0, toTemp, 0, temp.length);
                    System.arraycopy(bs, 0, toTemp, temp.length, bs.length);
                    temp = toTemp;
                }
            }
            if(temp != null && temp.length > 0){    //  the last line does not contain an enter
                String line = new String(temp, 0, temp.length, encode);
                line = Filter.filter(line, donors, repeatDonors, contribution, percentile);
                dataList.add(line);
                if(line != "") {
                    WriteFile.writeFileByLine(fcout, wBuffer, line + "\r\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}