package com.fahim.newapp.network;


import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.holder.ResponseBookHolder;
import com.fahim.newapp.holder.ResponseStandardHolder;
import com.fahim.newapp.holder.ResponseSubjectHolder;
import com.fahim.newapp.holder.StandardHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface APICall {


    @GET("index.php")
    Call<ResponseStandardHolder> callCreateStandard(@Query("api") String api,
                                                    @Query("standard") String std);

    @GET("index.php")
    Call<ResponseStandardHolder> callReadStandard(@Query("api") String api);

    @GET("index.php")
    Call<ResponseStandardHolder> callDeleteStandard(@Query("api") String api, @Query("id") int id);

    @GET("index.php")
    Call<ResponseStandardHolder> callEditStandard(@Query("api") String api, @Query("id") int id,
                                                  @Query("standardName") String stdname);


    @GET("index.php")
    Call<ResponseSubjectHolder> callReadSubject(@Query("api") String api);

    @GET("index.php")
    Call<ResponseSubjectHolder> callEditSubject(@Query("api") String api, @Query("subjectName") String name,
                                                @Query("id") int id);

    @GET("index.php")
    Call<ResponseSubjectHolder> callCreateSubject(@Query("api") String api,
                                                  @Query("subjectname") String subject,
                                                  @Query("standard_id") int std);

    @GET("index.php")
    Call<ResponseSubjectHolder> callDeleteSubject(@Query("api") String api, @Query("id") int id);

    //BOOK TABLE
    @GET("index.php")
    Call<ResponseBookHolder> callReadBook(@Query("api") String api);

    @POST("index.php")
    Call<ResponseBookHolder> callEditBook(@Query("api") String api, @Body BookHolder bookHolder);

    @POST("index.php")
    Call<ResponseBookHolder> callCreateBook(@Query("api") String createBookApi, @Body BookHolder bookHolder);

    @GET("index.php")
    Call<ResponseSubjectHolder> callDeleteBook(@Query("api")String delete_book_api,@Query("id") int selectedBookId);

    @Streaming
    @GET
    Call<ResponseBody> downloadFileByUrl(@Url String fileUrl);
    /*
    @GET("ValidateUser")
    Call<LoginResponseModel> validateUser(@Query("User_Name") String userName);

    @GET("GetModulesPermission")
    Call<PermissionResponseModel> callPermissionApi(@Query("User_Name") String userName,
                                                    @Query("AppName") String appName);

    @GET("ValidateUserClientUID")
    Call<ClientUIDResponseModel> validateClientUid(@Query("User_Name") String userName);

    @GET("IsUserRegisteredAlready")
    Call<Boolean> checkUserName(@Query("User_Name") String userName);

    @GET("UpdateMacAddress")
    Call<String> updateMacAddress(@Query("User_Name") String userName,
                                  @Query("Mac_Address") String uUid);
    @POST("RegisterUser")
    Call<String> registerNewUser(@Body RegisterRequestModel registerRequestModel);

    @POST("ValidateSubLevelUser")
    Call<String> validateSubLevelUser(@Body RegisterDeviceModel registerDeviceModel);

    @POST
    Call<List<CloudHolder>> loadStock(@Url String url);

    @POST("SaveThsSerialNumber")
    Call<MessageSuccessResponseModel> setTHSNumber(@Body SerialTHSHolder holder);

    @GET("GetStockCheckCount")
    Call<StockCountResponseModel> getStockCount(@Query("User_Name") String userName);

    @GET("GetTermsAndCondition")
    Call<TermsResponseModel> getTerms(@Query("ClientUid") String clientUid);

    @POST("CompareItemHistory")
    Call<MisCompareMainHolder> getItemHistory(@Body MissCompareBodyHolder missCompareBodyHolder);

    @POST("StockCheckReport_AdvancedIce")
    Call<String> stockCheckReport(@Query("StockLoadLocation") String location,
                                  @Body StockCheckDetails holder);

    @GET
    Call<String> connectERP(@Url String url);

    @GET
    Call<HashMap<String, String>> getWinGoldSuperUserLoc(@Url String url);

    @GET
    Call<HashMap<String, String>> GetStockPoint(@Url String url);

    @GET("GetClientMapping")
    Call<ExistingMapHolder> getClientMapping(@Query("User_Name") String userName);

    @GET
    Call<List<String>> getClientColumns(@Url String url);

    @POST
    @Streaming
    Call<ResponseBody> downloadWingold(@Url String url,
                                       @Body ERPHolder holder);

    @POST("SaveMapping")
    Call<Object> SaveMapping(@Body SaveMapHolder holder);

    @POST
    @Streaming
    Call<ResponseBody> downloadWingoldWithLoc(@Url String url,
                                              @Body HashMap<String, String> body);

    @GET("GetLocation")
    Call<HashMap<String, String>> getLocationForErp(@Query("User_Name") String userName);

    @GET("getStockPoint")
    Call<HashMap<String, String>> getStockPoint(@Query("User_Name") String userName);

    @GET
    Call<List<String>> getLocationSuntechSuperUser(@Url String url);

    @GET
    Call<List<String>> getApiForMalabar(@Url String url);

    @POST
    Call<List<CloudHolder>> GetStockWithTextByType(@Url String url);

    @POST
    Call<List<CloudHolder>> getExtraItemDetails(@Url String url,
                                                @Body MismatchDetailHolder holder);

    @GET("GetStockCheckTransaction_AdvancedIce")
    Call<List<StockReportMainHolder>> getStockCheckTransaction(@Query("location") String location,
                                                               @Query("FromDate") String FromDate,
                                                               @Query("ToDate") String ToDate,
                                                               @Query("User_Name") String userName,
                                                               @Query("IgnoreDate") boolean IgnoreDate);

    @GET("GetLocation_StdVersion")
    Call<List<String>> getLocation_StdVersion(@Query("User_Name") String userName);

    @GET("GetStockCheckTransactionDetails")
    Call<List<MismatchTags>> getStockCheckTransactionDetails(@Query("TID") String TID,
                                                             @Query("Status") String Status,
                                                             @Query("User_Name") String userName);

    @POST("GetCategorywiseMissing_AdvancedIce")
    Call<MissingCategoryDashboardHolder> getCategorywiseMissing(@Query("FromDate") String FromDate,
                                                                @Query("ToDate") String ToDate,
                                                                @Query("User_Name") String userName,
                                                                @Body ArrayList<String> locationlist,
                                                                @Query("IgnoreDate") boolean ignoreDate);

    @GET("InsertConsignmentData")
    Call<Object> callInsertConsignmentData(@Body String toJson);

    @POST
    Call<String> stockCheckReportAdvance(@Url String url,
                                         @Query("StockLoadLocation") String location,
                                         @Body StockCheckDetails holder);

    @GET("GetUsersWithLastLoginDate")
    Call<UserDataHolder> callGetUsersWithLastLoginDate(@Query("User_Name") String userName);

    @GET("DeleteSubLevelUser")
    Call<String> deleteSubLevelUser(@Query("User_Name") String userName,
                                    @Query("user") String user_name);

    @GET
    Call<HashMap<String, String>> getLocationWithUrl(@Url String url);

    @POST("RegisterUserFromSuperuser")
    Call<String> postRegisterUserFromSuperuser(@Body AddUserHolder locationlist);

    @POST("updateModulePermission")
    Call<Object> updateModulePermission(@Query("User_Name") String userName,
                                        @Query("user") String user_name,
                                        @Body List<String> locationList);

    @GET("GetModules")
    Call<List<ThirdChildHolder>> callGetModules(@Query("User_Name") String userName);

    @POST("updateLocationPermission")
    Call<Object> updateLocationPermission(@Query("User_Name") String userName,
                                          @Query("user") String user_name,
                                          @Body HashMap<String, String> locationList);

    @POST("updateStockPointPermission")
    Call<Object> updateStockPointPermission(@Query("User_Name") String userName,
                                            @Query("user") String user_name,
                                            @Body HashMap<String, String> locationList);

    *//**
     to test stock report with local url;
     *//*
    @GET
    Call<List<StockReportMainHolder>> getStockCheckTransactionAdvance(@Url String url,
                                                                      @Query("location") String location,
                                                                      @Query("FromDate") String FromDate,
                                                                      @Query("ToDate") String ToDate,
                                                                      @Query("User_Name") String userName);

    @GET("InsertQuotationData")
    Call<Object> updateQuotation(@Query("strJSONData") String json);

    @POST("Get_FoundZoneDetails")
    Call<List<ZoneMissingHolder>> getFoundZoneDetails(@Query("user_name") String userName,
                                                      @Body ArrayList<String> missingListItemcode);

    @GET("GetZoneDetails")
    Call<List<LocationZoneHolder>> getSavedZoneLocations(@Query("User_Name") String userName);

    @POST("SaveZone")
    Call<Object> saveLocZone(@Body LocZoneRequestHolder locZoneRequestHolder);

    @POST("DeleteZone")
    Call<Object> deleteZone(@Body LocZoneRequestHolder locZoneRequestHolder);
    @POST("ClearMapping")
    Call<String> clearMappingApi(@Query("user_name") String user);

    @POST("SaveUser_Data")
    Call<String> uploadScannedData(@Body CloudShareModel model);

    @GET("GetUser_Data")
    Call<List<CloudShareResponseModel>> getSavedStockData(@Query("username") String userName);
*/

}
