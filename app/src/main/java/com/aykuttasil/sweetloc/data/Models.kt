package com.aykuttasil.sweetloc.data


import java.text.DateFormat
import java.util.Date

data class Room(
        var roomName: String? = null,
        var roomOwner: String? = null,
        var createDate: Long? = Date().time,
        var createDateString: String? = DateFormat.getInstance().format(Date())
)


fun roomsNode() = "rooms"
fun roomNode(roomId: String) = "${roomsNode()}/$roomId"
fun roomMembersNode(roomId: String) = "${roomsNode()}/$roomId/members"


fun usersNode() = "users"
fun userNode(userId: String) = "${usersNode()}/$userId"
fun userRoomsNode(userId: String) = "${usersNode()}/$userId/rooms"
fun userRoomNode(userId: String, roomId: String) = "${usersNode()}/$userId/rooms/$roomId"
fun userLocationsNode(userId: String) = "${usersNode()}/$userId/locations"

