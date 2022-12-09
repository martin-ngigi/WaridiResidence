package com.example.waridiresidence.data.model.modelresponse.house


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UserHouseResponse(

    /**
     *
    {
    "id": 1,
    "houses": [
    {
    "id": 1,
    "images": [
    {
    "id": 1,
    "title": "Kitchen Image",
    "description": "Best kitche ever",
    "url": "https://foyr.com/learn/wp-content/uploads/2021/06/one-wall-kitchen-design.jpg",
    "house": 1
    },
    {
    "id": 2,
    "title": "Bedroom Image",
    "description": "Best Bedroom ever",
    "url": "https://wpmedia.roomsketcher.com/content/uploads/2021/12/09103646/Subtle_Blue_Shades_Large_Bedroom_idea-1024x768.jpg",
    "house": 1
    }
    ],
    "title": "House 1",
    "category": "One Bedroom",
    "description": "Very spacious room",
    "loaction": "Lavington, Nairobi",
    "status": "Vacant",
    "add_date": "2022-12-04T19:31:38.993000Z",
    "monthly_price": 100000,
    "user": 1
    }
    ],
    "agent_id": 1,
    "agent_name": "Martin Ngigi",
    "phone": "071234567"
    }
     */

    @SerializedName("agent_id")
    val agentId: Int,
    @SerializedName("agent_name")
    val agentName: String,
    @SerializedName("houses")
    val houses: List<House>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("phone")
    val phone: String
)