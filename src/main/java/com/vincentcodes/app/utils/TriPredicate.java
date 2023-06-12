package com.vincentcodes.app.utils;

@FunctionalInterface
public interface TriPredicate{
    boolean test(int key, boolean ctrl, boolean shift);
}
