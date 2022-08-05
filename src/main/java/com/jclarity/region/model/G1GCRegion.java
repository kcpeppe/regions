package com.jclarity.region.model;

//basically a data holder

//[11.617s][trace][gc,region] GC(7) G1HR CSET(EDEN) [0x000000070e600000, 0x000000070e800000, 0x000000070e800000]
// hex fields are: top, bottom end
// The capacity of the region is bottom - end
// Bytes used is bottom - top
// Bytes free is top - end
public record G1GCRegion(RegionType assignment, long topAddress, long startAddress, long endAddress) {

    public long bytesUsed() { return endAddress - topAddress; }
    public long bytesFree() { return topAddress - endAddress; }
    public long regionSize() { return endAddress - startAddress; }

}

