package com.jclarity.region.io;


import com.jclarity.region.model.G1GCHeap;
import com.jclarity.region.model.G1GCRegion;
import com.jclarity.region.model.RegionType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class RegionStream {

    private static final String HEX_ADDRESS = "0x([0-9,a-f]{16})";
    private static final String DECIMAL_POINT = "(?:\\.|,)";
    private static final String INTEGER = "\\d+";
    private static final String DATE = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[\\+|\\-]\\d{4}";

    private static final String DATE_STAMP = "\\[" + DATE + "\\]";
    private static final String UPTIME = "\\[" + INTEGER + DECIMAL_POINT + "\\d{3}s\\]";
    private static final String TIME_MILLIS = "\\[\\d+ms\\]";
    private static final String TIME_NANOS = "\\[\\d+ns\\]";
    private static final String PID_TID = "\\[\\d+\\]";
    private static final String UNIFIED_LOG_LEVEL_BLOCK = "\\[(?:error|warning|info|debug|trace|develop) *\\]";
    private static final Pattern DECORATORS = Pattern.compile("(" + DATE_STAMP + ")?(" + UPTIME + ")?(" + TIME_MILLIS + ")?(" + TIME_MILLIS + ")?(" + TIME_NANOS + ")?(" + TIME_NANOS + ")?(" + PID_TID + ")?(" + PID_TID + ")?(" + UNIFIED_LOG_LEVEL_BLOCK + ")?");
    //Using zero-width negative lookbehind to miss capturing records formatted like [0x1f03].
    //[0.081s][trace][safepoint] Thread: 0x00007fd0d2006800  [0x1f03] State: _at_safepoint _has_called_back 0 _at_poll_safepoint 0
    private static final Pattern TAGS = Pattern.compile(".*(?<=^|\\])\\[([a-z0-9,. ]+)\\]");

    // [35.836s][trace][gc,region] G1HR ALLOC(EDEN) [0x000000072ce00000, 0x000000072ce00000, 0x000000072d000000]
    // Hex values are: top, bottom, end
    private static final Pattern REGION_LOG_ENTRY = Pattern.compile("G1HR\\s+(ALLOC|CLEANUP|COMMIT|CSET|INACTIVE|RETIRE|REUSE|UNCOMMIT)\\((FREE|EDEN|SURV|OLD|HUMS|HUMC)\\)\\s+\\[" + HEX_ADDRESS + ",\\s+" + HEX_ADDRESS + ",\\s+" + HEX_ADDRESS + "\\]");

    private int OPERATION = 1;
    private int REGION_TYPE = 2;
    private int BOTTOM = 3;
    private int TOP = 4;
    private int END = 5;

    private BufferedReader reader;
    private boolean eof = false;

    public RegionStream(Path file) throws IOException {
        reader = Files.newBufferedReader(file);
    }

    public boolean isEof() { return eof; }

    Map<Double, G1GCHeap> heapOverTime = new ConcurrentHashMap<>();

    public Stream<G1GCHeap> read() throws IOException {
        String line;
        while ( (line = reader.readLine()) != null) {

            Matcher decorators = DECORATORS.matcher(line);
            Matcher matcher = REGION_LOG_ENTRY.matcher(line);
            if (matcher.find()) {
                G1GCHeap g1GCHeap = null;
                if (decorators.find()) {
                    double uptime = getUpTime(decorators);
                    if (uptime > 0) {
                        g1GCHeap = heapOverTime.computeIfAbsent(uptime, G1GCHeap::new);
                    }
                }
                Objects.requireNonNull(g1GCHeap, "couldn't get uptime from " + line);
                String operation = matcher.group(OPERATION);
                RegionType type = RegionType.valueOf(matcher.group(REGION_TYPE));
                long bottom = Long.parseLong(matcher.group(BOTTOM),16);
                long top = Long.parseLong(matcher.group(TOP),16);
                long end = Long.parseLong(matcher.group(END),16);
                if (end - bottom <= 0) {
                    System.out.println(line);
                }
                g1GCHeap.add(new G1GCRegion(type, top, bottom, end));
            }
        }
        eof = true;
        return heapOverTime.values().stream();
    }

    private double getUpTime(Matcher decorators) {
        String value = decorators.group(2);
        if (value != null) {
            value = value.replace(",", ".");
            return Double.parseDouble(value.substring(1, value.length() - 2));
        }
        return -1.0d;
    }
}
