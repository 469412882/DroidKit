package com.yofish.imagemodule.okhttpmodule;


public interface OnProgressListener {
    void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes);
}
