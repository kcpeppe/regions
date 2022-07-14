package com.jclarity.region.model;


import com.jclarity.region.io.RegionStreamDataSink;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class G1GCHeap implements RegionStreamDataSink, Iterable<G1GCRegion>, Comparable<G1GCHeap> {

    private final ArrayList<G1GCRegion> g1GCRegions = new ArrayList<>();

    private final double uptime;

    public G1GCHeap(double uptime) { this.uptime = uptime; }

    @Override
    public void add(G1GCRegion g1GCRegion) {
        g1GCRegions.add(g1GCRegion);
    }

    public int size() {
        return g1GCRegions.size();
    }

    public Stream<G1GCRegion> stream() {
        return g1GCRegions.stream();
    }

    @Override
    public Iterator<G1GCRegion> iterator() {
        return g1GCRegions.iterator();
    }

    @Override
    public void forEach(Consumer<? super G1GCRegion> action) {
        g1GCRegions.forEach(action);
    }

    @Override
    public Spliterator<G1GCRegion> spliterator() {
        return g1GCRegions.spliterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        G1GCHeap g1GCHeap = (G1GCHeap) o;
        return Double.compare(g1GCHeap.uptime, uptime) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uptime);
    }

    @Override
    public int compareTo(G1GCHeap o) {
        return Double.compare(uptime, o.uptime);
    }
}
