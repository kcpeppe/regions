package com.jclarity.region.io;


import com.jclarity.region.model.G1GCRegion;
import com.jclarity.region.model.RegionType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegionStream {

    //###   EDEN 0x00000007b4800000-0x00000007b4c00000    4194304    4194304    4194304             0.0       5832         16
    private String HEX_ADDRESS = "0x([0-9,a-f]{16})";
    private String INTEGER = "(\\d+)";
    private String DOUBLE = "(\\d+(?:\\.|,)\\d+)";
    private String SPACE = "\\s+";

    private Pattern REGION_LOG_ENTRY = Pattern.compile("^###\\s+(FREE|EDEN|SURV|OLD|HUMS|HUMC)\\s+" + HEX_ADDRESS + "-" + HEX_ADDRESS + SPACE + INTEGER + SPACE + INTEGER + SPACE + INTEGER + SPACE + DOUBLE + SPACE + INTEGER + SPACE + INTEGER);
    private int REGION_TYPE = 1;
    private int LOWER_ADDRESS = 2;
    private int UPPER_ADDRESS = 3;
    private int USED = 4;
    private int PREVIOUS_LIVE = 5;
    private int NEXT_LIVE = 6;
    private int GC_EFFICIENCY = 7;
    private int RSET_SIZE = 8;
    private int CODE_ROOTS_SIZE = 9;

    private BufferedReader reader;
    private boolean eof = false;

    public RegionStream(Path file) throws IOException {
        reader = Files.newBufferedReader(file);
    }

    public void readBlock( RegionStreamDataSink dataSink) throws IOException {
        G1GCRegion g1GCRegion;
        while ( ( g1GCRegion = readRegion()) != null)
            dataSink.add(g1GCRegion);
    }

    public boolean isEof() { return eof; }

    private G1GCRegion readRegion() throws IOException {
        String line;
        Matcher matcher;
        while ( (line = reader.readLine()) != null) {
            if (line.startsWith("### SUMMARY "))
                return null;
            matcher = REGION_LOG_ENTRY.matcher(line);
            if ( matcher.find()) {
                RegionType type = RegionType.valueOf(matcher.group(REGION_TYPE));
                long lowerAddress = Long.parseLong(matcher.group(LOWER_ADDRESS),16);
                long upperAddress = Long.parseLong(matcher.group(UPPER_ADDRESS),16);
                int used = Integer.parseInt(matcher.group(USED));
                int previousLive = Integer.parseInt(matcher.group(PREVIOUS_LIVE));
                int nextLive = Integer.parseInt(matcher.group(NEXT_LIVE));
                double gcEfficiency = Double.parseDouble(matcher.group(GC_EFFICIENCY));
                int rsetSize = Integer.parseInt(matcher.group(RSET_SIZE));
                int codeRootSize = Integer.parseInt(matcher.group(CODE_ROOTS_SIZE));
                return new G1GCRegion(type, lowerAddress, upperAddress, used, previousLive, nextLive, gcEfficiency, rsetSize, codeRootSize);
            }
        }
        eof = true;
        return null;
    }
}
