package com.jclarity.region.model;

import com.jclarity.region.io.RegionStream;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;


public class JavaVirtualMachine implements Iterable<G1GCHeap> {

    ArrayList<G1GCHeap> viewsOverTime = new ArrayList<>();
    private Path path;

    public JavaVirtualMachine(String fileName) {
        this.path = FileSystems.getDefault().getPath(fileName);
    }

    public JavaVirtualMachine( Path path) {
        this.path = path;
    }

    public void load() throws IOException {
        RegionStream stream = new RegionStream(path);
        while ( ! stream.isEof()) {
            G1GCHeap heap = new G1GCHeap();
            stream.readBlock(heap);
            stream.readBlock(new G1GCHeap());
            viewsOverTime.add(heap);
        }
    }

    public int getNumberOfViews() {
        return viewsOverTime.size();
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
