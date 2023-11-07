package io.collective.basic;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    List<Block> blocks = new ArrayList<Block>();

    public boolean isEmpty() {
        return blocks.isEmpty();
    }

    public void add(Block block) {
        blocks.add(block);
    }

    public Block get(int index) {
        return blocks.get(index);
    }

    public int size() {
        return blocks.size();
    }

    public boolean isValid() {
        if (isEmpty()) return true;

        Block genesis = get(0);
        if (!isValid(genesis)) return false;

        for (int i = 1; i < size(); i++) {
            Block prev = get(i - 1);
            Block curr = get(i);
            if (!isValid(curr)) return false;
            if (!curr.getPreviousHash().equals(prev.getHash())) return false;
        }

        return true;
    }

    static boolean isValid(Block block) {
        return isMined(block) && block.isValid();
    }

    /// Supporting functions that you'll need.

    public static Block mine(Block block) throws NoSuchAlgorithmException {
        Block mined = new Block(block.getPreviousHash(), block.getTimestamp(), block.getNonce());

        while (!isMined(mined)) {
            mined = new Block(mined.getPreviousHash(), mined.getTimestamp(), mined.getNonce() + 1);
        }
        return mined;
    }

    public static boolean isMined(Block minedBlock) {
        return minedBlock.getHash().startsWith("00");
    }
}