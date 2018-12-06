package com.yofish.netmodule.retrofit.download.DownLoadListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 自定义精度的body
 * 
 * @author wzg
 */
public class DownloadResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private DownloadProgressListener progressListener;
    private BufferedSource bufferedSource;
    private long timestamp;
    private long readstamp;
    /* 默认间隔时间为2秒 */
    private final long restTime = 1000;
    /* 起始间隔时间为0.1秒 */
    private final long firstRestTime = 100;
    /* 为了获取初始下载速度设定的标识 */
    private boolean first = true;

    public DownloadResponseBody(ResponseBody responseBody, DownloadProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                if (timestamp == 0) {
                    timestamp = System.currentTimeMillis();
                }
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source
                // is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (null != progressListener) {
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);

                    long currentTime = System.currentTimeMillis();
                    long t = first ? firstRestTime : restTime;
                    if (currentTime - timestamp > t) {
                        /* 每秒下载速度 */
                        long speed = (totalBytesRead - readstamp) / (currentTime - timestamp) * 1000;
                        long pridictFinishSecond = speed == 0 ? 0 : (responseBody.contentLength() - totalBytesRead)
                                / speed;
                        progressListener.speedPerSecond(speed);
                        progressListener.pridictFinishSecond(pridictFinishSecond);
                        timestamp = currentTime;
                        readstamp = totalBytesRead;
                        if (first) {
                            first = false;
                        }
                    }
                }
                return bytesRead;
            }
        };

    }
}
