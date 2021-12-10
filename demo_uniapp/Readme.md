### 一、开发环境

1、修改接口api

![img](https://img.kancloud.cn/4e/ff/4efff062a973fab2d04045a2aafa56dd_1374x534.png)

### 二、打包

1、H5

![img](https://img.kancloud.cn/45/f1/45f1aa90a5954119782944fb3be0c0f8_1920x1080.png)

网站标题不要为空
![img](https://img.kancloud.cn/d2/fc/d2fca7cdcf96ae0a661f1f33b3908a66_426x195.png)

在这里可以填写网站的标题和请求的域名 ，默认的话 他的指向是服务器的根目录。
比如域名是 [www.test.com](http://www.test.com/)，那资源路径就是 [www.test.com/static/xxx.文件后缀名字，那static文件就要放在服务器的根目录下面](http://www.test.com/static/xxx.文件后缀名字，那static文件就要放在服务器的根目录下面) ，但这样不利于文件管理。这个时候就要选择高级 进行H5配置

![img](https://img.kancloud.cn/e7/9d/e79d5141fdaa260fe4cc6da892e07db3_757x590.png)

比如在服务器的根目录创建个为 [www.test.com/h5](http://www.test.com/h5) 那就在那个运行的路径上添加 /h5
这样的话资源的运行路径就是 [www.test.com/h5/xxx.文件后缀名，在根目录创建多少级](http://www.test.com/h5/xxx.文件后缀名，在根目录创建多少级) 这样的路径就对应多少级
然后重新点击发布就能得到两个文件夹
:
![img](https://img.kancloud.cn/af/0f/af0fb1c433f3b52e680e29531365a9e4_650x164.png)
把这个放到服务器对应的 文件夹就可以了 然后访问服务器上对应的index.html就是H5页面了

2、微信小程序打包

1. 申请微信小程序AppID，参考：微信教程【https://developers.weixin.qq.com/miniprogram/dev/framework/quickstart/getstart.html#申请帐号】。

2. 在HBuilderX中顶部菜单上依次单击“发行” =>“小程序-微信”，输入小程序名称和appid单击发行即可在unpackage/dist/build/mp-weixin生成微信小程序项目代码。

   ![img](https://img.kancloud.cn/6e/2c/6e2c306c9674accc64ec8f5d24e14c06_1920x1080.png)

   输入小程序的appid和名称
   ![img](https://img.kancloud.cn/98/76/9876b1aaa9699ef1cd97e1cea67ec568_833x397.png)

   发行后在微信开发者工具中打开，
   **要注意小程序的合法域名的配置。以及微信小程序域名必须https开头**
   ![img](https://img.kancloud.cn/fd/ec/fdec91f7e7c2655f8f21e5c12b36b607_1919x748.png)
   小程序配置域名白名单：https://blog.csdn.net/weixin_44606457/article/details/105227254

   注意：
   因为小程序打包上传大小不能超过2048kb，所以在上传是需要对部分静态文件进行删除

   打开打包文件夹

   ![img](https://img.kancloud.cn/b2/d1/b2d15e18eb376e02ab7f146e302904a9_1311x693.png)

   打开static文件

   ![img](https://img.kancloud.cn/c7/af/c7af19209f34ba0b01ec5bf73832a937_385x263.png)

   删除下图圈中说明的文件夹
   ![img](https://img.kancloud.cn/c4/a4/c4a4b9b1fa8629b8d484cfcf0fe63c2d_1499x374.png)

   然后再上传才符合小程序要求的大小

   另外一种方式：目录条件编译：
   https://uniapp.dcloud.io/platform?id=static-目录的条件编译

   小程序配置业务域名

   ![img](https://img.kancloud.cn/f9/34/f934c7965f8e7c9767574651537f6611_1305x801.png)

   ![img](https://img.kancloud.cn/35/79/3579d02a52f31e74b8c198101ad24b9a_1445x776.png)

   ### 三、websocket使用

   对应vue页面引用socket.js，并进行配置

   ```
   <script>
       //引用socket.js
       import socket from '@/common/js-sdk/socket/socket.js'
   
       export default {
           data() {
               return {
               }
           },
           mounted() { 
                 //初始化websocket
                 this.onSocketOpen()
                 this.onSocketReceive()
           },
           destroyed: function () { // 离开页面生命周期函数
                 socket.closeSocket()
           },
           methods: {
                 onSocketOpen: function () {
                   // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
                   socket.init('websocket'); //对应要连接的socket
                 },
                 onSocketReceive: function () {
                      var _this=this
                      socket.acceptMessage = function(res){
       					// console.log("页面收到的消息", res);
       					if(res.cmd == "topic"){
       					  //系统通知
       					}else if(res.cmd == "user"){
       					  //用户消息
       					} else if(res.cmd == 'email'){
       					 //邮件消息
       					}
       			}
                 }
           }
       }
   </script>
   ```

   

   

