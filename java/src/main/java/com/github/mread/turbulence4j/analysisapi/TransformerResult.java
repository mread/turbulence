package com.github.mread.turbulence4j.analysisapi;

public interface TransformerResult<T> {

    TransformerResult<String> NEVER_RUN = new TransformerResult<String>() {
        @Override
        public String getResult() {
            return "never run";
        }
    };

    T getResult();
}
