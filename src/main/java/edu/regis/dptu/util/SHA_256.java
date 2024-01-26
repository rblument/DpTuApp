/*
 * DPTu: Dynamic Programming Tutor
 * 
 *  (C) Johanna & Richard Blumenthal, All rights reserved
 *  The SHA-256 algorithm is originall from the public domain, changes are ricks.
 * 
 *  Unauthorized use, duplication or distribution without the authors'
 *  permission is strictly prohibited.
 * 
 *  Unless required by applicable law or agreed to in writing, this
 *  software is distributed on an "AS IS" basis without warranties
 *  or conditions of any kind, either expressed or implied.
 */
package edu.regis.dptu.util;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;

/**
 * An implementation of the SHA-256 algorithm.
 * 
 * See sha256(String).
 * For example sha256("Regis Computer Science Rocks!") returns
 *   fddfe0c1671993dbe8da88ccfbdf8aae3ae255d41b2808ff86041cca4cff65e5
 * 
 *  This code was originally in the public domain and has been modified since.
 * 
 * @author rickb
 * @author unknown
 */
public class SHA_256 {
     /**
     * The singleton instance of this frame.
     */
    private final static SHA_256 SINGLETON;
    
    // Invoked when this class is loaded
    static {     
        SINGLETON = new SHA_256();
    }
    
    /**
     * Return the singleton instance of this algorithm.
     * 
     * @return the SHA_256 singleton
     */
    public static SHA_256 instance() {
        return SINGLETON;
    }
    
