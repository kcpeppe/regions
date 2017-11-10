package com.jclarity.region.model;

//basically a data holder

public class G1GCRegion {

    private RegionType assignment;
    private long regionSize;
    private long startAddress;
    private long endAddress;
    private int bytesUsed;
    private int previousLive;
    private int nextLive;
    private double gcEfficiency;
    private int rememberedSetSize;
    private int codeRootsSize;

    //###   type address-range used prev-live next-live gc-eff remset code-roots
    public G1GCRegion(RegionType assignment, long startAddress, long endAddress, int bytesUsed, int previousLive, int nextLive, double gcEffeciency, int rememberedSetSize, int codeRootsSize) {
        this.regionSize = endAddress - startAddress;
        this.assignment = assignment;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.bytesUsed = bytesUsed;
        this.previousLive = previousLive;
        this.nextLive = nextLive;
        this.gcEfficiency = gcEffeciency;
        this.rememberedSetSize = rememberedSetSize;
        this.codeRootsSize = codeRootsSize;
    }

    public RegionType getAssignment() { return assignment; }
    public long getRegionSize() { return this.regionSize; }
    public long getStartAddress() { return startAddress; }
    public long getEndAddress() { return endAddress; }
    public int getBytesUsed() { return bytesUsed; }
    public int getPreviousLive() { return previousLive; }
    public int getNextLive() { return nextLive; }
    public double getGcEfficiency() { return gcEfficiency; }
    public int getRememberedSetSize() { return rememberedSetSize; }
    public int getCodeRootsSize() { return codeRootsSize; }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getAssignment().getLabel()).append("     ");
        builder.append(getStartAddress()).append("-");
        builder.append(getEndAddress()).append("     ");
        builder.append(getBytesUsed()).append("     ");
        builder.append(getPreviousLive()).append("     ");
        builder.append(getNextLive()).append("     ");
        builder.append(getGcEfficiency()).append("     ");
        builder.append(getRememberedSetSize()).append("     ");
        builder.append(getCodeRootsSize()).append("     ");
        return builder.toString();
    }

    public boolean equals(Object o) {
        if ( o instanceof G1GCRegion) {
            return ((G1GCRegion)o).getStartAddress() == this.getStartAddress();
        }
        return false;
    }

    public int hashCode() {
        return (int)getStartAddress();
    }

}

