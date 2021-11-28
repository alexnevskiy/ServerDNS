package model.resourcerecord;

import java.util.ArrayList;
import java.util.List;

import static model.Util.convertToBytes;
import static model.Util.parseNameToBytes;

public class DNSResourceRecord {
    private String rrName = "";
    private short rrType = 0;
    private short rrClass = 0;
    private int rrTtl = 0;
    private short rdLength = 0;
    private String rData = "";

    public DNSResourceRecord() {}

    public String getRrName() {
        return rrName;
    }

    public void setRrName(String rrName) {
        this.rrName = rrName;
    }

    public short getRrType() {
        return rrType;
    }

    public void setRrType(short rrType) {
        this.rrType = rrType;
    }

    public short getRrClass() {
        return rrClass;
    }

    public void setRrClass(short rrClass) {
        this.rrClass = rrClass;
    }

    public int getRrTtl() {
        return rrTtl;
    }

    public void setRrTtl(int rrTtl) {
        this.rrTtl = rrTtl;
    }

    public short getRdLength() {
        return rdLength;
    }

    public void setRdLength(short rdLength) {
        this.rdLength = rdLength;
    }

    public String getRData() {
        return rData;
    }

    public void setRData(String rData) {
        this.rData = rData;
    }

    public List<Byte> getResourceRecord() {
        List<Byte> resourceRecordList = new ArrayList<>();
        resourceRecordList.addAll(parseNameToBytes(rrName));
        resourceRecordList.addAll(convertToBytes(rrType));
        resourceRecordList.addAll(convertToBytes(rrClass));
        resourceRecordList.addAll(convertToBytes(rrTtl));
        resourceRecordList.addAll(convertToBytes(rdLength));
        return resourceRecordList;
    }
}
