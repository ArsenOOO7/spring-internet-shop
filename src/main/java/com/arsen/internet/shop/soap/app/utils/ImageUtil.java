package com.arsen.internet.shop.soap.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Class to pack/unpack image
 * @author Arsen Sydoryk
 */
public class ImageUtil {

    /**
     * Compressing image
     * @param data of image
     * @return of packed image
     */
    public static byte[] compress(byte[] data){

        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();;

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {

            byte[] temp = new byte[4 * 1024];
            int size;
            while (!deflater.finished()) {
                size = deflater.deflate(temp);
                outputStream.write(temp, 0, size);
            }

            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Decompress the image
     * @param data of compressed image
     * @return unpacked image
     */
    public static byte[] decompress(byte[] data){

        Inflater inflater = new Inflater();
        inflater.setInput(data);

        try(ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(data.length)) {
            byte[] temp = new byte[4 * 1024];
            int count;
            while(!inflater.finished()){
                count = inflater.inflate(temp);
                arrayOutputStream.write(temp, 0, count);
            }

            return arrayOutputStream.toByteArray();
        } catch (IOException | DataFormatException e) {
            throw new RuntimeException(e);
        }
    }

}
