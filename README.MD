## 介绍
可以实时获取直播间的弹幕，并使用语音读出来弹幕内容

* 项目编码需要使用UTF-8
* 项目需要在Java classpath下放置jacob-1.18-x64.dll才能正常调用windows语音，dll文件在resources文件夹下
* 启动项目需要添加VM options: -Dfile.encoding=gbk
* 想要监听的直播间id，在直播间url上，需要配置在WebSocketRequestCons中CERTIFICATION_REQUEST_JSON字段的roomid参数后再启动

## 待办
* 目前只有windows系统声音，未来考虑接入第三方语音
* 为了增加更多bilibili直播间交互的处理，比如送礼物等