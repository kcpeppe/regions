package com.jclarity.region.model;

import com.jclarity.region.io.RegionStream;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;


public class JavaVirtualMachine implements Iterable<G1GCHeap> {

    List<G1GCHeap> viewsOverTime = List.of();

    Map<Long,Integer> regionMapping = new HashMap<>();
    private Path path;

    int regionCount = 0;

    long regionSize = 0L;

    public JavaVirtualMachine(String fileName) {
        this.path = FileSystems.getDefault().getPath(fileName);
    }

    public JavaVirtualMachine( Path path) {
        this.path = path;
    }

    public void load() throws IOException {
        RegionStream regionStream = new RegionStream(path);
        viewsOverTime = regionStream.read().sorted().toList();
        regionCount = 0;
        viewsOverTime.stream().flatMap(G1GCHeap::stream).mapToLong(G1GCRegion::startAddress).distinct().sorted()
                .forEach(address -> regionMapping.putIfAbsent(address, regionCount++));
        regionSize = viewsOverTime.stream().flatMap(G1GCHeap::stream).mapToLong(G1GCRegion::regionSize).max().getAsLong();
    }

    public long regionSize() { return regionSize; }
    public int regionCount() { return regionCount; }

    public int getNumberOfViews() {
        return viewsOverTime.size();
    }

    public int getRegionIndex(G1GCRegion g1GCRegion) {
        return regionMapping.get(g1GCRegion.startAddress());
    }

    public G1GCHeap getG1GCHeapAt(int index) {
        return viewsOverTime.get(index);
    }

    @Override
    public Iterator<G1GCHeap> iterator() {
        return viewsOverTime.iterator();
    }

    @Override
    public void forEach(Consumer<? super G1GCHeap> action) {
        viewsOverTime.forEach(action);
    }

    @Override
    public Spliterator<G1GCHeap> spliterator() {
        return viewsOverTime.spliterator();
    }
}