    /**
     * The constants (in hex) defined in the SHA-256 specification.
     */
    private static final int[] K = {
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
            0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    /**
     * The initial working variable constants defined in the SHA-256 specification.
     */
    private static final int[] H0 = {
        0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a, 0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
    };

    private static final int BLOCK_BITS = 512;
    
    
    private static final int BLOCK_BYTES = BLOCK_BITS / 8;

    private final int[] w = new int[64];
    private final int[] h = new int[8];
    private final int[] temp = new int[8];
    
    /**
     * Initialize this algorithm with an empty set of SHA-256 listeners.
     */
    private SHA_256() {
    }
    
    /**
     * Create a SHA-256 digest of the given message.
     * 
     * @param msg
     */
    public String sha256(String msg) {
        Charset charset = Charset.forName("ASCII");

        byte[] asciiEncodeMsg = msg.getBytes(charset);

        byte[] digest = hash(asciiEncodeMsg);

        String digestStr = bytesToHex(digest);
        
        return digestStr;
    }

    private static String hexToBin(String hexString) {
        StringBuffer buffer = new StringBuffer();
        for (int pos = 0; pos < hexString.length(); pos++) {
            switch (hexString.charAt(pos)) {
                case '0':
                    buffer.append("0000");
                    break;

                case '1':
                    buffer.append("0001");
                    break;

                case '2':
                    buffer.append("0010");
                    break;

                case '3':
                    buffer.append("0011");
                    break;

                case '4':
                    buffer.append("0100");
                    break;

                case '5':
                    buffer.append("0101");
                    break;

                case '6':
                    buffer.append("0110");
                    break;

                case '7':
                    buffer.append("0111");
                    break;

                case '8':
                    buffer.append("1000");
                    break;

                case '9':
                    buffer.append("1001");
                    break;

                case 'A':
                case 'a':
                    buffer.append("1010");
                    break;

                case 'B':
                case 'b':
                    buffer.append("1011");
                    break;

                case 'C':
                case 'c':
                    buffer.append("1100");
                    break;

                case 'D':
                case 'd':
                    buffer.append("1101");
                    break;

                case 'E':
                case 'e':
                    buffer.append("1110");
                    break;

                default: // 'F'
                    buffer.append("1111");
                    break;

            }
        }

        return buffer.toString();
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    

    // NEXT class
    /**
     * Hashes the given message with SHA-256 and returns the hash.
     *
     * @param message The bytes to hash.
     * @return The hash's bytes.
     */
    public byte[] hash(byte[] message) {
        // let H = H0
        System.arraycopy(H0, 0, h, 0, H0.length);

        // initialize all words
        int[] words = pad(message);
        
        

        // enumerate all blocks (each containing 16 words)
        for (int i = 0, n = words.length / 16; i < n; ++i) {

            // initialize w from the block's words
            System.arraycopy(words, i * 16, w, 0, 16);
            
            
            
            // Modify the zero-ed indexes at the end of the array using the following algorithm:
            //For i from w[16…63]:
            //s0 = (w[i-15] rightrotate 7) xor (w[i-15] rightrotate 18) xor (w[i-15] rightshift 3)
            //s1 = (w[i- 2] rightrotate 17) xor (w[i- 2] rightrotate 19) xor (w[i- 2] rightshift 10)
            //w[i] = w[i-16] + s0 + w[i-7] + s1
            for (int t = 16; t < w.length; ++t) {
                w[t] = smallSig1(w[t - 2]) + w[t - 7] + smallSig0(w[t - 15]) + w[t - 16];
            }


            // let TEMP = H
            System.arraycopy(h, 0, temp, 0, h.length);

            // operate on TEMP
            for (int t = 0; t < w.length; ++t) {
                //     =  H                 E              E         F        G
                int t1 = temp[7] + bigSig1(temp[4]) + ch(temp[4], temp[5], temp[6]) + K[t] + w[t];
                        
                //                  A             A         B       C
                int t2 = bigSig0(temp[0]) + maj(temp[0], temp[1], temp[2]);
                
                
                System.arraycopy(temp, 0, temp, 1, temp.length - 1);
                           
                // E
                temp[4] += t1;
                temp[0] = t1 + t2;
            }

            // add values in TEMP to values in H
            for (int t = 0; t < h.length; ++t) {
                h[t] += temp[t];
            }
        }

        return toByteArray(h);
    }

    /**
     * <b>Internal method, no need to call.</b> Pads the given message to have a length
     * that is a multiple of 512 bits (64 bytes), including the addition of a
     * 1-bit, k 0-bits, and the message length as a 64-bit integer.
     * The result is a 32-bit integer array with big-endian byte representation.
     *
     * @param message The message to pad.
     * @return A new array with the padded message bytes.
     */
    public  int[] pad(byte[] message) {
        // new message length: original + 1-bit and padding + 8-byte length
        // --> block count: whole blocks + (padding + length rounded up)
        int finalBlockLength = message.length % BLOCK_BYTES;
        int blockCount = message.length / BLOCK_BYTES + (finalBlockLength + 1 + 8 > BLOCK_BYTES ? 2 : 1);

        final IntBuffer result = IntBuffer.allocate(blockCount * (BLOCK_BYTES / Integer.BYTES));

        // copy as much of the message as possible
        ByteBuffer buf = ByteBuffer.wrap(message);
        for (int i = 0, n = message.length / Integer.BYTES; i < n; ++i) {
            result.put(buf.getInt());
        }
        // copy the remaining bytes (less than 4) and append 1 bit (rest is zero)
        ByteBuffer remainder = ByteBuffer.allocate(4);
        remainder.put(buf).put((byte) 0b10000000).rewind();
        result.put(remainder.getInt());

        // ignore however many pad bytes (implicitly calculated in the beginning)
        result.position(result.capacity() - 2);
        // place original message length as 64-bit integer at the end
        long msgLength = message.length * 8L;
        result.put((int) (msgLength >>> 32));
        result.put((int) msgLength);

        return result.array();
    }

    /**
     * Converts the given int array into a byte array via big-endian conversion
     * (1 int becomes 4 bytes).
     *
     * @param ints The source array.
     * @return The converted array.
     */
    private  byte[] toByteArray(int[] ints) {
        ByteBuffer buf = ByteBuffer.allocate(ints.length * Integer.BYTES);
        for (int i : ints) {
            buf.putInt(i);
        }
        return buf.array();
    }

    private  int ch(int x, int y, int z) {
        return (x & y) | ((~x) & z);
    }

    private  int maj(int x, int y, int z) {
        return (x & y) | (x & z) | (y & z);
    }

    private  int bigSig0(int x) {
        return Integer.rotateRight(x, 2)
                ^ Integer.rotateRight(x, 13)
                ^ Integer.rotateRight(x, 22);
    }

    private  int bigSig1(int x) {
        return Integer.rotateRight(x, 6)
                ^ Integer.rotateRight(x, 11)
                ^ Integer.rotateRight(x, 25);
    }

    private  int smallSig0(int x) {
        return Integer.rotateRight(x, 7)
                ^ Integer.rotateRight(x, 18)
                ^ (x >>> 3);
    }

    private  int smallSig1(int x) {
        return Integer.rotateRight(x, 17)
                ^ Integer.rotateRight(x, 19)
                ^ (x >>> 10);
    }
    
    // Rickb
    public  String padLeftZeros(String inputString, int length) {
    if (inputString.length() >= length) {
        return inputString;
    }
    StringBuilder sb = new StringBuilder();
    while (sb.length() < length - inputString.length()) {
        sb.append('0');
    }
    sb.append(inputString);

    return sb.toString();
    }
}

