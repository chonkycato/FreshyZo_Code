package com.example.freshyzo.api

// OTP Generation
/** https://freshyzo.com/admin/Customer_App_Api/send_otp
//{
//    "mobile_no": "7047112327"
//    "method": "signup" //login for login
//} **/

// Login Authentication
/** for otp authentication-post method
//https://freshyzo.com/admin/Customer_App_Api/auth
//{
//    "mobile_no": "7047112327",
//    "otp":"237552"
//
//}
//server response-{"customer_id":"745","customer_role":"customers"} **/

// User Registration
/** method: "post",
// url: https://freshyzo.com/admin/Customer_App_Api/signup
//payload-first_name, last_name, mobile_no, colony, otp
//response-
//"Invalid or expired OTP."
//'failed'
//{
//    'ragistration_id' => $ragistration_id,
//    'customer_role' => 'ragister_customer',
//}; **/

// Product Category api
/** api link-https://freshyzo.com/admin/Customer_App_Api/product_category_list
//  response-[{
"product_category_id":"4",
"product_category_name":"MilkProducts",
"product_category_title":"",
"have_sub_category":"yes",
"category_status":"active",
"category_image":"352041682413819117a7006.png",
"outlet_id":"1"},
//    image link example-https://freshyzo.com/admin/uploads/product_category_image/352041682413819117a7006.png */

//Product List API
/** Updated product list api
 * link-https://freshyzo.com/admin/Customer_App_Api/fetch_product
 * payload -
 * {
 *      product_id: "",
 *      customer_id: "",
 *      category_id: ""
 * }
 * for image base url-https://freshyzo.com/admin/uploads/product_image/ +name of the product image
 * exmaple- https://freshyzo.com/admin/uploads/product_image/18346172641582750943874.webp
 *
 * response -
 * [{"product_id":"46","product_name":"Cow ghee 500nml","dairy_product_image":"18346172641582750943874.webp",
 * "unit":"Pcs","product_price":"400","dairy_mrp":"500.00","product_type":"product","required_milk":"0.00",
 * "product_status":"active","dairy_product_id":"46"},...] */

// Colony API
/** url: https://freshyzo.com/admin/Customer_App_Api/get_all_colony
//data: {
//    "colony": [
//        {
//            "colony_id": "2",
//            "colony_name": "Adarsh nagar"
//        }, ...
//      ]
//  } */

// Carousel Banner API
/** api link- https://freshyzo.com/admin/Customer_App_Api/banner_list
//response - {"banner_list":
//              [
//                {
//                      "offer_banner_id":"1",
//                      "img_name":"214071727963239318d32f4.jpeg"
//                  }
//              ]
//         }
// example- https://freshyzo.com/admin/uploads/offer_banner_image/214071727963239318d32f4.jpeg */

// Profile Data API
/** API Link- https://freshyzo.com/admin/Customer_App_Api/profile_data
payload -
{
customer_id: "1",
customer_role: "guest"
}
[{"customer_image":"user.png","first_name":"vivek","last_name":"devloper","contact_1":"7067428075","address":"test","colony_id":"3","colony_name":"Daldal
seoni \/ Mowa"}]

{
"customer_id": "1",
"customer_role": "customer"
}
[{"customer_image":"user.png","first_name":"rahul","last_name":"verma","contact_1":"8319471979","address":"shankar
nagar","colony_id":"5","colony_name":"shankar nagar"}]*/

//Update Profile Data
/** API link- https://freshyzo.com/admin/Customer_App_Api/update_profile
payload -
{
customer_id
role:"",
first_name:"",
last_name:"",
number:"",
address:"",
colony_id:""
}
response-
{"Profile updated successfully"}
{"Failed"} */

// Recharge History API
/** link-https://freshyzo.com/admin/Customer_App_Api/fetch_recharges
payload-
{
"customer_id": "745",
"customer_role": "customer"
}
response-
{
"recharge_id": "6169",
"customer_id": "745",
"guest_id": null,
"recharge_amount": "26",
"user_id": "1",
"recharge_date": "2024-06-21 16:44:00",
"payment_id": "",
"customer_type": "membership"
},
{
"recharge_id": "3063",
"customer_id": "745",
"guest_id": null,
"recharge_amount": "1",
"user_id": "21",
"recharge_date": "2023-11-04 14:34:22",
"payment_id": "745ORDS32851539",
"customer_type": "membership"
},vvm
 */

// Order Details API
/** API Link- https://freshyzo.com/admin/Customer_App_Api/fetch_orders
payload-
{
"customer_id": "745",
"customer_role": "customer"
}
response-
[
{
"online_order_details": "[{\"item_id\":\"2\",\"item_name\":\"cow milk 1000 ml\",\"item_qty\":\"1\",\"item_price\":\"55\",\"item_img\":\"\",\"item_gst\":10}]",
"total_order_price": "65.00",
"delivery_charge": "0.00",
"point_discount": "0.00",
"coupan_discount": "0.00",
"online_order_date": "2023-10-12 22:49:06",
"delivery_date": "2023-10-13 00:00:00",
"order_status": "canceled"
},
{
"online_order_details": "[{\"item_id\":11,\"item_name\":\"paneer 250 gm\",\"item_qty\":1,\"item_price\":80,\"item_img\":\"6074116824141793d50b851.jpg\",\"item_gst\":0}]",
"total_order_price": "80.00",
"delivery_charge": "0.00",
"point_discount": "0.00",
"coupan_discount": "0.00",
"online_order_date": "2023-12-14 16:11:23",
"delivery_date": "2023-12-15 00:00:00",
"order_status": "placed"
},*/