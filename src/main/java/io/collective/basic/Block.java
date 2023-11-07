package io.collective.basic;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {
    private String previousHash;
    private long timestamp;
    private int nonce;
    private String hash;

    public Block(String previousHash, long timestamp, int nonce) throws NoSuchAlgorithmException {
        this.previousHash = previousHash;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.hash = calculatedHash();
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getNonce() {
        return nonce;
    }

    public String getHash() {
        return hash;
    }

    public String calculatedHash() throws NoSuchAlgorithmException {
        return calculateHash(getHashInput());
    }

    private String getHashInput() {
        byte[] previousHashBytes = reverseByteArray(getPreviousHash().getBytes(StandardCharsets.UTF_8));
        byte[] timestampBytes = ByteBuffer.allocate(Long.BYTES).putLong(getTimestamp()).array();
        byte[] nonceBytes = ByteBuffer.allocate(Long.BYTES).putInt(getNonce()).array();
        return (
            byteArrayToLittleEndianHex(previousHashBytes) +
            byteArrayToLittleEndianHex(timestampBytes) +
            byteArrayToLittleEndianHex(nonceBytes)
        );
    }

    static byte[] reverseByteArray(byte[] bytes) {
        byte[] reversedByteArray = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            reversedByteArray[i] = bytes[bytes.length - 1 - i];
        }
        return reversedByteArray;
    }

    static String byteArrayToLittleEndianHex(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        StringBuilder hexString = new StringBuilder();
        while (buffer.hasRemaining()) {
            hexString.append(String.format("%02X", buffer.get()));
        }
        return hexString.toString();
    }

    /// Supporting functions that you'll need.

    static String calculateHash(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(string.getBytes());
        return String.format("%064x", new BigInteger(1, digest.digest()));
    }
}