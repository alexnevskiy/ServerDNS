package model.resourcerecord;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

public class DNSResourceRecordAAAA extends DNSResourceRecord {
    public DNSResourceRecordAAAA() {
        super();
    }

    public DNSResourceRecordAAAA(byte[] bytes) {
        super(bytes);
        if (getRdLength() != 16) {
            throw new IllegalArgumentException("Does not match the specified type.");
        }
        int rDataStart = getRrName().length() + 10;
        setRData(new String(Arrays.copyOfRange(bytes, rDataStart, rDataStart + getRdLength())));
    }

    @Override
    public List<Byte> getResourceRecord() {
        List<Byte> resourceRecordAList = super.getResourceRecord();
        resourceRecordAList.addAll(Arrays.asList(ArrayUtils.toObject(getRData().getBytes())));
        return resourceRecordAList;
    }
}
