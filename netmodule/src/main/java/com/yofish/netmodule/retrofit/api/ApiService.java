package com.yofish.netmodule.retrofit.api;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 通用的API
 *
 * Created by hch on 2017/6/28.
 */

public interface ApiService {

    @POST("{method}")
    @FormUrlEncoded
    Observable<ResponseBody> excutePost(@Path("method") String method, @FieldMap Map<String, String> params);

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("{method}")
    Observable<ResponseBody> excutePost(@Path("method") String method, @Body RequestBody requestBody);

    @GET("{url}")
    Observable<ResponseBody> excuteGet(@Path("url") String url);

    @Multipart
    @POST("{method}")
    Observable<ResponseBody> upLoadFile(@Path("method") String method, @Part MultipartBody.Part file);

    @Multipart
    @POST("{url}")
    Observable<ResponseBody> uploadFiles(@Path("url") String url, @PartMap Map<String, RequestBody> maps);

    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中, RANGE为从指定的地方下载*/
    @GET
    Observable<ResponseBody> downloadFile(@Header("RANGE") String start, @Url String fileUrl);
}
