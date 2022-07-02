## 介绍
可以实时获取直播间的弹幕，并使用语音读出来弹幕内容

* 项目编码需要使用UTF-8
* 启动项目需要添加VM options: -Dfile.encoding=gbk
* 想要监听的直播间id，在直播间url上，需要配置在WebSocketRequestCons中CERTIFICATION_REQUEST_JSON字段的roomid参数后再启动