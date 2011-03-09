package com.github.mread.turbulence4j.output;

import java.io.IOException;
import java.util.Map;

public interface CanWriteOutput {

    void write(Map<String, int[]> data) throws IOException;

}
