package com.example.sel.base.model

class ResponseItemHomeMenu{

    var data: HomeMenu?=null

}
class HomeMenu {
    var menutitle : String?=null
    var item: List<ItemHomeMenu>? = null

}
data class ItemHomeMenu (
    var name : String?= null,
    var type : String?= null,
    var image : String?= null,
    var action : String?= null,
    var actionUrl : String?= null,
    var isForceUpdate : Boolean?= null,
    var lock : Boolean? = null,
    var lockImage : String?= null,
    var description : String?= null,
    var buttonName: String?= null,
)

