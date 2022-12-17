package com.example.noteapp

class Notes {

   var id:String?=null
    var title:String?=null
     var body:String?=null
     var timestamp: String?=null

    constructor(){}
    constructor(  id:String,
                  title:String,
                  body:String,
                  timestamp:String){
    this.id=id
    this.title=title
    this.body=body
    this.timestamp=timestamp
    }

}