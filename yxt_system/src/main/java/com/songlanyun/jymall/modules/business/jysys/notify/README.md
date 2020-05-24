## 消息通知使用方式
* 在model目录下面有一个`NotifyModel`的实体，使用该实体作为消息发送的容器
+ 使用模板发送消息    
  - 填充`notifyTempId`属性，属性值为模板表中的模板ID
  - 然后填写`contents`属性，类型为`list数组`,list的长度为占位符的数量，占位符在模板表中有具体统计出来的数量
  - `remark`属性选填
  - `userIds`属性不填则推送给所有用户
  - `type`属性自动为模板的属性
```java
import org.springframework.beans.factory.annotation.Autowired;
@Autowired
private NotifyControll notifyController;

public static void main(String[] args){
  NotifyModel notifyModel = new NotifyModel();
  notifyModel.setNotifyTempId("模板ID");

  List<String> contents = new ArrayList<>();
  contents.add("占位符1");
  contents.add("占位符2");
  contents.add("占位符3");
  //有多少个占位符就add多少个，超过的无用，少了自动使用空字符串替换
  notifyModel.setContents(contents);

  List<Long> userIds = new ArrayList<>();
  userIds.add("接收消息的用户ID");
  userIds.add("接收消息的用户ID");
  userIds.add("接收消息的用户ID");
  //需要发送多少个用户就add多少个
  notifyModel.setUserIds(userIds);

  //如果全部推送可以传null,或者空的list
  notifyModel.setUserIds(new ArrayList<>());

  notifyController.sendNotify(notifyModel);
}
```

+ 发送自定义消息
  - 填充`type`属性，`type`是写死的，目前还没有确定下来什么代表什么
  - 填充`title`属性，通知消息的消息头
  - 填充`content`属性，通知消息内容
  - `remark`属性选填
  - `userIds`属性不填则推送给所有用户

```java
import org.springframework.beans.factory.annotation.Autowired;
@Autowired
private NotifyControll notifyController;

public static void main(String[] args){
  NotifyModel notifyModel = new NotifyModel();
  notifyModel.setNotifyTempId("模板ID");

  //直接填写任何内容，长度限制在500
  notifyModel.setContent("通知消息的内容");

  //通知消息的标题
  notifyModel.setTitle("通知消息的标题");

  //通知消息的类型
  notifyModel.setType("通知消息的类型");

  List<Long> userIds = new ArrayList<>();
  userIds.add("接收消息的用户ID");
  userIds.add("接收消息的用户ID");
  userIds.add("接收消息的用户ID");
  //需要发送多少个用户就add多少个
  notifyModel.setUserIds(userIds);

  //如果全部推送可以传null,或者空的list
  notifyModel.setUserIds(new ArrayList<>());

  //消息通知备注，选填
  notifyModel.setRemark("消息通知备注");

  notifyController.sendNotify(notifyModel);
}
```

* `NotifyModel`中有个`isTemp`方法，用来判断当前使用这个模板的时，是通过自定义模板发送通知消息还是使用自定义内容发送通知消息