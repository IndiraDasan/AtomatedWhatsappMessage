package com.example.automatedwhatsappmessage.api

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    val status: Boolean,
    val message: String = "Default Response",
    val total_entries: Int = 0,
    val results: T
)


@Keep
data class ErrorResponse(val status: Boolean, val message: String, val data: List<String>)


data class CallLogData(val count:String,val callLogName:String)
data class SubsCriptionData(val heading:String,val subHeading:String ,val days:String,)



data class RegisterLogin (
    @SerializedName("DeviceUniqueId")
    @Expose
    var deviceUniqueId: String? = null,

    @SerializedName("DeviceName")
    @Expose
    var deviceName: String? = null,

    @SerializedName("LoginMobileNumber")
    @Expose
    var loginMobileNumber: String? = null,

    @SerializedName("UserPassword")
    @Expose
    var userPassword: String? = null,

    @SerializedName("CustomerName")
    @Expose
    var customerName: String? = null,

    @SerializedName("Email")
    @Expose
    var email: String? = null,

    @SerializedName("AccountType")
    @Expose
    var accountType: String? = null,

    @SerializedName("BusinessMobileNumber")
    @Expose
    var businessMobileNumber: String? = null,

    @SerializedName("BusinessName")
    @Expose
    var businessName: String? = null,

    @SerializedName("BusinessAddress")
    @Expose
    var businessAddress: String? = null
)


data class Login(
    @SerializedName("LoginMobileNumber")
    @Expose
    var loginMobileNumber: String? = null,

    @SerializedName("UserPassword")
    @Expose
var userPassword: String? = null
)
@Keep
data class DeviceDetail(

    @SerializedName("deviceuniqueid")
    @Expose
    var deviceuniqueid: String? = null,


    @SerializedName("templatetype")
    @Expose
    var templatetype: String? = null,
)

@Keep
data class TemplateBody(
    @SerializedName("LoginMobileNumber")
    @Expose
    var loginMobileNumber: String? = null,
)

@Keep
data class TemplateList (

    @SerializedName("messagetemplateid") var messagetemplateid: Int? = null,
    @SerializedName("userregistrationid") var userregistrationid: Int? = null,
    @SerializedName("templatename") var templatename: String? = null,
    @SerializedName("templatecontent") var templatecontent: String? = null,
    @SerializedName("templateimage") var templateimage: String? = null,
    @SerializedName("templatelocation") var templatelocation: String? = null,
    @SerializedName("templatetype") var templatetype: String? = null,
    @SerializedName("isactive") var isactive: Int? = null,
    @SerializedName("createdon") var createdon: String? = null

)






@Keep
data class UserProfile (

    @SerializedName("userregistrationid") var userregistrationid: Int? = null,
    @SerializedName("deviceuniqueid") var deviceuniqueid: String? = null,
    @SerializedName("devicename") var devicename: String? = null,
    @SerializedName("loginmobilenumber") var loginmobilenumber: String? = null,
    @SerializedName("userpassword") var userpassword: String? = null,
    @SerializedName("customername") var customername: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("accounttype") var accounttype: String? = null,
    @SerializedName("businessmobilenumber") var businessmobilenumber: String? = null,
    @SerializedName("businessname") var businessname: String? = null,
    @SerializedName("businessaddress") var businessaddress: String? = null,
    @SerializedName("isactive") var isactive: Int? = null,
    @SerializedName("appstatus") var appstatus: Int? = null,
    @SerializedName("issubscription") var issubscription: Int? = null,
    @SerializedName("createdon") var createdon: String? = null,
    @SerializedName("lastloginon") var lastloginon: String? = null,
    @SerializedName("isuserloggedin") var isuserloggedin: Int? = null

)

@Keep
data class AddTemplateBody (
    @SerializedName("LoginMobileNumber") var loginMobileNumber: String? = null,
    @SerializedName("MessageTemplateId") var messageTemplateId: String? = null,
    @SerializedName("TemplateName") var templateName: String? = null,
    @SerializedName("TemplateContent") var templateContent: String? = null,
    @SerializedName("TemplateImage") var templateImage: String? = null,
    @SerializedName("TemplateLocation") var templateLocation: String? = null,
    @SerializedName("TemplateType") var templateType: String? = null
)


data class AppStatusBody (

    @SerializedName("LoginMobileNumber") var LoginMobileNumber: String? = null,
    @SerializedName("AppStatus") var AppStatus: String? = null

)

