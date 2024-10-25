package com.aimyskin.laserserialmodule.interceptor;

import com.aimyskin.laserserialmodule.responseClassify.device808Classify.CommandWordEnum;
import com.aimyskin.serialmodule.Response;
import com.aimyskin.serialmodule.interceptor.Interceptor;

import java.nio.ByteBuffer;
import java.util.Arrays;

import okio.ByteString;

public class CRC16Interceptor implements Interceptor {


    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public Response processRequest(Response response) throws Exception {
        byte[] dataBytes = response.request.data.toByteArray();
        byte[] crc16Bytes = new byte[2];
        crcOperate(dataBytes, crc16Bytes);
        ByteBuffer allBytes = ByteBuffer.allocate(dataBytes.length + crc16Bytes.length);
        allBytes.put(dataBytes);
        allBytes.put(crc16Bytes);
        response.request.data = ByteString.of(allBytes.array());
        return response;
    }


    @Override
    public Response processResponse(Response response) throws Exception {
        byte[] allBytes = response.data.toByteArray();
        if (allBytes.length <= 2) {
            throw new Exception("response data length error");
        }
        byte[] dataBytes = new byte[allBytes.length - 2];
        byte[] crc16Bytes = new byte[2];
        ByteBuffer dataBuffer = ByteBuffer.wrap(allBytes);
        dataBuffer.get(dataBytes, 0, dataBytes.length);
        dataBuffer.get(crc16Bytes, 0, crc16Bytes.length);

        byte[] crc16DataBytes = new byte[2];
        crcOperate(dataBytes, crc16DataBytes);
        if (!Arrays.equals(crc16Bytes, crc16DataBytes)) {
            response.data = ByteString.decodeHex(Integer.toHexString(FrameCheck5AA5Interceptor.header) + Integer.toHexString(CommandWordEnum.COMMAND_WORD_85.getCommandWord()));
            return response;
        }
        response.data = ByteString.of(dataBytes);
        return response;
    }


    private void crcOperate(byte[] dataBytes, byte[] crc16Bytes) {
        int crcValue = calculateCRC16(dataBytes);

        // 将CRC16值拆分为两个字节
        byte crcHigh = (byte) ((crcValue >> 8) & 0xFF);
        byte crcLow = (byte) (crcValue & 0xFF);
        crc16Bytes[0] = crcLow;
        crc16Bytes[1] = crcHigh;
    }

    private int calculateCRC16(byte[] data) {
        int crc = 0xFFFF;
        for (byte b : data) {
            crc ^= b & 0xFF;
            for (int i = 0; i < 8; i++) {
                if ((crc & 1) != 0) {
                    crc = (crc >> 1) ^ 0xA001;
                } else {
                    crc = crc >> 1;
                }
            }
        }
        return crc & 0xFFFF;
    }
}
