package io.github.ppetrbednar.dstr.logic.railway.structures;

public record Rail(String key, String left, String right, int length) implements Comparable<Rail> {
    public String getNext(Switch current) {
        return left.equals(current.getKey()) ? right : left;
    }

    @Override
    public int compareTo(Rail o) {
        return length - o.length();
    }
}
