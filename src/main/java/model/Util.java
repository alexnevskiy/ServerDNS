package model;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
    public static String convertNameBytesToString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        if ((bytes[0] & 0xFF) == 0xC0) {
            stringBuilder.append(Arrays.toString(Arrays.copyOfRange(bytes, 0, 2)));
            return stringBuilder.toString();
        }
        int currentPosition = 0;
        byte nodeLength;
        while (bytes[currentPosition] != 0) {
            nodeLength = bytes[currentPosition];
            for (int i = currentPosition + 1; i < currentPosition + nodeLength; i++) {
                stringBuilder.append(bytes[i]);
            }
            stringBuilder.append(".");
            currentPosition += nodeLength + 1;
        }
        String nameString = stringBuilder.toString();
        return nameString.substring(0, nameString.length() - 1);
    }

    public static short convertToShort(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getShort();
    }

    public static int convertToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getInt();
    }

    public static List<Byte> convertToBytes(short number) {
        byte[] bytes = ByteBuffer.allocate(Short.BYTES).putShort(number).array();
        return Arrays.asList(ArrayUtils.toObject(bytes));
    }

    public static List<Byte> convertToBytes(int number) {
        byte[] bytes = ByteBuffer.allocate(Integer.BYTES).putInt(number).array();
        return Arrays.asList(ArrayUtils.toObject(bytes));
    }

    public static List<Byte> parseNameToBytes(String name) {
        String[] nodes = name.split("\\.");
        List<Byte> nameBytes = new ArrayList<>();
        for (String node : nodes) {
            nameBytes.add((byte) node.length());
            nameBytes.addAll(Arrays.asList(ArrayUtils.toObject(node.getBytes())));
        }
        nameBytes.add((byte) 0);
        return nameBytes;
    }
}
