package com.aykuttasil.sweetloc.data


import java.text.DateFormat
import java.util.Date

data class UserModel(
        var userId: String? = null,
        var userEmail: String? = null,
        var userPassword: String? = null,
        var userName: String? = null,
        var userSurname: String? = null,
        var userTel: String? = null,
        var userRegId: String? = null,
        var userToken: String? = null,
        var userImageUrl: String? = null,
        var userOneSignalId: String? = null,
        var userCity: String? = null,
        var userCreateDate: Long? = Date().time,
        var userCreateDateString: String? = DateFormat.getInstance().format(Date())
)

data class RoomEntity(
        var roomId: String? = null,
        var roomName: String? = null,
        var roomOwner: String? = null,
        var createDate: Long? = Date().time,
        var createDateString: String? = DateFormat.getInstance().format(Date())
)

data class LocationEntity(
        val locationId: Long? = null,
        val name: String? = null,
        val address: String? = null,
        val latitude: Double? = null,
        val longitude: Double? = null,
        val accuracy: Double? = null,
        val time: Long? = null,
        val formatTime: String? = null,
        val speed: Long? = null,
        val createDate: String? = null,
        val placesName: String? = null
)

fun roomsNode() = "rooms"
fun roomNode(roomId: String) = "${roomsNode()}/$roomId"
fun roomMembersNode(roomId: String) = "${roomNode(roomId)}/members"
fun roomMemberNode(roomId: String, userId: String) = "${roomMembersNode(roomId)}/$userId"
fun roomLocationsNode(roomId: String) = "${roomNode(roomId)}/locations"

fun usersNode() = "users"
fun userNode(userId: String) = "${usersNode()}/$userId"
fun userRoomsNode(userId: String) = "${userNode(userId)}/rooms"
fun userRoomNode(userId: String, roomId: String) = "${userRoomsNode(userId)}/$roomId"
fun userLocationsNode(userId: String) = "${userNode(userId)}/locations"

