package com.company;

import java.io.*;

public class ReadPercentile {
    public static String readTxtFile(String filePath) {
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = bufferedReader.readLine();
                bufferedReader.close();
                read.close();
                return lineTxt;
            } else {
                System.out.println("File not found");
            }
        }
        catch (Exception e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return "";
    }
}


