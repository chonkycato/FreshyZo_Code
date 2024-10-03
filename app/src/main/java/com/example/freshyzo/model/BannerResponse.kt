package com.example.freshyzo.model


data class BannerResponse(
    val banner_list: List<Banner>
)

data class Banner(
    val offer_banner_id: String,
    val img_name: String
)