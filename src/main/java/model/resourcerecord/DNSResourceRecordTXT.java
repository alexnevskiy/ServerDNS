package model.resourcerecord;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

public class DNSResourceRecordTXT extends DNSResourceRecord {
    private short txtLength = 0;

    public DNSResourceRecordTXT() {
        super();
    }

    public short getTxtLength() {
        return txtLength;
    }

    public void setTxtLength(short txtLength) {
        this.txtLength = txtLength;
    }

    public DNSResourceRecordTXT(byte[] bytes) {
        super(bytes);
        int rDataStart = getRrName().length() + 10;
        txtLength = bytes[rDataStart];
        setRData(new String(Arrays.copyOfRange(bytes, rDataStart + 1, rDataStart + txtLength)));
    }

    @Override
    public List<Byte> getResourceRecord() {
        List<Byte> resourceRecordAList = super.getResourceRecord();
        resourceRecordAList.addAll(Arrays.asList(ArrayUtils.toObject(getRData().getBytes())));
        return resourceRecordAList;
    }
}
