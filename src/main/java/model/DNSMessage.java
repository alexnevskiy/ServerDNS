package model;

import model.resourcerecord.DNSResourceRecord;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DNSMessage {
    private DNSHeader header = new DNSHeader();
    private List<DNSQuestion> questions = new ArrayList<>();
    private List<DNSResourceRecord> resourceRecords = new ArrayList<>();

    public DNSMessage() {}

    public DNSMessage(byte[] bytes) {
        header = new DNSHeader(Arrays.copyOfRange(bytes, 0, 12));
        DNSQuestion question = new DNSQuestion(Arrays.copyOfRange(bytes, 12, bytes.length));
        questions = Collections.singletonList(question);    // In practice, only 1 question per message is used
        resourceRecords = new ArrayList<>();
    }

    public DNSHeader getHeader() {
        return header;
    }

    public void setHeader(DNSHeader header) {
        this.header = header;
    }

    public List<DNSQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<DNSQuestion> questions) {
        this.questions = questions;
    }

    public List<DNSResourceRecord> getResourceRecords() {
        return resourceRecords;
    }

    public void setResourceRecords(List<DNSResourceRecord> resourceRecords) {
        this.resourceRecords = resourceRecords;
    }

    public byte[] getMessage() {
        List<Byte> messageList = new ArrayList<>(header.getHeader());
        for (DNSQuestion question : questions) {
            messageList.addAll(question.getQuestion());
        }
        for (DNSResourceRecord resourceRecord : resourceRecords) {
            messageList.addAll(resourceRecord.getResourceRecord());
        }
        Byte[] bytes = messageList.toArray(new Byte[0]);
        return ArrayUtils.toPrimitive(bytes);
    }
}
