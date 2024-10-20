package com.example.freshyzo.model

data class CategoryResponse(
    val product_category_id: String,
    val product_category_name: String,
    val product_category_title: String?,
    val have_sub_category: String,
    val category_status: String,
    val category_image: String?,
    val outlet_id: String
)

data class CategoryListResponse(
    val categoryList: List<CategoryResponse>
)