data class MessageTypeBody (
    @SerializedName("LoginMobileNumber") var loginMobileNumber: String? = null,
    @SerializedName("MessageType") var messageType: String? = null

)


@Keep
data class DeleteTemplateBody (
    @SerializedName("LoginMobileNumber") var loginMobileNumber: String? = null,
    @SerializedName("MessageTemplateId") var messageTemplateId: String? = null,
)

@Keep
data class AddCallStatus (
    @SerializedName("LoginMobileNumber") var LoginMobileNumber: String? = null,
    @SerializedName("MessageTo") var MessageTo: String? = null,
    @SerializedName("CallType") var CallType: String? = null,
    @SerializedName("MessageStatus") var MessageStatus: String? = null

)
@Keep
data class AddCallStatusResults (
    @SerializedName("messagecountid") var messagecountid: Int? = null,
    @SerializedName("userregistrationid") var userregistrationid: Int? = null,
    @SerializedName("messageto") var messageto: String? = null,
    @SerializedName("calltype") var calltype: String? = null,
    @SerializedName("messagestatus") var messagestatus: Int? = null,
    @SerializedName("isactive") var isactive: Int? = null,
    @SerializedName("createdon") var createdon: String? = null
)

@Keep
data class AppStatusResponse (
    @SerializedName("userregistrationid") var userregistrationid: Int? = null,
    @SerializedName("deviceuniqueid") var deviceuniqueid: String? = null,
    @SerializedName("devicename") var devicename: String? = null,
    @SerializedName("loginmobilenumber") var loginmobilenumber: String? = null,
    @SerializedName("userpassword") var userpassword: String? = null,
    @SerializedName("customername") var customername: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("accounttype") var accounttype: String? = null,
    @SerializedName("businessmobilenumber") var businessmobilenumber: String? = null,
    @SerializedName("businessname") var businessname: String? = null,
    @SerializedName("businessaddress") var businessaddress: String? = null,
    @SerializedName("isactive") var isactive: Int? = null,
    @SerializedName("appstatus") var appstatus: Int? = null,
    @SerializedName("issubscription") var issubscription: Int? = null,
    @SerializedName("createdon") var createdon: String? = null,
    @SerializedName("lastloginon") var lastloginon: String? = null,
    @SerializedName("isuserloggedin") var isuserloggedin: Int? = null,
    @SerializedName("messagetype") var messagetype: String? = null

)
@Keep
data class DeviceCheckResponse (

    @SerializedName("userregistrationid") var userregistrationid: Int? = null,
    @SerializedName("deviceuniqueid") var deviceuniqueid: String? = null,
    @SerializedName("devicename") var devicename: String? = null,
    @SerializedName("loginmobilenumber") var loginmobilenumber: String? = null,
    @SerializedName("userpassword") var userpassword: String? = null,
    @SerializedName("customername") var customername: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("accounttype") var accounttype: String? = null,
    @SerializedName("businessmobilenumber") var businessmobilenumber: String? = null,
    @SerializedName("businessname") var businessname: String? = null,
    @SerializedName("businessaddress") var businessaddress: String? = null,
    @SerializedName("isactive") var isactive: Int? = null,
    @SerializedName("appstatus") var appstatus: Int? = null,
    @SerializedName("issubscription") var issubscription: Int? = null,
    @SerializedName("createdon") var createdon: String? = null,
    @SerializedName("lastloginon") var lastloginon: String? = null,
    @SerializedName("isuserloggedin") var isuserloggedin: Int? = null,
    @SerializedName("messagetype") var messagetype: String? = null,
    @SerializedName("messageTemplate") var messageTemplate: ArrayList<MessageTemplate> = arrayListOf()

)

data class MessageTemplate (

    @SerializedName("messagetemplateid") var messagetemplateid: Int? = null,
    @SerializedName("userregistrationid") var userregistrationid: Int? = null,
    @SerializedName("templatename") var templatename: String? = null,
    @SerializedName("templatecontent") var templatecontent: String? = null,
    @SerializedName("templateimage") var templateimage: String? = null,
    @SerializedName("templatelocation") var templatelocation: String? = null,
    @SerializedName("templatetype") var templatetype: String? = null,
    @SerializedName("isactive") var isactive: Int? = null,
    @SerializedName("createdon") var createdon: String? = null

)


@Keep
enum class CallType{
    MISSEDCALL,
    RECEIVEDCALL,
    DAILEDCALL,
}

