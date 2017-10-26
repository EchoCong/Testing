/*
 * Copyright (c) 2010 Jason Howk (subaquatic.net)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/*
This program was modified by Shikai Huang for non-profit experiments
Date: 20/10/2017
 */

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

/**
 * Implementation of Base64Decoder data encoding/decoding per <a href="http://tools.ietf.org/html/rfc4648">RFC4648</a>.
 *
 * @author Jason Howk
 */
public abstract class Base64Decoder {

    private static final Logger logger = Logger.getLogger("net.subaquatic.Base64Decoder.class");
    private static final String b64alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static char[] b64AlphabetArray = b64alphabet.toCharArray();
    private static final String inputRegex = "^[A-Za-z0-9+/]*[=]{0,2}";


    /**
     * Encode the string into Base64Decoder format.
     * @param input An array of bytes to encode.
     * @return The encoded String.
     */
    public static String encode(byte[] input) throws BugException {
        int tmpSize = input.length;
        if(tmpSize == 0) {
            throw new BugException("This exception simulates a null input bug");
        }
        //Number of bits must be evenly divisible by 6
        while ((tmpSize * 8) % 6 > 0) {
            tmpSize++;
        }

        byte[] paddedArray = new byte[tmpSize];
        System.arraycopy(input, 0, paddedArray, 0, input.length);

        StringBuilder encString = new StringBuilder();
        for (int x = 0; x < paddedArray.length; x += 3) {
            // Mask off the 8 LSB (0xff).  4 6-bit tuples = 24 bits.  Enough to fit in an int...
            int tuple = ((paddedArray[x] & 0xff) << 16) | ((paddedArray[x + 1] & 0xff) << 8) | (paddedArray[x + 2] & 0xff);
            logger.log(Level.FINEST, "ebit:{0}", Integer.toBinaryString(tuple));

            // Break them down into 6-bit tuples and lookup the value.
            int offset = 0xfc0000;
            for (int y = 3; y >= 0; y--) {
                if ((x + (3 - y) <= input.length) && ((tuple & offset) >>> 6 * y) > 0) {
                    encString.append(b64AlphabetArray[(tuple & offset) >>> 6 * y]);
                    logger.log(Level.FINEST, "emsk:{0}", Integer.toBinaryString((tuple & offset) >>> 6 * y));
                } else {
                    // We're beyond the orignal size and into padding territory.  If it's 0, it's a pad.
                    encString.append("=");
                }
                offset = offset / 0x40;
            }
        }
        return encString.toString();
    }

    /**
     * Decodes a Base64Decoder encoded string.
     * @param input The encoded Base64Decoder String.
     * @return The decoded string as an array of bytes.  Null if error or invalid input was received.
     */
    public static byte[] decode(String input, boolean b0, boolean b1, boolean b2, int line) throws BugException {
        boolean bugNullPoint = b0;
        boolean bugNonAlphabet = b1;
        boolean bugIndexOutBoundary = b2;

        byte[] output = null;
        ArrayList<Integer> oal = new ArrayList<Integer>();

        String tmpString = input.toString();
        try{
            if(tmpString.length() == 0 && bugNullPoint) {
                System.out.println("The exception is in line "+line);
                throw new BugException("This exception simulates a null input bug");
            }
            // Reject any string that contains non-alphabet characters.
            if (!tmpString.matches(inputRegex) && bugNonAlphabet) {
                //return output;
                System.out.println("The exception is in line "+line);
                throw new BugException("This bug simulates the input string that contains non-alphabet character");
            }
        }catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }


        if (tmpString.indexOf("=") != -1) {
            // Replace an = with an A (0) (for byte boundaries)
            tmpString = tmpString.replaceAll("=", "A");
        }
        char[] ca = tmpString.toCharArray();
        //System.out.println(ca.length);

        try{
            for (int z = 0; z < ca.length; z += 4) {
                int dbit = ((b64alphabet.indexOf(ca[z]) & 0x3f) << 18)
                        | ((b64alphabet.indexOf(ca[z + 1]) & 0x3f) << 12)
                        | ((b64alphabet.indexOf(ca[z + 2]) & 0x3f) << 6)
                        | ((b64alphabet.indexOf(ca[z + 3]) & 0x3f));
                logger.log(Level.FINEST, "dbit:{0}", Integer.toBinaryString(dbit));

                int offset = 0xff0000;
                for (int q = 16; q >= 0; q -= 8) {
                    int bt = (dbit & offset) >>> q;
                    // Two's complement stuff here...
                    if (Integer.highestOneBit(bt) == 128) {
                        bt = -((((~bt) << 24) >>> 24) + 1);
                    }
                    // Check for last quantum again...
                    if (oal.size() <= ca.length && bt != 0) {
                        logger.log(Level.FINEST, "bt:{0}:{1}", new Object[]{bt, Integer.toBinaryString(bt)});
                        oal.add(bt);
                    }
                    offset = offset / 0x100;

                }
            }
        }catch(Exception e){
            if(bugIndexOutBoundary){
                System.out.println("The exception is in line "+line);
                throw new BugException("This bug simulates the array index out of the boundary -- The length of the string mod 4 is not 0 ");
            }else{
                //e.printStackTrace(System.err);
            }

        }

        output = new byte[oal.size()];
        for (int x = 0; x < oal.size(); x++) {
            output[x] = oal.get(x).byteValue();
        }
        return output;
    }

    /**
     * Read files by lines
     */
    public static void generateEncodeFile(String fileName) {
        File fileRead = new File(fileName);
        File fileWritten = new File("EncodeResult.txt");

        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(fileRead));
            writer = new BufferedWriter(new FileWriter(fileWritten.getAbsoluteFile()));
            String tempString = null;
            int line = 1;
            // read one line once in a time until the file ends
            while ((tempString = reader.readLine()) != null) {
                // line++;
                try {
                    String resultEncode = new String(Base64Decoder.encode(tempString.getBytes()));
                    writer.write(resultEncode+"\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    public static void generateDecodeFile(String filename, boolean b0, boolean b1, boolean b2) {
        File fileRead = new File(filename);
        File fileWritten = new File("DecodeResult.txt");

        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(fileRead));
            writer = new BufferedWriter(new FileWriter(fileWritten.getAbsoluteFile()));
            String tempString = null;
            int line = 1;
            // read one line once in a time until the file ends
            while ((tempString = reader.readLine()) != null) {
                try {
                    String resultEncode = new String(Base64Decoder.decode(tempString, b0, b1, b2,line));
                    writer.write(resultEncode + "\n");
                    line++;
                } catch (BugException e) {
                    e.printStackTrace();
                    //System.out.println("get bug!!!");
                    System.exit(0);
                }
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


    public static void main(String[] args) throws BugException {

        String filename = "";
        boolean b0 = false;
        boolean b1 = false;
        boolean b2 = false;

        try {
            filename = args[0];
            b0 = Boolean.parseBoolean(args[1]);
            b1 = Boolean.parseBoolean(args[2]);
            b2 = Boolean.parseBoolean(args[3]);
        }catch (Exception e){
            System.err.println("Invalid boolean arguments provided! (stacktrace follows)");
            e.printStackTrace(System.err);
            System.exit(1);
        }

        //generateEncodeFile("original.txt")
        generateDecodeFile(filename, b0, b1, b2);



        //String output = "T`til";
        //System.out.println("testDecodeString... "+output);

        //String resultDecode = new String(Base64Decoder.decode(output, b0, b1, b2,1));
        //System.out.println("RESULT: "+resultDecode);
    }

}
