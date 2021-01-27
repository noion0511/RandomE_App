package com.noion.randome.domain.randomchat

//서버에서는 메세지 오너에 대한 정의가 없었지만 앱에서는 메세지의 주인을 표시해주기 위해 OWNER를 정의
data class MessageModel(
        val owner: Owner,
        val nickName: String,
        val content: String
) {
    //상대방의 메세지가 연달아 표시되거나 나의 메세지가 연달아 표시되는 경우
    //이름을 가리고 메세지 사이의 여백을 줄이는 등의 기능을 수행하기 위해 필요한 프로퍼티
    var collapseName: Boolean = false

    //리싸이클러뷰 어댑터에는 뷰타입 번호에 따라 다른 뷰를 그려주는 기능이 있음
    //이 기능을 사용하기 위해 SENDER, RECEIVER에 번호를 부여함
    enum class Owner(val viewType: Int) {
        SENDER(0),
        RECEIVER(1);
    }
}