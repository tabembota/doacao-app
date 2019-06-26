package com.tabembota.doaacao.helper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EnviarEmailService {

    @GET("enviarEmail")
    Call<Integer> enviarEmail(@Query("dest") String dest, @Query("subj") String subj, @Query("corpo") String corpo);

}
