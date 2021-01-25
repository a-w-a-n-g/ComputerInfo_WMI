## 该项目通过jacob调用wmi获取远程电脑的各信息

### 关于项目架构
***
* 该项目通过Maven构建，其中使用了一个本地依赖--jacob。使用Hibernate连接Mysql数据库，数据库、用户名、密码都在application.properties中配置。该项目主要通过jacob调用Windows原装的WMI接口，来去获取远程电脑的信息。其中src\main\java\com\awang\WMI\service下的才为项目调用WMI所使用到的类，src\main\java\com\awang\WMI\other下的类为使用其它框架调用WMI的代码，没有成功实现算是**半成品**。

### 关于运行项目
***
1. 首先配置好所需获取电脑信息的excel文档，该文档中三列分别为：电脑名或IP、用户名、密码。所使用用户得是所获取电脑的管理员（注：域管理员默认是域中所有电脑的管理员），后两列可以不填，若不填则使用com.awang.WMI.service.WMIByJacob类中的默认值。若电脑名为local时，则获取本地电脑的信息。
2. 运行com.awang.WMI.service.ConnectAndGetInfo类，然后选择上述的excel文档。即会获取文档中的各电脑信息。

### 常见问题
***
1. 该项目仅可以获取网线连接的电脑的信息，而不能获取到使用wifi连接的电脑的信息。