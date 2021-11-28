package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static model.Util.*;

public class DNSQuestion {
    private String qName = "";
    private short qType = 0;
    private short qClass = 0;

    public DNSQuestion() {}

    public DNSQuestion(byte[] bytes) {
        qName = convertNameBytesToString(Arrays.copyOfRange(bytes, 0, bytes.length - 4));
        qType = convertToShort(Arrays.copyOfRange(bytes, bytes.length - 4, bytes.length - 2));
        qClass = convertToShort(Arrays.copyOfRange(bytes, bytes.length - 2, bytes.length));
    }

    public String getQName() {
        return qName;
    }

    public void setQName(String qName) {
        this.qName = qName;
    }

    public short getQType() {
        return qType;
    }

    public void setQType(short qType) {
        this.qType = qType;
    }

    public short getQClass() {
        return qClass;
    }

    public void setQClass(short qClass) {
        this.qClass = qClass;
    }

    public List<Byte> getQuestion() {
        List<Byte> questionList = new ArrayList<>();
        questionList.addAll(parseNameToBytes(qName));
        questionList.addAll(convertToBytes(qType));
        questionList.addAll(convertToBytes(qClass));
        return questionList;
    }
}
