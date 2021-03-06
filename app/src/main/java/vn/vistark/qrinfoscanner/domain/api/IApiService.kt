package vn.vistark.qrinfoscanner.domain.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import vn.vistark.qrinfoscanner.domain.DTOs.*
import vn.vistark.qrinfoscanner.domain.api.requests.material_batch.GetMaterialBatchBody
import vn.vistark.qrinfoscanner.domain.api.requests.material_ship.GetMaterialShipBody
import vn.vistark.qrinfoscanner.domain.api.requests.technical_data.GetTechnicalDataBody
import vn.vistark.qrinfoscanner.domain.api.requests.technical_data.GetTechnicalDataDetailBody
import vn.vistark.qrinfoscanner.domain.api.responses.account.AccountSuccessfulRespone
import vn.vistark.qrinfoscanner.domain.api.responses.login.LoginSuccessfulResponse
import vn.vistark.qrinfoscanner.domain.api.responses.material_batch.MaterialBatchCreateSuccessfulResponse
import vn.vistark.qrinfoscanner.domain.api.responses.material_ship.CreateMaterialShipSuccessfulResponse
import vn.vistark.qrinfoscanner.domain.api.responses.register.RegisterSuccessfulResponse
import vn.vistark.qrinfoscanner.domain.api.responses.shipment.ShipmentCreateSuccessfulResponse
import vn.vistark.qrinfoscanner.domain.api.responses.upload_qr_code.UploadQRSuccessfulResponse
import vn.vistark.qrinfoscanner.domain.entities.*

interface IApiService {
    companion object {
        const val BASE_URL = "https://gdst.vn/"
    }

    @GET("api/locations")
    fun getGDSTLocations(): Call<ArrayList<GDSTLocation>>

    @GET("api/countries")
    fun getGDSTCountries(): Call<ArrayList<GDSTCountry>>

    @GET("api/species")
    fun getGDSTSpecies(): Call<ArrayList<GDSTSpecie>>

    @GET("api/gear-types")
    fun getGDSTGearTypes(): Call<ArrayList<GDSTGearType>>

    @GET("api/ports")
    fun getGDSTPorts(): Call<ArrayList<GDSTPort>>

    @GET("api/fipcodes")
    fun getGDSTFipCodes(): Call<ArrayList<GDSTFipCode>>

    @GET("api/product-froms")
    fun getGDSTProductForms(): Call<ArrayList<GDSTProductForm>>

    @GET("api/companies")
    fun getGDSTCompanies(): Call<ArrayList<GDSTCompany>>

    @POST("api/register")
    fun postGDSTRegister(@Body dto: GDSTUserRegisterDTO): Call<RegisterSuccessfulResponse>

    @POST("api/login")
    fun postGDSTLogin(@Body dto: GDSTUserLoginDTO): Call<LoginSuccessfulResponse>

    @GET("api/account")
    fun getGDSTProfile(): Call<AccountSuccessfulRespone>

    @GET("api/ships")
    fun getGDSTShip(): Call<ArrayList<GDSTShip>>

    @GET("api/shipments")
    fun getGDSTShipment(): Call<ArrayList<GDSTShipment>>

    @Multipart
    @POST("api/upload/qrcode")
    fun postGDSTUploadQrCode(@Part image: MultipartBody.Part): Call<UploadQRSuccessfulResponse>

    @POST("api/create-shipment")
    fun postGDSTShipment(@Body dto: GDSTShipmentCreateDTO): Call<ShipmentCreateSuccessfulResponse>

    @POST("api/material-bacths")
    fun getGDSTMaterialBatch(@Body body: GetMaterialBatchBody): Call<ArrayList<GDSTMaterialBacth>>

    @POST("api/create-merterial-batch")
    fun postGDSTMaterialBatch(@Body dto: GDSTMaterialBacthCreateDTO): Call<MaterialBatchCreateSuccessfulResponse>

    @POST("api/material-ships")
    fun getGDSTMaterialShip(@Body body: GetMaterialShipBody): Call<ArrayList<GDSTMaterialShip>>

    @POST("api/create-merterial-ship")
    fun postGDSTMaterialShip(@Body dto: GDSTMaterialShipCreateDTO): Call<CreateMaterialShipSuccessfulResponse>

    @POST("api/update-material-ship")
    fun postGDSTMaterialShipUpdate(@Body dto: GDSTMaterialShipUpdateDTO): Call<Any>

    @POST("api/technicaldata")
    fun postGDSTTechnicalData(@Body dto: GDSTTechnicalDataDTO): Call<Any>

    @POST("api/technicals")
    fun getGDSTTechnicalData(@Body body: GetTechnicalDataBody): Call<ArrayList<GDSTTechnicalData>>

    @POST("api/detail-technical")
    fun getGDSTTechnicalDataDetail(@Body body: GetTechnicalDataDetailBody): Call<ArrayList<GDSTTechnicalData>>

    @POST("api/update-technical")
    fun postGDSTTechnicalDataUpdate(@Body dto: GDSTTechnicalDataUpdateDTO): Call<Any>

    @POST("api/account/update")
    fun postGDSTUpdateProfile(@Body dto: GDSTProfileUpdateWithOutPassword): Call<Any>

    @POST("api/account/update")
    fun postGDSTUpdateProfile(@Body dto: GDSTProfileUpdateWithPassword): Call<Any>

    @Multipart
    @POST("api/upload/image")
    fun postGDSTUploadImage(@Part image: MultipartBody.Part): Call<UploadQRSuccessfulResponse>
}