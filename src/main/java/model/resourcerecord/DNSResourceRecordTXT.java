package model.resourcerecord;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

public class DNSResourceRecordTXT extends DNSResourceRecord {
    private byte txtLength = 0;

    public DNSResourceRecordTXT() {
        super();
    }

    public short getTxtLength() {
        return txtLength;
    }

    public void setTxtLength(byte txtLength) {
        this.txtLength = txtLength;
    }

    @Override
    public void setRData(String rData) {
        super.setRData(new String(new byte[] {txtLength}) + rData);
    }

    public DNSResourceRecordTXT(byte[] bytes) {
        super(bytes);
        int rDataStart = getRrName().length() + 10;
        txtLength = bytes[rDataStart];
        super.setRData(new String(Arrays.copyOfRange(bytes, rDataStart, rDataStart + txtLength + 1)));
    }

    @Override
    public List<Byte> getResourceRecord() {
        List<Byte> resourceRecordAList = super.getResourceRecord();
        resourceRecordAList.addAll(Arrays.asList(ArrayUtils.toObject(getRData().getBytes())));
        return resourceRecordAList;
    }
}
