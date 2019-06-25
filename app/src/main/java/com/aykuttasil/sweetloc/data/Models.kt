/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.data

import android.location.Location
import com.aykuttasil.androidbasichelperlib.SuperHelper
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
    var userCity: String? = null
    // var userCreateDate: Long? = Date().time,
    // var userCreateDateString: String? = DateFormat.getInstance().format(Date())
)

data class RoomEntity(
    var roomId: String? = null,
    var roomName: String? = null,
    var roomOwner: String? = null,
    var createDate: Long? = Date().time,
    var createDateString: String? = DateFormat.getInstance().format(Date())
)

data class LocationEntity(
    var locationId: Long? = null,
    var name: String? = null,
    var address: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var accuracy: Float? = null,
    var time: Long? = null,
    var formatTime: String? = null,
    var speed: Long? = null,
    var createDate: String? = null,
    var placesName: String? = null
)

/*
data class RoomLocationModel(
    var user: UserEntity? = null,
    var location: LocationEntity? = null
)
*/

data class RoomMemberLocationModel(
    var user: UserModel? = null,
    var location: LocationEntity? = null
)

fun RoomEntity.toRoomMemberLocationModel(userModel: UserModel, location: LocationEntity): RoomMemberLocationModel {
    return RoomMemberLocationModel(userModel, location)
}

fun Location.toLocationEntity(): LocationEntity {
    return LocationEntity(
        latitude = this.latitude,
        longitude = this.longitude,
        accuracy = this.accuracy,
        address = this.provider,
        time = this.time,
        formatTime = SuperHelper.getFormatTime(this.time),
        createDate = SuperHelper.formatTime
    )
}

fun roomsNode() = "rooms"
fun roomNode(roomId: String) = "${roomsNode()}/$roomId"

fun roomMembersNode(roomId: String) = "${roomNode(roomId)}/members"
fun roomMemberNode(roomId: String, userId: String) = "${roomMembersNode(roomId)}/$userId"
fun roomMemberLocationNode(roomId: String, userId: String) = "${roomMemberNode(roomId,userId)}/location"
fun roomMemberUserNode(roomId: String, userId: String) = "${roomMemberNode(roomId,userId)}/user"

// fun roomLocationsNode(roomId: String) = "${roomNode(roomId)}/locations"

// fun roomMembersLocationNode(roomId: String) = "${roomMembersNode(roomId)}/location"

/*----*/

fun usersNode() = "users"
fun userNode(userId: String) = "${usersNode()}/$userId"

fun userRoomsNode(userId: String) = "${userNode(userId)}/rooms"
fun userRoomNode(userId: String, roomId: String) = "${userRoomsNode(userId)}/$roomId"

fun userLocationsNode(userId: String) = "${userNode(userId)}/locations"
