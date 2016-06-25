package com.jclarity.region.io;

import com.jclarity.region.model.G1GCRegion;

public interface RegionStreamDataSink {

    void add(G1GCRegion g1GCRegion);
}
