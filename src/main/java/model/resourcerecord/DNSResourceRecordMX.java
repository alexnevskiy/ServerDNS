package model.resourcerecord;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

import static model.Util.*;

public class DNSResourceRecordMX extends DNSResourceRecord {
    private short preference = 0;

    public DNSResourceRecordMX() {
        super();
    }

    public short getPreference() {
        return preference;
    }

    public void setPreference(short preference) {
        this.preference = preference;
    }

    public DNSResourceRecordMX(byte[] bytes) {
        super(bytes);
        int rDataStart = getRrName().length() + 10;
        preference = convertToShort(Arrays.copyOfRange(bytes, rDataStart, rDataStart + 2));
        setRData(new String(Arrays.copyOfRange(bytes, rDataStart + 2, rDataStart + getRdLength() - 2)));
    }

    @Override
    public List<Byte> getResourceRecord() {
        List<Byte> resourceRecordAList = super.getResourceRecord();
        resourceRecordAList.addAll(Arrays.asList(ArrayUtils.toObject(getRData().getBytes())));
        return resourceRecordAList;
    }
}
